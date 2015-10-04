package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.utils.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CompanyModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256)
    public String name;

    @Column(length = 256)
    public String description;

    @OneToMany(mappedBy="company", cascade = CascadeType.ALL)
    @JsonIgnore
    public Set<EmployerModel> employees = new HashSet<>();

    @OneToMany(mappedBy="company", cascade = CascadeType.ALL)
    @JsonIgnore
    public Set<JobModel> jobs = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public UserModel user;

    public static Finder<Long, CompanyModel> find = new Finder<>(Long.class, CompanyModel.class);

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
