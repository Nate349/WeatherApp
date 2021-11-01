package com.example.weatherapp;

import java.io.Serializable;

public class Weather implements Serializable {
    public String dateTime;
    public String temp;
    public String feelsLike;
    public String humidity;
    public String UVindex;
    public String sunrise;
    public String sunset;
    public String visibility;
    public String weatherId;
    public String weatherMain;
    public String weatherDiscription;
    public String weatherIcon;
    public String pressure;
    public String clouds;
    public String wind_speed;
    public String wind_deg;
    public String wind_gust;



    public Weather(String dateTime, String temp, String feelsLike, String humidity, String UVindex, String sunrise, String sunset, String visibility,String weatherId,String weatherMain, String weatherDiscription, String weatherIcon,String pressure,String clouds,String wind_speed,String wind_deg,String wind_gust) {
        this.dateTime = dateTime;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.UVindex = UVindex;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.visibility = visibility;
        this.weatherId = weatherId;
        this.weatherMain = weatherMain;
        this.weatherDiscription = weatherDiscription;
        this.weatherIcon = weatherIcon;
        this.pressure = pressure;
        this.clouds = clouds;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.wind_gust = wind_gust;
    }
}
