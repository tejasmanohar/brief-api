package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.helpers.ExperienceHelper;
import models.utils.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ExperienceModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.MaxLength(256)
    @Constraints.Required
    public String company;

    @Constraints.MaxLength(256)
    @Constraints.Required
    public String position;

    @Constraints.Required
    @JsonFormat(pattern = "yyyy-MM")
    public Date startDate;

    @JsonFormat(pattern = "yyyy-MM")
    public Date endDate;

    @ManyToOne
    @JsonIgnore
    public CandidateModel candidate;

    public ExperienceModel() {}

    public ExperienceModel(String company, String position, Date startDate, Date endDate, CandidateModel candidate) {
        this.company = company;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.candidate = candidate;
    }

    public ExperienceHelper toExperienceHelper() {
        return new ExperienceHelper(company, position, startDate, endDate);
    }

}
