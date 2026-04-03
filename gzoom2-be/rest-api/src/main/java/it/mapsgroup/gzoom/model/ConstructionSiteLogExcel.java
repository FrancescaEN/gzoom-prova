package it.mapsgroup.gzoom.model;

/**
 * @author Andrea Fossi.
 */
public class ConstructionSiteLogExcel extends ConstructionSiteLog {
    private WeatherData weatherData;
    private CslActivity activity;

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public CslActivity getActivity() {
        return activity;
    }

    public void setActivity(CslActivity activity) {
        this.activity = activity;
    }
}
