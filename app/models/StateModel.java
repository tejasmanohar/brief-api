package models;

import models.utils.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class StateModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 10, unique = true, nullable = false)
    public String code;

    @Column(length = 256, unique = true, nullable = false)
    public String name;

    private static Model.Finder<Long, StateModel> find = new Model.Finder<>(Long.class, StateModel.class);

    public StateModel() {
    }

    public StateModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<StateModel> all() {
        return StateModel.find.all();
    }

    public static StateModel findByCode(String code) {
        return find.where().eq("code", code).findUnique();
    }
}
