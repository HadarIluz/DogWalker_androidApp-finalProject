package com.example.ex8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//my
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //create new menu of 'settings' for all fragments
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    //addToBackStack -->Save and Display the last screen mode after we exit the application.

    /*commit->You must call commit() last. If you're adding multiple fragments to the same container,
    then the order in which you add them determines the order they appear in the view hierarchy.*/

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSettings:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { //    |
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragContainer, new MySettingsFragment()).addToBackStack("BBB")
                            .commit();
                }
                else{ // ___
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.country_details, new MySettingsFragment()).addToBackStack("BBB")
                            .commit();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}