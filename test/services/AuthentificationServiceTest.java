package services;

import com.google.inject.Inject;
import models.EmployerModel;
import models.UserModel;
import models.helpers.SingupHelper;
import org.junit.Before;
import org.junit.Test;
import util.BaseServiceTest;
import utils.exceptions.BusinessValidationException;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class AuthentificationServiceTest extends BaseServiceTest {

    @Inject
    IAuthenticationService authentificationService;

    @Inject
    IUserService userService;

    private static final String COMPANY_EMAIL = "company@email.com";
    private static final String COMPANY_PASSWORD = "companypassword";

    private static final String CANDIDATE_EMAIL = "new@email.com";
    private static final String CANDIDATE_PASSWORD = "candidatepassword";

    private static final String EXISTED_EMAIL = "fake@email.com";
    private static final String EXISTED_PASSWORD = "fakepassword";

    private static final String EMPLOYER_EMAIL = "employer@email.com";

    @Before
    public void before() {
        new UserModel(EXISTED_EMAIL, EXISTED_PASSWORD, UserModel.Role.CANDIDATE).save();
    }

    @Test
    public void candidateSingUpSuccessTest() {
        SingupHelper helper = new SingupHelper();
        helper.emailAddress = CANDIDATE_EMAIL;
        helper.password = CANDIDATE_PASSWORD;

        UserModel user = authentificationService.singUp(null, UserModel.Role.CANDIDATE, helper);

        assertThat(user.emailAddress).isEqualTo(CANDIDATE_EMAIL);
        assertThat(user.authToken).isNotNull();
        assertThat(user.role).isEqualTo(UserModel.Role.CANDIDATE);
    }

    @Test
    public void companySingUpSuccessTest() {
        SingupHelper helper = new SingupHelper();
        helper.emailAddress = COMPANY_EMAIL;
        helper.password = COMPANY_PASSWORD;

        UserModel user = authentificationService.singUp(null, UserModel.Role.COMPANY, helper);

        assertThat(user.emailAddress).isEqualTo(COMPANY_EMAIL);
        assertThat(user.authToken).isNotNull();
        assertThat(user.role).isEqualTo(UserModel.Role.COMPANY);
    }

    @Test
    public void employerSingUpSuccessTest() {
        SingupHelper companyHelper = new SingupHelper();
        companyHelper.emailAddress = COMPANY_EMAIL;
        companyHelper.password = COMPANY_PASSWORD;

        UserModel company = authentificationService.singUp(null, UserModel.Role.COMPANY, companyHelper);

        SingupHelper employerHelper = new SingupHelper();
        employerHelper.emailAddress = EMPLOYER_EMAIL;

        UserModel employer = authentificationService.singUp(company, UserModel.Role.EMPLOYER, employerHelper);

        assertThat(employer.emailAddress).isEqualTo(EMPLOYER_EMAIL);
        assertThat(employer.authToken).isNull();
        assertThat(employer.role).isEqualTo(UserModel.Role.EMPLOYER);
    }

    @Test
    public void employerSingUpCascadeTest() {
        SingupHelper companyHelper = new SingupHelper();
        companyHelper.emailAddress = COMPANY_EMAIL;
        companyHelper.password = COMPANY_PASSWORD;

        SingupHelper employerHelper = new SingupHelper();
        employerHelper.emailAddress = EMPLOYER_EMAIL;


        UserModel company = authentificationService.singUp(null, UserModel.Role.COMPANY, companyHelper);
        authentificationService.singUp(company, UserModel.Role.EMPLOYER, employerHelper);

        List<EmployerModel> employees = userService.findEmployeesByCompanyId(company.company.id);

        assertThat(employees.size()).isEqualTo(1);

        assertThat(employees.get(0).user.emailAddress).isEqualTo(EMPLOYER_EMAIL);
    }


    @Test(expected = BusinessValidationException.class)
    public void singUpExistedFailTest() {
        SingupHelper helper = new SingupHelper();
        helper.emailAddress = EXISTED_EMAIL;
        helper.password = EXISTED_PASSWORD;

        authentificationService.singUp(null, UserModel.Role.CANDIDATE, helper);
    }

    @Test(expected = BusinessValidationException.class)
    public void singUpExistedEmployerFailTest() {
        SingupHelper companyHelper = new SingupHelper();
        companyHelper.emailAddress = COMPANY_EMAIL;
        companyHelper.password = COMPANY_PASSWORD;

        UserModel company = authentificationService.singUp(null, UserModel.Role.COMPANY, companyHelper);

        SingupHelper employerHelper = new SingupHelper();
        employerHelper.emailAddress = EXISTED_EMAIL;

        UserModel employer = authentificationService.singUp(company, UserModel.Role.EMPLOYER, employerHelper);
    }
}
