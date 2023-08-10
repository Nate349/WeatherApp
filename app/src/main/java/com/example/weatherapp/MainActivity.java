package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {
    private TextView locationText;
    private TextView timeText;
    private TextView tempText;
    private TextView feelsLikeText;
    private ImageView weatherIcon;
    private TextView weatherDiscriptionText;
    private TextView windsText;
    private TextView humidityText;
    private TextView UVIText;
    private TextView visibilityText;
    private TextView morningTempText;
    private TextView daytimeTempText;
    private TextView eveningTempText;
    private TextView nightTempText;
    private TextView sunriseText;
    private TextView sunsetText;
    private SwipeRefreshLayout swipeRefresh;
    private TextView time1;
    private TextView time2;
    private TextView time3;
    private TextView time4;
    private RecyclerView recyclerView;
    private String userLocation = "Chicago,IL";
    private boolean fahrenheit = true;
    private SharedPreferences.Editor edit;
    private Menu weatherMenu;
    public Long timeZoneOff;
    public String fc;
    private HourlyAdapter hourAdapter;
    private final ArrayList<Hourly> hours = new ArrayList<>();
    private DailyAdapter dayAdapter;
    private final ArrayList<Daily> days = new ArrayList<>();
    private DailyAct dailyAct;
    private Double lat;
    private Double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        hourAdapter = new HourlyAdapter(hours,this);
        dayAdapter = new DailyAdapter(dailyAct,days);
        recyclerView.setAdapter(hourAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        locationText = findViewById(R.id.locationText);
        timeText = findViewById(R.id.timeText);
        tempText = findViewById(R.id.tempText);
        feelsLikeText = findViewById(R.id.feelsLikeText);
        weatherIcon  = findViewById(R.id.weatherIcon);
        weatherDiscriptionText = findViewById(R.id.weatherDiscriptionText);
        windsText = findViewById(R.id.windsText);
        humidityText = findViewById(R.id.humidityText);
        UVIText = findViewById(R.id.UVIndexText);
        visibilityText = findViewById(R.id.visibilityText);
        morningTempText = findViewById(R.id.morningTempText);
        daytimeTempText = findViewById(R.id.daytimeTempText);
        eveningTempText = findViewById(R.id.eveningTempText);
        nightTempText = findViewById(R.id.nightTempText);
        sunriseText = findViewById(R.id.sunriseText);
        sunsetText = findViewById(R.id.sunsetText);
        swipeRefresh = findViewById(R.id.swipe_container);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        edit = sharedPref.edit();
        if (sharedPref.contains("F")) {
            fahrenheit = sharedPref.getBoolean("F", true);
        }
        else{
            edit.putBoolean("F", true);
            edit.apply();
            fahrenheit = sharedPref.getBoolean("F", true);
        }

        if(sharedPref.contains("lat")){
            this.userLocation = sharedPref.getString("userLocation", "Chicago, IL");
            this.lat = Double.parseDouble(sharedPref.getString("lat","41.8675766"));
            this.lon = Double.parseDouble(sharedPref.getString("lon","-87.616232"));
        }
        swipeRefresh.setOnRefreshListener(this::refresh);
        download();



    }
    public void download(){
        if (!this.netCheck()) {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
            timeText.setText("No internet connection");
        } else {
            lat = getLatLon(userLocation)[0];
            lon = getLatLon(userLocation)[1];
            Runnable info = new WeatherRunnable(this, getLocationName(userLocation),lat,lon, fahrenheit);
            new Thread(info).start();;
        }

    }
    private boolean netCheck() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    private void refresh() {
        if (!this.netCheck()) {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
            timeText.setText("No internet connection");

        } else {
            this.download();
        }
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.weathermenu, menu);

        this.weatherMenu = menu;
        MenuItem units = this.weatherMenu.getItem(0);
        if (!fahrenheit) {
            units.setIcon(R.drawable.units_c);
        } else {
            units.setIcon(R.drawable.units_f);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!this.netCheck()) {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
            timeText.setText("No internet connection");
            return true;
        }
        if(item.getItemId() == R.id.tempType){
            if(!fahrenheit){
                fahrenheit = true;
                edit.putBoolean("F", true);
                item.setIcon(R.drawable.units_f);
                edit.apply();
            }
            else{
                fahrenheit = false;
                edit.putBoolean("F",false);
                item.setIcon(R.drawable.units_c);
                edit.apply();
            }
        }
        if(item.getItemId() == R.id.locationButton){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (getLocationName(et.getText().toString()) == null) {
                        Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        userLocation = getLocationName(et.getText().toString());
                        locationText.setText(userLocation);
                        edit.putString("userLocation", userLocation);
                        edit.putString("lat", String.valueOf(getLatLon(userLocation)[0]));
                        edit.putString("lon", String.valueOf(getLatLon(userLocation)[1]));
                        lat = getLatLon(userLocation)[0];
                        lon = getLatLon(userLocation)[1];
                        edit.apply();
                        download();
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    refresh();
                }
            });
            builder.setMessage("For US locations, enter as 'City', or 'City, State'" + "\nFor international locations, enter as 'City, Country'");
            builder.setTitle("Enter a Location");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if(item.getItemId() == R.id.dailyButton){
            Intent intent = new Intent(this, DailyAct.class);
            intent.putExtra("fc", this.fc);
            intent.putExtra("timezoneOff", String.valueOf(this.timeZoneOff));
            intent.putExtra("location", this.userLocation);
            intent.putExtra("days", this.days);
            startActivity(intent);
        }
            download();


        return true;
    }

    public void updateData(TotalWeather w) {
        String fc;
        String ph;
        String distanceType;
        if(fahrenheit){
            fc = "°F";
            this.fc = fc;
            ph = "mph";
            distanceType = "mi";
        }
        else{
            fc = "°C";
            this.fc = fc;
            ph = "kph";
            distanceType = "km";
        }
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(Long.parseLong(w.currentWeather.dateTime) + Long.parseLong(w.timeZoneOff), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf); // Thu Sep 30 10:06 PM, 2021
        timeText.setText(formattedTimeString);
        tempText.setText(new StringBuilder().append(Math.round(Double.parseDouble(w.currentWeather.temp))).append(fc).toString());
        feelsLikeText.setText(new StringBuilder().append("Feels Like ").append(Math.round(Double.parseDouble(w.currentWeather.feelsLike))).append(fc).toString());
        String iconCode = "_" + w.currentWeather.weatherIcon;
        int iconResId = this.getResources().getIdentifier(iconCode, "drawable", this.getPackageName());
        weatherIcon.setImageResource(iconResId);
        weatherDiscriptionText.setText(new StringBuilder().append(w.currentWeather.weatherDiscription).append("(").append(w.currentWeather.clouds).append("% clouds)").toString());
        windsText.setText(new StringBuilder().append("Winds: ").append(getDirection(Double.parseDouble(w.currentWeather.wind_deg))).append(" at ").append(Math.round(Double.parseDouble(w.currentWeather.wind_speed))).append(ph).toString());
        humidityText.setText(new StringBuilder().append("Humidity: ").append(w.currentWeather.humidity).append("%").toString());
        UVIText.setText(new StringBuilder().append("UV Index: ").append(Math.round(Double.parseDouble(w.currentWeather.UVindex))).toString());
        visibilityText.setText(new StringBuilder().append("Visibility: ").append(visiblity(w.currentWeather.visibility)).append(distanceType).toString());
        morningTempText.setText(new StringBuilder().append(Math.round(Double.parseDouble(w.daily[0].morn))).append(fc).toString());
        daytimeTempText.setText(new StringBuilder().append(Math.round(Double.parseDouble(w.daily[0].day))).append(fc).toString());
        eveningTempText.setText(new StringBuilder().append(Math.round(Double.parseDouble(w.daily[0].eve))).append(fc).toString());
        nightTempText.setText(new StringBuilder().append(Math.round(Double.parseDouble(w.daily[0].night))).append(fc).toString());
        LocalDateTime ldt2 = LocalDateTime.ofEpochSecond(Long.parseLong(w.currentWeather.sunrise) + Long.parseLong(w.timeZoneOff), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String sunr = ldt2.format(dtf2);
        timeZoneOff = Long.parseLong(w.timeZoneOff);
        sunriseText.setText(new StringBuilder().append("Sunrise: ").append(sunr).toString());
        LocalDateTime ldt3 = LocalDateTime.ofEpochSecond(Long.parseLong(w.currentWeather.sunset) + Long.parseLong(w.timeZoneOff), 0, ZoneOffset.UTC);
        String suns = ldt3.format(dtf2);
        sunsetText.setText(new StringBuilder().append("Sunset: ").append(suns).toString());
        time1.setText("8am");
        time2.setText("1pm");
        time3.setText("5pm");
        time4.setText("11pm");
        locationText.setText(getLocationName(userLocation));
        hours.clear();
        hours.addAll(Arrays.asList(w.hourly));
        hourAdapter.notifyDataSetChanged();
        days.clear();
        days.addAll(Arrays.asList(w.daily));
        dayAdapter.notifyDataSetChanged();


    }
    private String getLocationName(String userProvidedLocation) {

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);

            if (address == null || address.isEmpty()) {
                return null;
            }
            String country = address.get(0).getCountryCode();
            if (country == null) {
                return null;
            }

            Address addressItem = address.get(0);

            String p1;
            String p2;
            if (country.equals("US")) {
                p1 = addressItem.getFeatureName();
                p2 = addressItem.getAdminArea();
            } else {
                p1 = addressItem.getLocality();
                if (p1 == null)
                    p1 = addressItem.getFeatureName();
                p2 = addressItem.getCountryName();
            }

            if (p1 == null || p1.isEmpty()) return null;
            if (p2 == null || p2.isEmpty()) return null;

            userLocation = p1 + ", " + p2;
            return userLocation;
        } catch (IOException e) {
            return null;
        }
    }
    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            double lat = address.get(0).getLatitude();
            double lon = address.get(0).getLongitude();

            return new double[] {lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }
    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }
    public String visiblity(String meters){
        double vis;
        if(fahrenheit){
            vis = Math.round((Double.parseDouble(meters) / 1609.344)*10.0)/10.0;
        }
        else{
            vis = Math.round((Double.parseDouble(meters) / 1000)*10.0)/10.0;
        }
        return String.valueOf(vis);
    }

    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        Hourly hourly = hours.get(position);
        long begin = Long.parseLong(hourly.dt) * 1000;
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, begin);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        startActivity(intent);
    }
}
