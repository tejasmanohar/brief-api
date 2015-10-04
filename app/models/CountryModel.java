package models;

import models.utils.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class CountryModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 10, unique = true, nullable = false)
    public String countryCode;

    @Column(length = 256, unique = true, nullable = false)
    public String countryName;

    public static Finder<Long, CountryModel> find = new Finder<>(Long.class, CountryModel.class);

    public CountryModel() {}

    public CountryModel(String countryCode, String countryName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public static List<CountryModel> all() {
        return CountryModel.find.all();
    }

    public static CountryModel findByCode(String countryCode) {
        return find.where().eq("countryCode", countryCode).findUnique();
    }
}
