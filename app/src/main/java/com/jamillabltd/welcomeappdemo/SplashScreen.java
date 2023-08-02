package com.jamillabltd.welcomeappdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    ImageView splashIcon;
    TextView splashName;
    Animation Splash_top, Splash_bottom;

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //settings stored
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        boolean isNotDone = sharedPreferences.getBoolean("welcome_setting", true);
        String language = sharedPreferences.getString("language", "");
        String darkMood = sharedPreferences.getString("dark_mood", "System");

        //dark mood
        switch (darkMood) {
            case "On":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "Off":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "System":
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }//======= end dark mood ==============

        setContentView(R.layout.splash_screen);


        //For Splash Screen
        final Handler handler = new Handler();
        if (!isNotDone) {
            handler.postDelayed(() -> {
                startActivityWithClearStack(MainActivity.class);
            }, 2500);
        } else {
            handler.postDelayed(() -> {
                startActivityWithClearStack(WelcomeSettings.class);
            }, 2000);
        }

        // hide title bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        //animation
        splashIcon = findViewById(R.id.splashIconId);
        splashName = findViewById(R.id.splashNameId);

        Splash_top = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        Splash_bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        splashIcon.setAnimation(Splash_top);
        splashName.setAnimation(Splash_bottom);

    }

    private void startActivityWithClearStack(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}