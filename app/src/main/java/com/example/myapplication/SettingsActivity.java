package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends BaseActivity {

    private Spinner themeSpinner;
    private SharedPreferences sharedPreferences;
    private boolean userInitiatedSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        themeSpinner = findViewById(R.id.themeSpinner);
        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(this,
                R.array.theme_options, android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);

        // Get saved theme or default to "System Default"
        String currentTheme = sharedPreferences.getString("theme", "System Default");
        themeSpinner.setSelection(getThemePosition(currentTheme));

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (!userInitiatedSelection) {
                    userInitiatedSelection = true;
                    return;
                }

                String selectedTheme = parent.getItemAtPosition(position).toString();
                applyTheme(selectedTheme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


// code to change dark theme or light theme
    private void applyTheme(String selectedTheme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (selectedTheme) {
            case "System Default":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                editor.putString("theme", "System Default");
                break;
            case "Light Theme":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putString("theme", "Light Theme");
                break;
            case "Dark Theme":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putString("theme", "Dark Theme");
                break;
        }

        editor.apply();

    }

    private int getThemePosition(String theme) {
        switch (theme) {
            case "Light Theme":
                return 1;
            case "Dark Theme":
                return 2;
            default:
                return 0; // System Default
        }
    }



    private void restartMainActivity() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
