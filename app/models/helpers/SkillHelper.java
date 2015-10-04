package models.helpers;

import play.data.validation.Constraints;

public class SkillHelper {
    @Constraints.Required
    public String skill;
}
