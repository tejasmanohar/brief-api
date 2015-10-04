package models.helpers;

import play.data.format.Formats;
import play.data.validation.Constraints;

import java.util.Date;

public class ExperienceHelper {
    @Constraints.MaxLength(256)
    @Constraints.Required
    public String company;

    @Constraints.MaxLength(256)
    @Constraints.Required
    public String position;

    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy-MM")
    public Date startDate;

    @Formats.DateTime(pattern = "yyyy-MM")
    public Date endDate;

    public ExperienceHelper(){};

    public ExperienceHelper(String company, String position, Date startDate, Date endDate) {
        this.company = company;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
