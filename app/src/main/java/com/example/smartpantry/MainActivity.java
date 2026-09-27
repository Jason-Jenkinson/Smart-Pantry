package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnPantryList).setOnClickListener(v -> {
            startActivity(new Intent(this, PantryList.class));
        });

        findViewById(R.id.btnSuggestedRecipes).setOnClickListener(v -> {
            startActivity(new Intent(this, SuggestedRecipes.class));
        });

        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, Settings.class));
        });
    }
}