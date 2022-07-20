package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//my
public class MainActivity extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
    }

    /*create an settings menu for all fragments to use.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //in this function we gets the items which has been pressed and handle the click event, it can be pressed from any fragment in the application.
    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addDog:
                FragmentManager fm1 = getSupportFragmentManager();
                //put this string in the add dog dialog after we click on the dog menu.
                AddNewDogDialog alertDialog = AddNewDogDialog.newInstance("Add Dog");
                alertDialog.show(fm1, "fragment_alert");
                break;
            case R.id.exitApp:
                //TODO: BUG: when press on exit the application start again!!!
                FragmentManager fm2 = getSupportFragmentManager();
                //put this string on the window when we click on EXIT button in the menu.
                ExitDialog alertExitDialog = ExitDialog.newInstance("Closing DogWalker application");
                alertExitDialog.show(fm2, "fragment_alert");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }


}