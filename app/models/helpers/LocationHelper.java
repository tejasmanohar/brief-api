package models.helpers;

import play.data.validation.Constraints;

public class LocationHelper {
    @Constraints.Required
    public String countryCode;

    public String stateCode;

    @Constraints.Required
    public String cityName;

    public LocationHelper () {}

    public LocationHelper(String countryCode, String stateCode, String cityName) {
        this.countryCode = countryCode;
        this.stateCode = stateCode;
        this.cityName = cityName;
    }
}
