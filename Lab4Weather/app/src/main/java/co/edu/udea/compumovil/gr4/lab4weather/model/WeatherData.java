package co.edu.udea.compumovil.gr4.lab4weather.model;

/**
 * Created by MattNP on 26/04/2016.
 */
public class WeatherData {
    private String id;
    private String name;
    private Coordinate coord;
    private SysClass sys;
    private MainClass main;
    private Wind wind;
    private Weather[] weather;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public SysClass getSys() {
        return sys;
    }

    public void setSys(SysClass sys) {
        this.sys = sys;
    }

    public MainClass getMain() {
        return main;
    }

    public void setMain(MainClass main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }
}
