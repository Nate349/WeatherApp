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
    private final boolean fahrenheit;
    //TODO get the urls
    private static final String weatherURL = "";
    private static final String iconUrl = "";
    // Sign up to get your API Key at:  https://home.openweathermap.org/users/sign_up
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";


    WeatherRunnable(MainActivity main, String city, boolean fahrenheit) {
        this.main = main;
        this.city = city;
        this.fahrenheit = fahrenheit;
    }



    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("q", city);
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

        final Weather w = parseJSON(jsonString);
        main.runOnUiThread(() -> main.updateData(w));
    }

    private Weather parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);
                String timezone = jObjMain.getString("timezone");
                String timezone_offset = jObjMain.getString("timezone_offset");

                //current
                JSONObject current = jObjMain.getJSONObject("current");
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
                JSONArray weatherJsonArray = jObjMain.getJSONArray("weather");
                JSONObject weather = (JSONObject) weatherJsonArray.get(0);
                String[] weatherArray = weatherParser(weather);
                String id = weatherArray[0];
                String main = weatherArray[1];
                String description = weatherArray[2];
                String icon = weatherArray[3];
                JSONArray hourlyArray = jObjMain.getJSONArray("hourly");
                Hourly[] hourly = new Hourly[hourlyArray.length()];
                for (int i = 0; i < hourlyArray.length(); i++) {
                    Hourly hour = hourlyParser((JSONObject) hourlyArray.get(i));
                    hourly[i] = hour;
                }


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
    }
}

//return new Weather(dateTime, temp, feelsLike, humidity, UVindex, morningTemp, daytimeTemp, sunrise, sunset, eveningTemp, nightTemp, visability, winds, weatherDiscription, weatherIcon);

