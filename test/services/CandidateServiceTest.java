package services;

import com.google.inject.Inject;
import models.UserModel;
import models.helpers.CandidateInfoHelper;
import models.helpers.LocationHelper;
import models.helpers.SingupHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import util.BaseServiceTest;
import utils.exceptions.BusinessValidationException;

import static org.fest.assertions.Assertions.assertThat;

public class CandidateServiceTest extends BaseServiceTest {

    @Inject
    ICandidateService candidateService;

    @Inject
    IAuthenticationService authentificationService;

    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "testuser";
    private static final String FIRST_NAME = "test";
    private static final String LAST_NAME = "user";
    private static final String SUMMARY = "blah blah blah";
    private static final String CITY_NAME = "test city";
    private static final String COUNTRY_CODE = "UA";
    private static final String US_CODE = "US";

    private UserModel user;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void before() {
        SingupHelper helper = new SingupHelper();
        helper.emailAddress = EMAIL;
        helper.password = PASSWORD;
        user = authentificationService.singUp(null, UserModel.Role.CANDIDATE, helper);
    }

    @Test
    public void updateSuccessTest() {
        LocationHelper locationHelper = new LocationHelper(COUNTRY_CODE, null, CITY_NAME);

        CandidateInfoHelper infoHelper = new CandidateInfoHelper();
        infoHelper.firstName = FIRST_NAME;
        infoHelper.lastName = LAST_NAME;
        infoHelper.summary = SUMMARY;
        infoHelper.location = locationHelper;

        UserModel updatedUser = candidateService.update(user, infoHelper);

        assertThat(updatedUser.candidate.firstName).isEqualTo(FIRST_NAME);
        assertThat(updatedUser.candidate.lastName).isEqualTo(LAST_NAME);
        assertThat(updatedUser.candidate.summary).isEqualTo(SUMMARY);
        assertThat(updatedUser.candidate.cityName).isEqualTo(CITY_NAME);
        assertThat(updatedUser.candidate.countryCode).isEqualTo(COUNTRY_CODE);
    }

    @Test
    public void updateInvalidCountryCodeFailTest() {
        LocationHelper locationHelper = new LocationHelper("FAKE", null, CITY_NAME);

        CandidateInfoHelper infoHelper = new CandidateInfoHelper();
        infoHelper.firstName = FIRST_NAME;
        infoHelper.lastName = LAST_NAME;
        infoHelper.summary = SUMMARY;
        infoHelper.location = locationHelper;

        expectedEx.expect(BusinessValidationException.class);
        expectedEx.expectMessage("Can't find country");

        candidateService.update(user, infoHelper);
    }

    @Test
    public void updateInvalidStateFailTest() {
        LocationHelper locationHelper = new LocationHelper(US_CODE, "FAKE", CITY_NAME);

        CandidateInfoHelper infoHelper = new CandidateInfoHelper();
        infoHelper.firstName = FIRST_NAME;
        infoHelper.lastName = LAST_NAME;
        infoHelper.summary = SUMMARY;
        infoHelper.location = locationHelper;

        expectedEx.expect(BusinessValidationException.class);
        expectedEx.expectMessage("Can't find state");

        candidateService.update(user, infoHelper);
    }

    @Test
    public void updateStateRequiredFOrUsFailTest() {
        LocationHelper locationHelper = new LocationHelper(US_CODE, null, CITY_NAME);

        CandidateInfoHelper infoHelper = new CandidateInfoHelper();
        infoHelper.firstName = FIRST_NAME;
        infoHelper.lastName = LAST_NAME;
        infoHelper.summary = SUMMARY;
        infoHelper.location = locationHelper;

        expectedEx.expect(BusinessValidationException.class);
        expectedEx.expectMessage("StateModel should be specified");

        candidateService.update(user, infoHelper);
    }
}
