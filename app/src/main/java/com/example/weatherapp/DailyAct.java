package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DailyAct extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DailyAdapter dailyAdapter;
    private ArrayList<Daily> dailyList;
    private String location;

    public String fc;
    public String timeZoneOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailyact);
        Intent intent = getIntent();
        if (intent.hasExtra("fc")) {
            this.fc = intent.getStringExtra("fc");
        }
        if (intent.hasExtra("days")) {
            this.dailyList = (ArrayList<Daily>) intent.getSerializableExtra("days");
        }
        if (intent.hasExtra("location")) {
            this.location = intent.getStringExtra("location");
        }
        if (intent.hasExtra("timezoneOff")) {
            this.timeZoneOff = intent.getStringExtra("timezoneOff");
        }
        this.setTitle(location);
        recyclerView = findViewById(R.id.dayRecycle);
        dailyAdapter = new DailyAdapter(this, dailyList);
        recyclerView.setAdapter(dailyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

