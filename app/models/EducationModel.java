package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.helpers.EducationHelper;
import models.utils.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

@Entity
public class EducationModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.MaxLength(256)
    @Constraints.Required
    public String school;

    @Constraints.MaxLength(256)
    public String degree;

    @Constraints.Required
    @JsonFormat(pattern = "yyyy")
    public Date startDate;

    @JsonFormat(pattern = "yyyy")
    public Date endDate;

    @ManyToOne
    @JsonIgnore
    public CandidateModel candidate;

    public EducationModel() {}

    public EducationModel(String school, String degree, Date startDate, Date endDate) {
        this.school = school;
        this.degree = degree;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public EducationModel(String school, String degree, Date startDate, Date endDate, CandidateModel candidate) {
        this.school = school;
        this.degree = degree;
        this.startDate = startDate;
        this.endDate = endDate;
        this.candidate = candidate;
    }


    public EducationHelper toEducationHelper() {
        return new EducationHelper(school, degree, startDate, endDate);
    }
}
