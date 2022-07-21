package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class OpenScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_screen_app);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(OpenScreen.this, MainActivity.class));
            }
        }, 4000);
    }
}