package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.utils.Model;

import javax.persistence.*;

@Entity
public class EmployerModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256)
    public String firstName;

    @Column(length = 256)
    public String lastName;

    @ManyToOne
    @JsonIgnore
    public CompanyModel company;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public UserModel user;

    public static Finder<Long, EmployerModel> find = new Finder<>(Long.class, EmployerModel.class);

    @JsonProperty
    public Long companyId() {
        if (company != null) {
            return company.id;
        }
        else {
            return null;
        }
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
