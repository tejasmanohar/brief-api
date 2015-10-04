package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.utils.Model;

import javax.persistence.*;

@Entity
public class SkillModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long id;

    @Column(length = 256, unique = true, nullable = false)
    public String name;

    public static Finder<Long, SkillModel> find = new Finder<>(Long.class, SkillModel.class);

    public static SkillModel findByName(String name) {
        return find.where().eq("name", name.replaceAll("\\s+","").toLowerCase()).findUnique();
    }

    public SkillModel() {
    }

    public SkillModel(String name) {
        this.name = name.replaceAll("\\s+","").toLowerCase();
    }

    @Override
    public void save() {
        name = name.replaceAll("\\s+","").toLowerCase();
        super.save();
    }
}
