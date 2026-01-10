package com.example.pt6_jose_buales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Click en MLB
        findViewById(R.id.imageButton).setOnClickListener(v -> openTeams("mlb"));

        // Click en MLS
        findViewById(R.id.imageButton2).setOnClickListener(v -> openTeams("mls"));

        // Click en NBA
        findViewById(R.id.imageButton3).setOnClickListener(v -> openTeams("nba"));

        // Click en NFL
        findViewById(R.id.imageButton4).setOnClickListener(v -> openTeams("nfl"));

        // Click en NHL
        findViewById(R.id.imageButton5).setOnClickListener(v -> openTeams("nhl"));
    }

    // Abrir TeamsActivity pasando la liga
    private void openTeams(String lliga) {
        Intent intent = new Intent(this, TeamsActivity.class);
        intent.putExtra("LLIGA", lliga);
        startActivity(intent);
    }
}
