package services;

import com.google.inject.Inject;
import models.CandidateModel;
import models.EmployerModel;
import models.UserModel;
import org.junit.Before;
import org.junit.Test;
import util.BaseServiceTest;
import utils.exceptions.BusinessValidationException;

import static org.fest.assertions.Assertions.assertThat;

public class UserServiceTest extends BaseServiceTest {

    @Inject
    IUserService userService;

    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "testuser";
    private static final String EMPLOYER_EMAIL = "employer@email.com";
    private static final String EMPLOYER_PASSWORD = "employer";
    private static final String FAKE_EMAIL = "fake@email.com";
    private static final String FAKE_PASSWORD = "fakepassword";

    UserModel candidateUser;
    UserModel employerUser;

    @Before
    public void before() {
        candidateUser = new UserModel(EMAIL, PASSWORD, UserModel.Role.CANDIDATE);
        CandidateModel candidate = new CandidateModel();
        candidateUser.candidate = candidate;
        candidateUser.save();

        employerUser = new UserModel(EMPLOYER_EMAIL, EMPLOYER_PASSWORD, UserModel.Role.EMPLOYER);
        EmployerModel employer = new EmployerModel();
        employerUser.employer = employer;
        employerUser.save();
    }

    @Test
    public void validateSuccessTest() {
        assertThat(userService.validate(FAKE_EMAIL)).isTrue();
    }

    @Test(expected = BusinessValidationException.class)
    public void validateFailTest() {
        userService.validate(EMAIL);
    }

    @Test
    public void verifySuccessTest() {
        assertThat(userService.verify(EMAIL, PASSWORD)).isNotNull();
    }

    @Test
    public void verifyFailTest() {
        assertThat(userService.verify(FAKE_EMAIL, FAKE_PASSWORD)).isNull();
    }

    @Test
    public void findCandidateByIdTest() {
        assertThat(userService.findCandidateById(candidateUser.candidate.id)).isNotNull();
    }

    @Test
    public void findByEmailTest() {
        assertThat(userService.findByEmail(EMAIL)).isNotNull();
    }

    @Test
    public void findEmployerByIdTest() {
        assertThat(userService.findEmployerById(employerUser.employer.id)).isNotNull();
    }

}
