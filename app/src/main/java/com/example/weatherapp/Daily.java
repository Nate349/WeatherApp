package com.example.weatherapp;

import java.io.Serializable;

public class Daily implements Serializable {
    public String dt;
    public String day;
    public String min;
    public String max;
    public String night;
    public String eve;
    public String morn;
    public String id;
    public String description;
    public String icon;
    public String pop;
    public String uvi;

    Daily(String dt, String day, String min, String max, String night, String eve, String morn, String id, String description, String icon, String pop, String uvi){
        this.dt = dt;
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.pop = pop;
        this.uvi = uvi;
    }
}
