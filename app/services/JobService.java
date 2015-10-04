package services;

import com.google.inject.Inject;
import controllers.secured.Candidate;
import models.*;
import models.helpers.JobHelper;
import models.utils.Model;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import play.Logger;
import play.db.jpa.JPA;
import utils.exceptions.BusinessValidationException;

import java.util.*;
import java.util.stream.IntStream;

public class JobService implements IJobService {

    @Inject
    IConversationService conversationService;

    @Inject
    MailService mailService;

    @Override
    public JobModel createJob(UserModel owner, JobHelper helper) {

        if(owner.role.equals(UserModel.Role.CANDIDATE)) {
            throw new BusinessValidationException("role", "Invalid user role");
        }

        CompanyModel company = null;

        switch(owner.role) {
            case COMPANY:
                company = owner.company;
                break;
            case EMPLOYER:
                company = owner.employer.company;
                break;
        }

        JobModel job = new JobModel();
        job.title = helper.title;
        job.description = helper.description;
        job.experience = helper.experience;
        job.minSalary = helper.minSalary;
        job.maxSalary = helper.maxSalary;
        if(helper.location != null) {
            job.countryCode = helper.location.countryCode;
            job.stateCode = helper.location.stateCode;
            job.cityName = helper.location.cityName;
        }
        job.company = company;

        if(helper.skills != null) {
            helper.skills.forEach(skillName -> {
                SkillModel skill = SkillModel.findByName(skillName);

                if (skill == null) {
                    skill = new SkillModel(skillName);
                    skill.save();
                }

                job.skills.add(skill);
            });
        }

        job.save();

        return job;
    }

    @Override
    public JobApplyModel applyToJob(UserModel candidate, Long jobId) {
        JobModel job = JobModel.find.byId(jobId);

        if(job == null) {
            throw new BusinessValidationException("role", "Can't find job");
        }

        if(JobApplyModel.find.where().eq("candidate.id", candidate.candidate.id).eq("job.id",jobId).findUnique() != null) {
            throw new BusinessValidationException("jobId", "This user already applied for this job");
        }

        JobApplyModel jobApply = new JobApplyModel();
        jobApply.job = job;
        jobApply.candidate = candidate.candidate;
        jobApply.status = JobApplyModel.Status.PENDING;
        jobApply.save();

        job.visitors.add(candidate.candidate);
        job.save();

        return jobApply;
    }

    @Override
    public JobModel dismissJob(UserModel candidate, Long jobId) {
        JobModel job = JobModel.find.byId(jobId);

        if(job == null) {
            throw new BusinessValidationException("role", "Can't find job");
        }

        if(JobApplyModel.find.where().eq("candidate.id", candidate.candidate.id).eq("job.id",jobId).findUnique() != null) {
            throw new BusinessValidationException("jobId", "This user already applied for this job");
        }

        if(job.visitors.stream().filter(visitor -> candidate.id.equals(candidate.id)).findFirst().isPresent()) {
            throw new BusinessValidationException("jobId", "This user already checked this job");
        }

        job.visitors.add(candidate.candidate);
        job.save();

        return job;
    }

    @Override
    public JobApplyModel changeApplicationStatus(UserModel employer, Long applicationId, JobApplyModel.Status status) {
        JobApplyModel application = JobApplyModel.find.byId(applicationId);

        if(application == null) {
            throw new BusinessValidationException("applicationId", "Can't find application");
        }

        if(!application.job.company.id.equals(employer.employer.company.id)) {
            throw new BusinessValidationException("access", "Access denied");
        }

        String subject = null;
        String body = null;

        switch (status) {
            case INTERVIEWED:
                Logger.info("ConversationModel : " + ConversationModel.find.where().eq("jobApplication.id", applicationId).findUnique());
                if(ConversationModel.find.where().eq("jobApplication.id", applicationId).findUnique()!= null) {
                    throw new BusinessValidationException("status", "Can't change status to INTERVIEWED. Status already changed to this before");
                }
                Logger.info("candidate " + application.candidate);
                Logger.info("user " + application.candidate.user);
                conversationService.create(employer, application.job.title + " interview", applicationId, Arrays.asList(employer.id, application.candidate.user.id));

                subject = "Invite to interview";
                body = "You have received invite to interview about " + application.job.title + " job";
                break;

            case HIRED:
                subject = "Hire to job";
                body = "You have been hired to " + application.job.title + " job";
                break;
            case REJECTED:
                subject = "Request rejected";
                body = "Your request for " + application.job.title + " job have been rejected";
        }

        mailService.sendMail(
                application.candidate.user.emailAddress,
                subject,
                body
        );

        application.status = status;
        application.save();

        return application;
    }

    @Override
    public List<JobModel> findJobs(UserModel user, String countryCode, String stateCode,
                              String cityName, Integer experience,
                              Integer salary, String skills) {

        Model.Query findQuery = JobModel.find.where();

        if(countryCode != null) {
            findQuery.eq("countryCode", countryCode);

            if(stateCode != null) {
                findQuery.eq("stateCode", stateCode);
            }

            if(cityName != null) {
                findQuery.eq("cityName", cityName);
            }
        }

        if(experience != null) {
            findQuery.ge("experience", experience);
        }

        if(salary != null) {
            findQuery.ge("maxSalary", salary);
        }

        if(skills != null) {
            List<Long> ids = getJobIdsBySkills(skills);
            if(ids.isEmpty()) {
                return Collections.EMPTY_LIST;
            } else {
                findQuery.in("id", getJobIdsBySkills(skills));
            }
        }

        if(user.role.equals(UserModel.Role.CANDIDATE)){
            List<Long> ids = getJobIdsByVisitor(user.candidate.id);
            Logger.info("VISITORS IDS : " + ids.size());
            if(!ids.isEmpty()) {
                Criterion notVisited = Restrictions.in("id", getJobIdsByVisitor(user.candidate.id));
                findQuery.not(notVisited);
            }
        }

        return findQuery.findList();
    }

    @Override
    public List<JobApplyModel> getJobApplicants(UserModel jobOwner, Long jobId) {
        JobModel job = JobModel.find.byId(jobId);

        if(job == null) {
            throw new BusinessValidationException("id", "Can't find job");
        }

        if(jobOwner.role.equals(UserModel.Role.COMPANY)) {
            if(!jobOwner.company.id.equals(job.companyId())) {
                throw new BusinessValidationException("id", "Can't find job for your company");
            }
        } else if(!jobOwner.role.equals(UserModel.Role.EMPLOYER)) {
            if(!jobOwner.employer.companyId().equals(job.companyId())) {
                throw new BusinessValidationException("id", "Can't find job for your company");
            }
        }

        return JobModel.find.byId(jobId).jobApplications;
    }

    private List<Long> getJobIdsByVisitor(Long visitorId) {
        String sql = "SELECT DISTINCT t0.jobmodel_id id FROM jobmodel_candidatemodel t0 WHERE t0.visitors_id = " + visitorId ;

        List<SearchResult> results = JPA.em().createNativeQuery(sql, SearchResult.class).getResultList();
        List<Long> ids = new ArrayList<>();
        results.forEach(result -> ids.add(result.id));
        return ids;
    }

    private List<Long> getJobIdsBySkills(String skills) {
        Set<String> uniqueNames = new LinkedHashSet<>();

        Arrays.asList(skills.split(",")).forEach(
                name -> uniqueNames.add(name.replaceAll("\\s+", "").toLowerCase())
        );

        List<String> skillNames = new ArrayList<>(uniqueNames);

        StringBuilder skillsInQueryBuilder = new StringBuilder();

        skillsInQueryBuilder.append("'" + skillNames.get(0).replaceAll("\\s+","").toLowerCase() + "'");

        IntStream.range(1, skillNames.size()).forEach(
                idx -> skillsInQueryBuilder.append(
                        ",'" + skillNames.get(idx).replaceAll("\\s+", "").toLowerCase() + "'"
                )
        );

        String sql = "" +
                "SELECT DISTINCT t0.id FROM jobmodel t0 " +
                "JOIN jobmodel_skillmodel u1z_ ON u1z_.jobmodel_id = t0.id  " +
                "JOIN skillmodel u1 ON u1.id = u1z_.skills_id  " +
                "WHERE u1.name IN (" +
                skillsInQueryBuilder.toString() +
                ") GROUP BY t0.id HAVING COUNT(DISTINCT u1.id) = " + skillNames.size();

        List<SearchResult> results = JPA.em().createNativeQuery(sql, SearchResult.class).getResultList();
        List<Long> ids = new ArrayList<>();
        results.forEach(result -> ids.add(result.id));
        return ids;
    }
}
