package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HourlyHolder extends RecyclerView.ViewHolder {

    TextView dayText;
    TextView hourlyTime;
    ImageView weatherIcon3;
    TextView morningTempText3;
    TextView weatherDiscriptionText2;

    public HourlyHolder(@NonNull View v){
        super(v);
        dayText = v.findViewById(R.id.dayText);
        hourlyTime = v.findViewById(R.id.hourlyTime);
        weatherIcon3 = v.findViewById(R.id.weatherIcon3);
        morningTempText3 = v.findViewById(R.id.morningTempText3);
        weatherDiscriptionText2 = v.findViewById(R.id.weatherDiscriptionText2);
    }
}
