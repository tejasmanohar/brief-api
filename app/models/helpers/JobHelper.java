package models.helpers;

import play.data.validation.Constraints;

import java.util.List;

public class JobHelper {
    @Constraints.Required
    public String title;

    @Constraints.Required
    public String description;

    public List<String> skills;

    public LocationHelper location;

    public Integer experience;

    public Integer minSalary;

    public Integer maxSalary;
}
