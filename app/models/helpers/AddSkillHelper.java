package models.helpers;

import play.data.validation.Constraints;

import java.util.List;

public class AddSkillHelper {
    @Constraints.Required
    public List<SkillHelper> skills;
}
