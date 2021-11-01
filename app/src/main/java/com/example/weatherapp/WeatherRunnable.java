package com.example.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class WeatherRunnable implements Runnable{

    private static final String TAG = "WeatherDownloadRunnable";

    private final MainActivity main;
    private final String city;
    private final double latitude;
    private final double longitude;
    private final boolean fahrenheit;
    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall";
    private static final String yourAPIKey = "3a674edc22beb8182cd6cdb3ee5e2631";


    WeatherRunnable(MainActivity main, String city,double latitude,double longitude, boolean fahrenheit) {
        this.main = main;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fahrenheit = fahrenheit;
    }



    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("lat", String.format(Locale.getDefault(),"%f", latitude));
        buildURL.appendQueryParameter("lon", String.format(Locale.getDefault(),"%f", longitude));
        buildURL.appendQueryParameter("appid", yourAPIKey);
        if(fahrenheit){
            buildURL.appendQueryParameter("units", "imperial");
        }
        else{
            buildURL.appendQueryParameter("units", "metric");
        }
        buildURL.appendQueryParameter("lang", "en");
        buildURL.appendQueryParameter("exclude", "minutely");
        buildURL.appendQueryParameter("appid", yourAPIKey);
        String urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    public void handleResults(final String jsonString) {

        final TotalWeather w = parseJSON(jsonString);
        main.runOnUiThread(() -> main.updateData(w));
    }

    private TotalWeather parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);
                String latitude = jObjMain.getString("lat");
                String longitude =jObjMain.getString("lon");
                String timeZone = jObjMain.getString("timezone");
                String timeZoneOff = jObjMain.getString("timezone_offset");

                //current

                JSONObject current = jObjMain.getJSONObject("current");
                JSONArray weatherJsonArray = current.getJSONArray("weather");
                JSONObject weather = (JSONObject) weatherJsonArray.get(0);
                String[] weatherArray = weatherParser(weather);
                String id = weatherArray[0];
                String main = weatherArray[1];
                String description = weatherArray[2];
                String icon = weatherArray[3];
                String dt = current.getString("dt");
                String sunrise = current.getString("sunrise");
                String sunset = current.getString("sunset");
                String temp = current.getString("temp");
                String feelsLike = current.getString("feels_like");
                String pressure = current.getString("pressure");
                String humidity = current.getString("humidity");
                String uvi = current.getString("uvi");
                String clouds = current.getString("clouds");
                String visibility = current.getString("visibility");
                String windSpeed = current.getString("wind_speed");
                String windDeg = current.getString("wind_deg");
                String windGust;
                if(current.has("wind_gust")) {
                    windGust = current.getString("wind_gust");
                }
                else{
                    windGust = "";
                }
                Weather currentWeather = new Weather(dt,temp,feelsLike,humidity,uvi,sunrise,sunset,visibility,id,main,description,icon,pressure,clouds,windSpeed,windDeg,windGust);
                JSONArray hourlyArray = jObjMain.getJSONArray("hourly");
                Hourly[] hourly = new Hourly[hourlyArray.length()];
                for (int i = 0; i < hourlyArray.length(); i++) {
                    Hourly hour = hourlyParser((JSONObject) hourlyArray.get(i));
                    hourly[i] = hour;
                }
                JSONArray dailyArray = jObjMain.getJSONArray("daily");
                Daily[] daily = new Daily[dailyArray.length()];
                for (int i = 0; i < dailyArray.length(); i++) {
                Daily day = dailyParser((JSONObject) dailyArray.get(i));
                daily[i] = day;
                }

                TotalWeather totalWeather = new TotalWeather(latitude,longitude,timeZone,timeZoneOff,currentWeather,hourly,daily);
                return totalWeather;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public String[] weatherParser(JSONObject weather){
        try {
            String id = weather.getString("id");
            String main = weather.getString("main");
            String description = weather.getString("description");
            String icon = weather.getString("icon");
            String[] weatherArray = {id,main,description,icon};
            return weatherArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    public Hourly hourlyParser(JSONObject hourly){
        try {
            String dt = hourly.getString("dt");
            String temp = hourly.getString("temp");
            String pop = hourly.getString("pop");
            JSONArray weatherJsonArray = hourly.getJSONArray("weather");
            JSONObject weather = (JSONObject) weatherJsonArray.get(0);
            String[] weatherArray = weatherParser(weather);
            return new Hourly(dt,temp,weatherArray[2],weatherArray[3],pop);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return new Hourly("","","","","");
    }
    public Daily dailyParser(JSONObject daily){
        try {
            String dt = daily.getString("dt");
            JSONObject tempJsonObject = daily.getJSONObject("temp");
            String day = tempJsonObject.getString("day");
            String min = tempJsonObject.getString("min");
            String max = tempJsonObject.getString("max");
            String night = tempJsonObject.getString("night");
            String eve = tempJsonObject.getString("eve");
            String morn = tempJsonObject.getString("morn");
            JSONArray weatherJsonArray = daily.getJSONArray("weather");
            JSONObject weather = (JSONObject) weatherJsonArray.get(0);
            String[] weatherArray = weatherParser(weather);
            String pop = daily.getString("pop");
            String uvi = daily.getString("uvi");
            return new Daily( dt, day, min, max, night, eve, morn, weatherArray[0], weatherArray[2], weatherArray[3], pop, uvi);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return new Daily("","","","","","","","","","","","");
    }
}


