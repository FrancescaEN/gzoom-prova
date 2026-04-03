package it.mapsgroup.gzoom.model;

import java.math.BigDecimal;

/**
 * @author Andrea Fossi.
 */
public class WeatherData {
    private Long id;
    private Identifiable weatherCondition;
    private BigDecimal minTemperature;
    private BigDecimal maxTemperature;
    private Identifiable windType;
    private BigDecimal windSpeed;
    private BigDecimal rainfall;
    private Identifiable activitySuspension;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Identifiable getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(Identifiable weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public BigDecimal getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(BigDecimal maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public BigDecimal getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(BigDecimal minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Identifiable getWindType() {
        return windType;
    }

    public void setWindType(Identifiable windType) {
        this.windType = windType;
    }

    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(BigDecimal windSpeed) {
        this.windSpeed = windSpeed;
    }

    public BigDecimal getRainfall() {
        return rainfall;
    }

    public void setRainfall(BigDecimal rainfall) {
        this.rainfall = rainfall;
    }

    public Identifiable getActivitySuspension() {
        return activitySuspension;
    }

    public void setActivitySuspension(Identifiable activitySuspension) {
        this.activitySuspension = activitySuspension;
    }
}
