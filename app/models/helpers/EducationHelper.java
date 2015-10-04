package models.helpers;

import play.data.format.Formats;
import play.data.validation.Constraints;

import java.util.Date;

public class EducationHelper {
    @Constraints.MaxLength(256)
    @Constraints.Required
    public String school;

    @Constraints.MaxLength(256)
    public String degree;

    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy")
    public Date startDate;

    @Formats.DateTime(pattern = "yyyy")
    public Date endDate;

    public EducationHelper(){};

    public EducationHelper(String school, String degree, Date startDate, Date endDate) {
        this.school = school;
        this.degree = degree;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
