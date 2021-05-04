package ru.startandroid.develop.earthquakes;

public class Earthquake {

    private double mag;
    private String placeKm;
    private String placePoint;
    private long time;
    String url;

    public Earthquake() {
    }

    public Earthquake(double mag, String placeKm, String placePoint, long time, String url) {
        this.mag = mag;
        this.placeKm = placeKm;
        this.placePoint = placePoint;
        this.time = time;
        this.url = url;
    }

    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public String getPlaceKm() {
        return placeKm;
    }

    public void setPlaceKm(String placeKm) {
        this.placeKm = placeKm;
    }

    public String getPlacePoint() {
        return placePoint;
    }

    public void setPlacePoint(String placePoint) {
        this.placePoint = placePoint;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
