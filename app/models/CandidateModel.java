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
public class CandidateModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256)
    public String firstName;

    @Column(length = 256)
    public String lastName;

    @Column(length = 256)
    public String summary;

    @Column(length = 256)
    public String countryCode;

    @Column(length = 256)
    public String stateCode;

    @Column(length = 256)
    public String cityName;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    public List<ExperienceModel> experience = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    public List<EducationModel> education = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    public List<JobApplyModel> jobApplications = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    public Set<SkillModel> skills = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public UserModel user;

    public CandidateModel() {}

    public CandidateModel(String firstName, String lastName, String summary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
    }

    @JsonProperty
    public Long userId() {
        if (user != null) {
            return user.id;
        }
        else {
            return null;
        }
    }
}
