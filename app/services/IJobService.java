package services;

import models.JobModel;
import models.JobApplyModel;
import models.UserModel;
import models.helpers.JobHelper;

import java.util.List;

public interface IJobService {
    JobModel createJob(UserModel owner, JobHelper helper);
    JobApplyModel applyToJob(UserModel candidate, Long jobId);
    JobModel dismissJob(UserModel candidate, Long jobId);
    JobApplyModel changeApplicationStatus(UserModel employeer, Long applicationId, JobApplyModel.Status status);
    List<JobModel> findJobs(UserModel user, String countryCode, String stateCode,
                       String cityName, Integer experience,
                       Integer salary, String skills);
    List<JobApplyModel> getJobApplicants(UserModel jobOwner, Long jobId);
}
