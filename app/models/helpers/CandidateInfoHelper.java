package models.helpers;

import models.CandidateModel;
import models.EducationModel;
import models.ExperienceModel;
import models.SkillModel;
import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CandidateInfoHelper {
    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.MaxLength(256)
    public String summary;

    @Constraints.Required
    public LocationHelper location;

    public List<ExperienceHelper> experience;

    public List<EducationHelper> education;

    public Set<SkillModel> skills;

    public CandidateInfoHelper() {}

    public CandidateInfoHelper(CandidateModel basicInfo) {
        if(basicInfo != null) {
            firstName = basicInfo.firstName;
            lastName = basicInfo.lastName;
            summary = basicInfo.summary;
            location = basicInfo.countryCode == null ? null : new LocationHelper(basicInfo.countryCode, basicInfo.stateCode, basicInfo.cityName);
            education = toEducationHelperList(basicInfo);
            experience = toExperienceHelperList(basicInfo);
            skills = basicInfo.skills;
        }
    }

    public List<EducationHelper> toEducationHelperList(CandidateModel basicInfo) {
        List<EducationHelper> list = new ArrayList<>();

        for(EducationModel ed : basicInfo.education) {
            list.add(ed.toEducationHelper());
        }

        return list;
    }

    public List<ExperienceHelper> toExperienceHelperList(CandidateModel basicInfo) {
        List<ExperienceHelper> list = new ArrayList<>();

        for(ExperienceModel ex : basicInfo.experience) {
            list.add(ex.toExperienceHelper());
        }

        return list;
    }
}
