package services;

import models.UserModel;
import models.helpers.AddSkillHelper;
import models.helpers.CandidateInfoHelper;

public interface ICandidateService {
    UserModel update(UserModel user, CandidateInfoHelper basicInfo);
    UserModel addSkills(UserModel user, AddSkillHelper skills);
    UserModel deleteSkill(UserModel user, String skill);
}
