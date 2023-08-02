package com.jamillabltd.welcomeappdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class WelcomeSettings extends AppCompatActivity {
    LinearLayout layoutInflateArea;
    LayoutInflater layoutInflater;
    View view;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String instantLanguageValue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_settings);

        // hide title bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //settings store
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Button skip = findViewById(R.id.skipId);
        skip.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeSettings.this, MainActivity.class));
            this.finish();

            editor.putBoolean("welcome_setting", false);
            editor.putString("language", "English");
            editor.putString("dark_mood", "System");
            editor.apply();

        });

        //layout inflate
        layoutInflateArea = findViewById(R.id.layoutInflateAreaId);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.language_settings, layoutInflateArea);

        Button languageNext = view.findViewById(R.id.languageNextId);

        //TODO: language xml access
        //get selected language
        RadioGroup languageRadioGroup = view.findViewById(R.id.languageRadioGroupId);

        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton languageInstant = findViewById(checkedId);
            instantLanguageValue = languageInstant.getText().toString();

            if (!instantLanguageValue.equals("English")) {
                skip.setText("এড়িয়ে যান");
                languageNext.setText("পরবর্তী");
                setLocale("bn"); // Change to Bengali language
            } else {
                skip.setText("Skip");
                languageNext.setText("Next");
                setLocale("en"); // Change to English language
            }
            editor.putString("language", instantLanguageValue);
            editor.apply();

        });
        //===========end language ===============

        languageNext.setOnClickListener(v -> {
            editor.putString("language", instantLanguageValue);
            editor.apply();

            //remove language layout inflate and set dark mood layout
            layoutInflateArea.removeAllViews();

            //TODO: dark xml access
            view = layoutInflater.inflate(R.layout.dark_mood_setting, layoutInflateArea);
            Button darkNext = view.findViewById(R.id.darkMoodNextId);
            darkNext.setOnClickListener(v1 -> {
                RadioGroup darkMoodRadioGroup = view.findViewById(R.id.darkMoodRadioGroupId);
                int selectedDarkMood = darkMoodRadioGroup.getCheckedRadioButtonId();
                RadioButton darkMood = view.findViewById(selectedDarkMood);
                String darkMoodValue = darkMood.getText().toString();

                editor.putString("dark_mood", darkMoodValue);
                editor.apply();

                //remove darkMood layout inflate and set services in one place layout
                layoutInflateArea.removeAllViews();
                skip.setVisibility(View.GONE);

                //TODO: all services in one place xml access
                view = layoutInflater.inflate(R.layout.all_services_in_one_place, layoutInflateArea);


                Button allServices = view.findViewById(R.id.allServiceNextId);
                allServices.setOnClickListener(v2 -> {
                    editor.putBoolean("welcome_setting", false);
                    editor.apply();

                    startActivity(new Intent(WelcomeSettings.this, MainActivity.class));
                    this.finish();
                });


            });

        });


        String languageSP = sharedPreferences.getString("language", "");
        RadioButton englishRadioButton = findViewById(R.id.englishLanguageId);
        RadioButton bengaliRadioButton = findViewById(R.id.bengaliLanguageId);
        // Set the default language selection
        if (languageSP.equals("")) {
            englishRadioButton.setChecked(true);
        } else if (languageSP.equals("English")) {
            englishRadioButton.setChecked(true);
        } else {
            bengaliRadioButton.setChecked(true);
        }


    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        resources.updateConfiguration(configuration, displayMetrics);

        // Save the selected language in the SharedPreferences for future use
        editor.putString("language", langCode);
        editor.apply();
    }


}