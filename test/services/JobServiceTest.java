package services;

import com.google.inject.Inject;
import models.*;
import models.helpers.JobHelper;
import models.helpers.LocationHelper;
import models.helpers.SingupHelper;
import org.junit.Before;
import org.junit.Test;
import util.BaseServiceTest;
import utils.exceptions.BusinessValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class JobServiceTest extends BaseServiceTest {

    @Inject
    IJobService jobService;

    @Inject
    IAuthenticationService authentificationService;

    private static final String EMAIL = "candidate@email.com";
    private static final String PASSWORD = "candidate";
    private static final String EMPLOYER_EMAIL = "employer@email.com";
    private static final String EMPLOYER_PASSWORD = "employer";
    private static final String COMPANY_EMAIL = "company@email.com";
    private static final String COMPANY_PASSWORD = "company";

    UserModel candidateUser;
    UserModel companyUser;
    UserModel employerUser;

    @Before
    public void before() {
        SingupHelper helper = new SingupHelper();
        helper.emailAddress = EMAIL;
        helper.password = PASSWORD;

        candidateUser = authentificationService.singUp(null, UserModel.Role.CANDIDATE, helper);

        companyUser = new UserModel(COMPANY_EMAIL, COMPANY_PASSWORD, UserModel.Role.COMPANY);
        CompanyModel company = new CompanyModel();
        companyUser.company = company;
        companyUser.save();

        employerUser = new UserModel(EMPLOYER_EMAIL, EMPLOYER_PASSWORD, UserModel.Role.EMPLOYER);
        EmployerModel employer = new EmployerModel();
        employer.company = companyUser.company;
        employerUser.employer = employer;
        employerUser.save();
    }

    @Test
    public void createJobCompanyTest() {
        JobHelper helper = new JobHelper();
        helper.title = "Test Job";
        helper.description = "Test job description";
        helper.location = new LocationHelper("US", "TX", "Test city");
        helper.skills = new ArrayList<>(Arrays.asList("java", "js"));

        JobModel job = jobService.createJob(companyUser, helper);
        assertThat(job).isNotNull();
        assertThat(job.company.id).isEqualTo(companyUser.company.id);
    }

    @Test
    public void createJobEmployerTest() {
        JobHelper helper = new JobHelper();
        helper.title = "Test Job";
        helper.description = "Test job description";
        helper.location = new LocationHelper("US", "TX", "Test city");
        helper.skills = new ArrayList<>(Arrays.asList("java", "js"));

        JobModel job = jobService.createJob(employerUser, helper);
        assertThat(job).isNotNull();
        assertThat(job.company.id).isEqualTo(companyUser.company.id);
    }

    @Test(expected = BusinessValidationException.class)
    public void createJobCandidateTestFail() {
        JobHelper helper = new JobHelper();
        helper.title = "Test Job";
        helper.description = "Test job description";

        jobService.createJob(candidateUser, helper);
    }

    @Test
    public void findJobTest() {
        JobHelper job1 = new JobHelper();
        job1.title = "Test Job";
        job1.description = "Test job description";
        job1.location = new LocationHelper("US", "TX", "Test city 1");
        job1.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job1.experience = 1;
        job1.maxSalary = 10000;

        JobHelper job2 = new JobHelper();
        job2.title = "Test Job";
        job2.description = "Test job description";
        job2.location = new LocationHelper("US", "TX", "Test city 2");
        job2.skills = new ArrayList<>(Arrays.asList("java", "html"));
        job2.experience = 2;
        job2.maxSalary = 20000;

        JobHelper job3 = new JobHelper();
        job3.title = "Test Job";
        job3.description = "Test job description";
        job3.location = new LocationHelper("US", "MS", "Test city 3");
        job3.skills = new ArrayList<>(Arrays.asList("css", "ui"));
        job3.experience = 3;
        job3.maxSalary = 30000;

        jobService.createJob(employerUser, job1);
        jobService.createJob(employerUser, job2);
        jobService.createJob(employerUser, job3);

        List<JobModel> jobs = jobService.findJobs(employerUser, "US", null, null, null, null, null);
        assertThat(jobs.size()).isEqualTo(3);

        jobs = jobService.findJobs(employerUser, "US", "MS", null, null, null, null);
        assertThat(jobs.size()).isEqualTo(1);

        jobs = jobService.findJobs(employerUser, "US", "MS", "Test city 3", null, null, null);
        assertThat(jobs.size()).isEqualTo(1);

        jobs = jobService.findJobs(employerUser, "US", null, null, 2, null, null);
        assertThat(jobs.size()).isEqualTo(2);

        jobs = jobService.findJobs(employerUser, "US", null, null, 2, 30000, null);
        assertThat(jobs.size()).isEqualTo(1);

        jobs = jobService.findJobs(employerUser, "US", null, null, null, null, "java");
        assertThat(jobs.size()).isEqualTo(2);

        jobs = jobService.findJobs(employerUser, "US", null, null, null, null, "css,ui");
        assertThat(jobs.size()).isEqualTo(1);

        jobs = jobService.findJobs(employerUser, "US", null, null, null, null, "js,ui");
        assertThat(jobs.size()).isEqualTo(0);
    }

    @Test
    public void applyToJobTest(){
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        JobApplyModel application = jobService.applyToJob(candidateUser, jobId);

        assertThat(application.candidate.id).isEqualTo(candidateUser.candidate.id);
        assertThat(application.job.id).isEqualTo(jobId);
    }

    @Test(expected = BusinessValidationException.class)
    public void applyToJobTestFail(){
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        jobService.applyToJob(candidateUser, jobId);
        jobService.applyToJob(candidateUser, jobId);
    }

    @Test
    public void dismissJobTest(){
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        JobModel jobModel =  jobService.dismissJob(candidateUser, jobId);
        assertThat(jobModel.visitors.size()).isEqualTo(1);
    }

    @Test(expected = BusinessValidationException.class)
    public void dismissJobTestFail(){
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        jobService.dismissJob(candidateUser, jobId);
        jobService.dismissJob(candidateUser, jobId);
    }

    @Test
    public void changeApplicationStatusToInterviewedTest() {
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        Long applicationId = jobService.applyToJob(candidateUser, jobId).id;

        JobApplyModel application = jobService.changeApplicationStatus(employerUser, applicationId, JobApplyModel.Status.INTERVIEWED);
        assertThat(application.status).isEqualTo(JobApplyModel.Status.INTERVIEWED);
    }

    @Test(expected = BusinessValidationException.class)
    public void changeApplicationStatusToInterviewedTestFail() {
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        Long applicationId = jobService.applyToJob(candidateUser, jobId).id;

        jobService.changeApplicationStatus(employerUser, applicationId, JobApplyModel.Status.INTERVIEWED);
        jobService.changeApplicationStatus(employerUser, applicationId, JobApplyModel.Status.INTERVIEWED);
    }

    @Test
    public void changeApplicationStatusToHiredTest() {
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        Long applicationId = jobService.applyToJob(candidateUser, jobId).id;

        JobApplyModel application = jobService.changeApplicationStatus(employerUser, applicationId, JobApplyModel.Status.HIRED);
        assertThat(application.status).isEqualTo(JobApplyModel.Status.HIRED);
    }

    @Test
    public void changeApplicationStatusRejectedTest() {
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        Long applicationId = jobService.applyToJob(candidateUser, jobId).id;

        JobApplyModel application = jobService.changeApplicationStatus(employerUser, applicationId, JobApplyModel.Status.REJECTED);
        assertThat(application.status).isEqualTo(JobApplyModel.Status.REJECTED);
    }
}