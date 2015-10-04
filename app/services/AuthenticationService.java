package services;

import actors.MailActor;
import actors.messages.MailMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.*;
import models.helpers.*;
import play.libs.Json;
import play.libs.ws.WS;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import utils.config.AppConfigurations;
import utils.exceptions.BusinessValidationException;

import java.util.UUID;

public class AuthenticationService implements IAuthenticationService {

    @Inject
    IUserService userService;

    @Inject
    MailService mailService;

    @Override
    public UserModel singUp(UserModel owner, UserModel.Role role, SingupHelper singup) {
        userService.validate(singup.emailAddress);
        UserModel userToSave = new UserModel(singup.emailAddress, singup.password, role);

        return saveUser(owner, userToSave);
    }

    @Override
    public UserModel singIn(LoginHelper login) {
        UserModel user = userService.verify(login.emailAddress, login.password);

        if (user == null){
            throw new BusinessValidationException("credentials", "Invalid credentials");
        }

        if(user.authToken == null){
            user.generateAuthTokenToken();
            user.save();
        }

        return user;
    }

    @Override
    public UserModel facebookSingIn(FacebookLoginHelper facebookHelper) {
        JsonNode graphJson = WS.url(AppConfigurations.Facebook.GRAPH_URL + facebookHelper.accessToken)
                .get()
                .get(AppConfigurations.Facebook.API_TIMEOUT)
                .asJson();

        validateFacebookGraph(graphJson);

        FacebookUser user = Json.fromJson(graphJson, FacebookUser.class);

        return singInFacebookUser(user);
    }

    @Override
    public UserModel twitterSingIn(TwitterLoginHelper twitterHelper) {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(AppConfigurations.Twitter.getAuthConsumerKey())
                .setOAuthConsumerSecret(AppConfigurations.Twitter.getAuthConsumerSecret())
                .setOAuthAccessToken(twitterHelper.accessToken)
                .setOAuthAccessTokenSecret(twitterHelper.accessSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        UserModel twitterUser;

        try {
            twitterUser = singInTwitterUser(twitter.verifyCredentials());
        } catch (TwitterException e) {
            throw new BusinessValidationException("twitterLogin", e.getErrorMessage());
        }

        return twitterUser;
    }

    @Override
    public UserModel setUpCredentials(UserModel user, SingupHelper credentials) {
        if(user.emailAddress != null) {
            throw new BusinessValidationException("emailAddress", "Email address already specified");
        } else {
            user.emailAddress = credentials.emailAddress;
            user.setPassword(credentials.password);
            user.save();
        }

        return user;
    }

    private UserModel singInFacebookUser(FacebookUser facebookUser) {
        UserModel user = userService.findByEmail(facebookUser.emailAddress);

        if(user == null) {
            return singUp(null, UserModel.Role.CANDIDATE, facebookUser.toSingup());
        } else {
            if(user.authToken == null) {
                user.generateAuthTokenToken();
                user.save();
            }

            return user;
        }
    }

    private UserModel singInTwitterUser(twitter4j.User twitterUser) {
        UserModel user = userService.findByTwitterId(twitterUser.getId());

        if(user == null) {
            return saveUser(null, new UserModel(null, null, UserModel.Role.CANDIDATE, twitterUser.getId()));
        } else {
            if(user.authToken == null) {
                user.generateAuthTokenToken();
                user.save();
            }

            return user;
        }
    }

    private Boolean validateFacebookGraph(JsonNode graphJson) {
        if(graphJson.get("error") != null) {
            throw new BusinessValidationException("accessToken", graphJson.get("error").get("message").asText());
        }

        return Boolean.TRUE;
    }

    private UserModel saveUser(UserModel owner, UserModel user) {
        switch (user.role) {
            case CANDIDATE:
                user.generateAuthTokenToken();
                createCandidate(user);
                break;
            case COMPANY:
                user.generateAuthTokenToken();
                createCompany(user);
                break;
            case EMPLOYER:
                String generatedPassword = "briefapp"; //UUID.randomUUID().toString();
                user.setPassword(generatedPassword);
                createEmployer(user, owner);
                sendWelcomeMail(user);
                break;
        }

        return user;
    }

    private void sendWelcomeMail(UserModel user) {
        mailService.sendMail(
                user.emailAddress,
                "Welcome to Brief System",
                "Welcome to Brief System. \n\nCredentials for access:\n userName: " + user.emailAddress + "\n password: " + user.password
        );
    }

    private void createEmployer(UserModel employer, UserModel company) {
        employer.employer = new EmployerModel();
        employer.employer.user = employer;
        employer.employer.company = company.company;
        employer.save();
    }

    private void createCompany(UserModel company) {
        company.company = new CompanyModel();
        company.company.user = company;
        company.save();
    }

    private void createCandidate(UserModel candidate) {
        candidate.candidate = new CandidateModel();
        candidate.candidate.user = candidate;
        candidate.save();
    }
}
