package services;

import com.google.inject.Inject;
import models.*;
import models.helpers.ConversationHelper;
import models.helpers.JobHelper;
import models.helpers.LocationHelper;
import models.helpers.SingupHelper;
import org.junit.Before;
import org.junit.Test;
import util.BaseServiceTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;

public class ConversationServiceTest extends BaseServiceTest {
    @Inject
    IAuthenticationService authenticationService;

    @Inject
    IJobService jobService;

    @Inject
    IConversationService conversationService;

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
        SingupHelper candidateHelper = new SingupHelper();
        candidateHelper.emailAddress = EMAIL;
        candidateHelper.password = PASSWORD;

        SingupHelper companyHelper = new SingupHelper();
        companyHelper.emailAddress = COMPANY_EMAIL;
        companyHelper.password = COMPANY_PASSWORD;

        SingupHelper employerHelper = new SingupHelper();
        employerHelper.emailAddress = EMPLOYER_EMAIL;
        employerHelper.password = EMPLOYER_PASSWORD;

        candidateUser = authenticationService.singUp(null, UserModel.Role.CANDIDATE, candidateHelper);
        companyUser = authenticationService.singUp(null, UserModel.Role.COMPANY, companyHelper);
        employerUser = authenticationService.singUp(companyUser, UserModel.Role.EMPLOYER, employerHelper);
    }

    @Test
    public void createConversationTest() {
        JobHelper job = new JobHelper();
        job.title = "Test Job";
        job.description = "Test job description";
        job.location = new LocationHelper("US", "TX", "Test city 1");
        job.skills = new ArrayList<>(Arrays.asList("java", "js"));
        job.experience = 1;
        job.maxSalary = 10000;

        Long jobId = jobService.createJob(employerUser, job).id;

        Long applicationId = jobService.applyToJob(candidateUser, jobId).id;

        ConversationHelper conversationHelper = conversationService.create(employerUser, "test", applicationId, Arrays.asList(candidateUser.id));

        assertThat(conversationHelper.members.size()).isEqualTo(2);
    }
}
