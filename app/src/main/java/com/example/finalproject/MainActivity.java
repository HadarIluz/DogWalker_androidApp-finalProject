package com.example.finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    MyBroadcastReceiver broadCastReceiver;

    //those are the REQUEST_CODE FOR THE RECEIVE SMS and READ_SMS
    private static final int RECEIVE_SMS_REQUEST_CODE   = 1;
    private static final int READ_SMS_REQUEST_CODE      = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

        askForSmsDangerousPermissions();

        //create new instance of MyBroadcastReceiver.
        broadCastReceiver = new MyBroadcastReceiver();
    }

    //@@@@@@@@@@@@@@@@@@@@@___SMSReceiver___@@@@@@@@@@@@@@@@@@@@@
        //we need to ask for permission because we use dangerous permission.
        private void askForSmsDangerousPermissions() {
            requestSmsDangerousPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_REQUEST_CODE);
            requestSmsDangerousPermission(Manifest.permission.READ_SMS, READ_SMS_REQUEST_CODE);
        }

        //we choose to use the new android flow in order to handle request.
        //this function will request permission for user respond.
        //permission= READ_SMS.
        //permissionRequestCode = READ_SMS_REQUEST_CODE.
        private void requestSmsDangerousPermission(String permission, int permissionRequestCode)
        {
            // check if permission already User approved
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
                return;

            // Permission is not granted. show an explanation.
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                Toast.makeText(this, "You must grant this permission in order to see SMS messages", Toast.LENGTH_LONG).show();

            // request the permission
            ActivityCompat.requestPermissions(this, new String[] { permission }, permissionRequestCode);
        }


        //this method is called when a respond from the user has been made.
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch (requestCode) {
                case RECEIVE_SMS_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "RECEIVE_SMS permission granted: ", Toast.LENGTH_SHORT).show();
                    }
                    /*The feature of receive sms is unavailable because the feature requires a permission that that user has denied. */
                    else {
                        Toast.makeText(this, "RECEIVE_SMS permission isn't granted: ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case READ_SMS_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "READ_SMS permission granted: ", Toast.LENGTH_SHORT).show();
                    }
                        /*The feature of read sms is unavailable because the feature requires a permission that that user has denied. */
                    else {
                        Toast.makeText(this, "READ_SMS permission isn't granted: ", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        //@@@@@@@@@@@@@@@@@@@@@___End___SMSReceiver___@@@@@@@@@@@@@@@@@@@@@



    /*create an settings menu for all fragments to use.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*in this function we gets the items which has been pressed and handle the click event,
    it can be pressed from any fragment in the application.*/
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