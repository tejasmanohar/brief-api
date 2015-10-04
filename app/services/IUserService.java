package services;

import models.EmployerModel;
import models.UserModel;

import java.util.List;

public interface IUserService {
    Boolean validate(String emailAddress);
    UserModel verify(String emailAddress, String password);
    UserModel getByToken(String authToken);
    UserModel findById(Long id);
    UserModel findCandidateById(Long id);
    UserModel findCompanyById(Long id);
    UserModel findEmployerById(Long id);
    UserModel findByEmail(String email);
    UserModel findByTwitterId(Long id);
    List<EmployerModel> findEmployeesByCompanyId(Long id);
}
