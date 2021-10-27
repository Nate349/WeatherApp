package com.example.weatherapp;

public class Weather {
    private String dateTime;
    private String temp;
    private String feelsLike;
    private String humidity;
    private String UVindex;
    private String morningTemp;
    private String daytimeTemp;
    private String sunrise;
    private String sunset;
    private String eveningTemp;
    private String nightTemp;
    private String visability;
    private String winds;
    private String weatherDiscription;
    private String weatherIcon;

    public Weather(String dateTime, String temp, String feelsLike, String humidity, String UVindex, String morningTemp, String daytimeTemp, String sunrise, String sunset, String eveningTemp, String nightTemp, String visability, String winds, String weatherDiscription, String weatherIcon) {
        this.dateTime = dateTime;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.UVindex = UVindex;
        this.morningTemp = morningTemp;
        this.daytimeTemp = daytimeTemp;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.eveningTemp = eveningTemp;
        this.nightTemp = nightTemp;
        this.visability = visability;
        this.winds = winds;
        this.weatherDiscription = weatherDiscription;
        this.weatherIcon = weatherIcon;
    }
}
