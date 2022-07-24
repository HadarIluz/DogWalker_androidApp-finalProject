package com.example.finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


public class MainActivity extends AppCompatActivity {

    private static Context context;
    MyBroadcastReceiver broadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

        //create new instance of MyBroadcastReceiver.
        broadCastReceiver = new MyBroadcastReceiver();
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





    //------------------------------ BroadcastReceiver -----------------------
    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(MyNotificationService.FOREGROUND_PROGRESS);
        this.registerReceiver(broadCastReceiver, intentFilter, Manifest.permission.FOREGROUND_SERVICE, null);

        if(!isMyServiceRunning(MyNotificationService.class)){
            Intent intent = new Intent(MainActivity.this, MyNotificationService.class);
            startForegroundService(intent);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadCastReceiver);
        super.onStop();
    }
    //------------------------------------------------------------------------------



    //----------------------------- NotificationService --------------------------
    private boolean isMyServiceRunning(Class<?> serviceClass){ // check if service already running
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for( ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }
    //------------------------------------------------------------------------------
}