package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DailyHolder extends RecyclerView.ViewHolder {
    TextView timeText2;
    TextView tempText2;
    TextView weatherDiscriptionText3;
    TextView probPrecipitation;
    TextView UVIndex3;
    TextView morningTempText2;
    TextView daytimeTempText2;
    TextView eveningTempText2;
    TextView nightTempText2;
    ImageView weatherIcon2;
    TextView time5;
    TextView time6;
    TextView time7;
    TextView time8;

    public DailyHolder (@NonNull View v){
        super(v);
        timeText2 = v.findViewById(R.id.timeText2);
        tempText2 = v.findViewById(R.id.tempText2);
        weatherDiscriptionText3 = v.findViewById(R.id.weatherDiscriptionText3);
        probPrecipitation = v.findViewById(R.id.probPrecipitation);
        UVIndex3 = v.findViewById(R.id.UVIndexText3);
        morningTempText2 = v.findViewById(R.id.morningTempText2);
        daytimeTempText2 = v.findViewById(R.id.daytimeTempText2);
        eveningTempText2 = v.findViewById(R.id.eveningTempText2);
        nightTempText2 = v.findViewById(R.id.nightTempText2);
        weatherIcon2 = v.findViewById(R.id.weatherIcon2);
        time5 = v.findViewById(R.id.time5);
        time6 = v.findViewById(R.id.time6);
        time7 = v.findViewById(R.id.time7);
        time8 = v.findViewById(R.id.time8);
    }
}
