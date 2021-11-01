package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyHolder> {
    private final ArrayList<Hourly> hourlyList;
    private final MainActivity main;

    HourlyAdapter(ArrayList<Hourly> hourlyList, MainActivity main){
        this.hourlyList = hourlyList;
        this.main = main;
    }


    @NonNull
    @Override
    public HourlyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View hourView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailyrecycle,parent,false);
        return new HourlyHolder(hourView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyHolder holder, int position) {
        Hourly hour = hourlyList.get(position);
        holder.weatherDiscriptionText2.setText(hour.description);
        String iconCode = "_" + hour.icon;
        int iconResId = main.getResources().getIdentifier(iconCode, "drawable", main.getPackageName());
        holder.weatherIcon3.setImageResource(iconResId);
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(Long.parseLong(hour.dt) + main.timeZoneOff, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        LocalDateTime currentldt = LocalDateTime.ofEpochSecond(Long.parseLong(hourlyList.get(0).dt) + main.timeZoneOff, 0, ZoneOffset.UTC);
        DateTimeFormatter ttf = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String fDay = ldt.format(dtf);
        String ftime = ldt.format(ttf);
        String currentDay = currentldt.format(dtf);

        if (fDay.equals(currentDay)){
            fDay = "Today";
        }
        holder.dayText.setText(fDay);
        holder.hourlyTime.setText(ftime);
        holder.morningTempText3.setText(new StringBuilder().append((int) Math.round(Double.parseDouble(hour.temperature))).append(main.fc).toString());
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }
}
