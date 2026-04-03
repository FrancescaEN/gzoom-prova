package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Fossi.
 */
public class WeatherDataCatalog {
    private List<Identifiable> weatherConditions;
    private List<Identifiable> windTypes;
    private List<Identifiable> activitySuspensions;

    public WeatherDataCatalog() {
        this.weatherConditions = new ArrayList<>();
        this.windTypes = new ArrayList<>();
        this.activitySuspensions = new ArrayList<>();
    }

    public List<Identifiable> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<Identifiable> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public List<Identifiable> getWindTypes() {
        return windTypes;
    }

    public void setWindTypes(List<Identifiable> windTypes) {
        this.windTypes = windTypes;
    }

    public List<Identifiable> getActivitySuspensions() {
        return activitySuspensions;
    }

    public void setActivitySuspensions(List<Identifiable> activitySuspensions) {
        this.activitySuspensions = activitySuspensions;
    }
}
