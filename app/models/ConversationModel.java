package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.utils.Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ConversationModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256, nullable = false)
    public String title;

    @OneToOne
    @JsonIgnore
    public JobApplyModel jobApplication;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    public Set<UserModel> members = new HashSet<>();

    public static Finder<Long, ConversationModel> find = new Finder<>(Long.class, ConversationModel.class);

    @JsonProperty
    public Long jobApplicationId() {
        if (jobApplication != null) {
            return jobApplication.id;
        }
        else {
            return null;
        }
    }
}
