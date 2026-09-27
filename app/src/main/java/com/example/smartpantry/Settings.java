package com.example.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

        SharedPreferences preferences = getSharedPreferences("smart_pantry_settings", MODE_PRIVATE);
        CheckBox expiringSoonCheckBox = findViewById(R.id.checkExpiringSoon);
        CheckBox metricUnitsCheckBox = findViewById(R.id.checkMetricUnits);

        expiringSoonCheckBox.setChecked(preferences.getBoolean("expiry_alerts_enabled", false));
        metricUnitsCheckBox.setChecked(preferences.getBoolean("metric_units_enabled", false));

        expiringSoonCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
            preferences.edit().putBoolean("expiry_alerts_enabled", isChecked).apply());
        metricUnitsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
            preferences.edit().putBoolean("metric_units_enabled", isChecked).apply());
    }
}
