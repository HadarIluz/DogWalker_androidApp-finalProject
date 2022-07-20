package com.example.ex8;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class OpenScreen extends AppCompatActivity {

    //Open the 'OpenScreen' application for 4 sec and go to mainActivity by using new Intent
    //we add the "OpenScreen" in the manifest.xml as MIAN
    //and add the "MainActivity" as   <category android:name="android.intent.category.DEFAULT" />
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_screen_app);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(OpenScreen.this ,MainActivity.class ));
            }
        }, 1000);
    }

}
