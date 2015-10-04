package services;

import models.EmployerModel;
import models.UserModel;
import utils.exceptions.BusinessValidationException;
import utils.secure.SecurityHelper;

import java.util.List;

public class UserService implements IUserService {

    @Override
    public UserModel verify(String emailAddress, String password) {
        return UserModel.find.where().eq("emailAddress", emailAddress).eq("shaPassword", SecurityHelper.getSha512(password)).findUnique();
    }

    @Override
    public UserModel getByToken(String authToken) {
        return UserModel.find.where().eq("authToken", authToken).findUnique();
    }

    @Override
    public UserModel findById(Long id) {
        return UserModel.find.byId(id);
    }

    @Override
    public UserModel findCandidateById(Long id) {
        return UserModel.find.where().eq("candidate.id", id).findUnique();
    }

    @Override
    public UserModel findCompanyById(Long id) {
        return UserModel.find.where().eq("company.id", id).findUnique();
    }

    @Override
    public UserModel findEmployerById(Long id) {
        return UserModel.find.where().eq("employer.id", id).findUnique();
    }

    @Override
    public List<EmployerModel> findEmployeesByCompanyId(Long id) {
        return EmployerModel.find.where().eq("company.id", id).findList();
    }

    @Override
    public UserModel findByEmail(String email) {
        return UserModel.find.where().eq("emailAddress", email).findUnique();
    }

    @Override
    public UserModel findByTwitterId(Long id) {
        return UserModel.find.where().eq("twitterId", id).findUnique();
    }

    @Override
    public Boolean validate(String emailAddress) {
        UserModel user = UserModel.find.where().eq("emailAddress", emailAddress).findUnique();

        if(user != null) {
            throw new BusinessValidationException("emailAddress", "UserModel with this email already exists");
        }

        return Boolean.TRUE;
    }
}
