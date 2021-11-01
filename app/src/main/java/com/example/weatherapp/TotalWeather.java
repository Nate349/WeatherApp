package com.example.weatherapp;

import java.io.Serializable;

public class TotalWeather implements Serializable {
    public String latitude;
    public String longitude;
    public String timeZone;
    public String timeZoneOff;
    public Weather currentWeather;
    public Hourly[] hourly;
    public Daily[] daily;

    public TotalWeather(String latitude,String longitude, String timeZone, String timeZoneOff,Weather currentWeather,Hourly[] hourly, Daily[] daily) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeZone = timeZone;
        this.timeZoneOff = timeZoneOff;
        this.currentWeather = currentWeather;
        this.hourly = hourly;
        this.daily = daily;
    }
}
