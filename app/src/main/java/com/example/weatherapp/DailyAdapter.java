package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class DailyAdapter extends RecyclerView.Adapter<DailyHolder> {
    private final DailyAct dailyAct;
    private final ArrayList<Daily> dailyList;

    DailyAdapter(DailyAct dailyAct, ArrayList<Daily> dailyList) {
        this.dailyAct = dailyAct;
        this.dailyList = dailyList;
    }
    @NonNull
    @Override
    public DailyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View daily = LayoutInflater.from(parent.getContext()).inflate(R.layout.sevendayrecycle, parent, false);

        return new DailyHolder(daily);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyHolder holder, int position) {
        Daily day = dailyList.get(position);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, M/dd", Locale.getDefault());
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(Long.parseLong(day.dt) + Long.parseLong(dailyAct.timeZoneOff), 0, ZoneOffset.UTC);
        String fDate = ldt.format(dtf);
        holder.timeText2.setText(new StringBuilder().append(" ").append(fDate).toString());
        holder.tempText2.setText(new StringBuilder().append((int) Math.round(Double.parseDouble(day.max))).append(dailyAct.fc).append("/").append((int) Math.round(Double.parseDouble(day.min))).append(dailyAct.fc).toString());
        holder.weatherDiscriptionText3.setText(day.description);
        holder.probPrecipitation.setText(new StringBuilder().append("(").append((int) Math.round(Double.parseDouble(day.pop)*100)).append("% ").append("precip.").append(")").toString());
        holder.UVIndex3.setText(new StringBuilder().append("UV Index: ").append(Math.round(Double.parseDouble(day.uvi))).toString());
        holder.morningTempText2.setText(new StringBuilder().append(Math.round(Double.parseDouble(day.morn))).append(dailyAct.fc).toString());
        holder.daytimeTempText2.setText(new StringBuilder().append(Math.round(Double.parseDouble(day.day))).append(dailyAct.fc).toString());
        holder.eveningTempText2.setText(new StringBuilder().append(Math.round(Double.parseDouble(day.eve))).append(dailyAct.fc).toString());
        holder.nightTempText2.setText(new StringBuilder().append(Math.round(Double.parseDouble(day.night))).append(dailyAct.fc).toString());
        String iconCode = "_" + day.icon;
        int iconResId = dailyAct.getResources().getIdentifier(iconCode, "drawable",  dailyAct.getPackageName());
        holder.weatherIcon2.setImageResource(iconResId);
        holder.time5.setText("8am");
        holder.time6.setText("1pm");
        holder.time7.setText("5pm");
        holder.time8.setText("11pm");


    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }
}
