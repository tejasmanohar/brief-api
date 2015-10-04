package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.utils.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class JobModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256, nullable = false)
    public String title;

    @Column(length = 256, nullable = false)
    public String description;

    @ManyToMany(cascade = CascadeType.ALL)
    public Set<SkillModel> skills = new HashSet<>();

    @Column(length = 256)
    public String countryCode;

    @Column(length = 256)
    public String stateCode;

    @Column(length = 256)
    public String cityName;

    public Integer experience;

    public Integer minSalary;

    public Integer maxSalary;

    @ManyToOne
    @JsonIgnore
    public CompanyModel company;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<JobApplyModel> jobApplications = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    public List<CandidateModel> visitors = new ArrayList<>();

    public static Finder<Long, JobModel> find = new Finder<>(Long.class, JobModel.class);

    @JsonProperty
    public Long companyId() {
        if (company != null) {
            return company.id;
        }
        else {
            return null;
        }
    }
}
