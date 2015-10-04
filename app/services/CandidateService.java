package services;

import models.*;
import models.helpers.AddSkillHelper;
import models.helpers.CandidateInfoHelper;
import models.helpers.LocationHelper;
import utils.exceptions.BusinessValidationException;

public class CandidateService implements ICandidateService {

    @Override
    public UserModel update(UserModel user, CandidateInfoHelper basicInfo) {
        LocationHelper location = getLocation(basicInfo.location.countryCode, basicInfo.location.stateCode, basicInfo.location.cityName);

        addBasicInfo(user, basicInfo);
        setLocation(user, location);
        addExperience(user, basicInfo);
        addEducation(user, basicInfo);

        user.save();

        return user;
    }

    @Override
    public UserModel addSkills(UserModel user, AddSkillHelper skills) {
        skills.skills.forEach(skillHelper -> {
            SkillModel skill = SkillModel.findByName(skillHelper.skill);

            if (skill == null) {
                skill = new SkillModel(skillHelper.skill);
                skill.save();
            }

            user.candidate.skills.add(skill);
        });

        user.save();
        return user;
    }

    @Override
    public UserModel deleteSkill(UserModel user, String skillName) {
        SkillModel skill = SkillModel.findByName(skillName);

        if(skill != null) {
            user.candidate.skills.remove(skill);
            user.save();
        }

        return user;
    }

    public LocationHelper getLocation(String countryCode, String stateCode, String cityName) {
        CountryModel country = CountryModel.findByCode(countryCode);

        if(country == null) {
            throw new BusinessValidationException("countryCode", "Can't find country");
        }

        StateModel state = null;

        if(stateCode != null) {
            state = StateModel.findByCode(stateCode);
        }

        if(country.countryCode.equals("US") && state == null) {
            if(stateCode == null) {
                throw new BusinessValidationException("stateCode", "StateModel should be specified");
            } else {
                throw new BusinessValidationException("stateCode", "Can't find state");
            }
        }

        if(cityName == null || cityName.isEmpty()) {
            throw new BusinessValidationException("cityName", "StateModel should be specified");
        } else if (!country.countryCode.equals("US")) {
            state = null;
        }

        return new LocationHelper(country.countryCode, state == null ? null : state.code, cityName);
    }


    private void setLocation(UserModel user, LocationHelper location) {
        user.candidate.countryCode = location.countryCode;
        user.candidate.stateCode = location.stateCode;
        user.candidate.cityName = location.cityName;
    }

    private void addBasicInfo(UserModel user, CandidateInfoHelper helper) {
        user.candidate.firstName = helper.firstName;
        user.candidate.lastName = helper.lastName;
        user.candidate.summary = helper.summary;
    }

    private void addEducation(UserModel user, CandidateInfoHelper helper) {
        if(helper.education == null) {
            return;
        }

        user.candidate.education.forEach(education -> education.delete());
        user.candidate.education.clear();

        helper.education.forEach(educationHelper -> {
            user.candidate.education.add(new EducationModel(
                            educationHelper.school,
                            educationHelper.degree,
                            educationHelper.startDate,
                            educationHelper.endDate,
                            user.candidate
                    )
            );
        });
    }

    private void addExperience(UserModel user, CandidateInfoHelper helper) {
        if(helper.experience == null) {
            return;
        }

        user.candidate.experience.forEach(experience ->experience.delete());
        user.candidate.experience.clear();

        helper.experience.forEach(experienceHelper -> {
            user.candidate.experience.add(new ExperienceModel(
                            experienceHelper.company,
                            experienceHelper.position,
                            experienceHelper.startDate,
                            experienceHelper.endDate,
                            user.candidate
                    )
            );
        });

        user.candidate.user = user;
    }

}
