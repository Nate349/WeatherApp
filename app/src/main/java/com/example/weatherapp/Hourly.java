package com.example.weatherapp;


import java.io.Serializable;

public class Hourly implements Serializable {
    public String dt;
    public String temperature;
    public String description;
    public String icon;
    public String pop;

    Hourly(String dt, String temperature, String description, String icon, String pop){
        this.dt = dt;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
        this.pop = pop;
    }
}
