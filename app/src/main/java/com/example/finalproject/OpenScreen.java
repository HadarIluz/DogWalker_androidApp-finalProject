package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class OpenScreen extends AppCompatActivity {

/*
Shows the Open screen of the application for 4 seconds and then enters the application by the MainActivity.class.
 */
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