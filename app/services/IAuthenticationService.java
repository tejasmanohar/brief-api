package services;

import models.UserModel;
import models.helpers.FacebookLoginHelper;
import models.helpers.LoginHelper;
import models.helpers.SingupHelper;
import models.helpers.TwitterLoginHelper;

public interface IAuthenticationService {
    UserModel singUp(UserModel owner, UserModel.Role role, SingupHelper singup);
    UserModel singIn(LoginHelper login);
    UserModel facebookSingIn(FacebookLoginHelper facebookHelper);
    UserModel twitterSingIn(TwitterLoginHelper twitterHelper);
    UserModel setUpCredentials(UserModel user, SingupHelper credentials);
}
