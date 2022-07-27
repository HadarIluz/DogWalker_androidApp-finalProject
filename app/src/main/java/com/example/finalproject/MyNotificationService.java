package com.example.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class MyNotificationService extends Service {

    /*
    Foreground services show a status bar notification, so that users
     are actively aware that your app is performing a task in the foreground
     and is consuming system resources. The notification cannot be dismissed
     unless the service is either stopped or removed from the foreground.
     */
    private MyService service;
    public static final String FOREGROUND_PROGRESS = ".foreground-progress";

    public final static int NOTIFICATION_ID1 = 1;
    Notification.Builder notificationBuilder;
    static NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //----------------------------------

    public MyNotificationService(){}

    @Override
    public void onCreate() {
        initService();
        super.onCreate();
    }

    private void initService(){
        String CHANNEL_ID = "my_channel_1";
        if(notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"my main channel", notificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("You have a message")
                .setSmallIcon(R.drawable.alret)
                .setContentIntent(pendingIntent);
        startForeground(NOTIFICATION_ID1, updateNotification(Integer.toString(0)));

    }


    //---------------------------------

    // happens every time the app starts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.service == null) {
            this.service = new MyService();
            service.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyService extends Thread {

        @Override
        public void run() {
            Intent intent = new Intent(FOREGROUND_PROGRESS);
            try {
                updateNotification("Welcome!");
                sendBroadcast(intent);
                Thread.sleep(1000 * 2);
                stopSelf();
                while (true) {
                    Calendar cl = Calendar.getInstance();

                    if (isWalkNeeded()) {
                        ReadWriteHandler.writeToRAW("1", getApplicationContext());

                        ArrayList<Dog> dogs = ReadWriteHandler.readFromSP();
                        for (Dog dog : dogs) {
                            if (dog.checkWalkNeeded() && dog.isSmsDog() == true) {
                                dog.setSmsDog(false);
                                smsSendMessage(dog);
                                Log.i("sms"," sms sent about dog: " + dog.getName());
                            }
                        }
                        ReadWriteHandler.writeToSP(dogs);

                        updateNotification("You need to take a dog to a walk\nEnter into app to see who!");
                        Thread.sleep(1000 * 60);
                        stopSelf();


                        intent = new Intent(FOREGROUND_PROGRESS);
                        sendBroadcast(intent);

                    } else {
                        ReadWriteHandler.writeToRAW("0", getApplicationContext());
                    }

                    Thread.sleep(1000 * 60);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }//end run

        private boolean isWalkNeeded() {
            ArrayList<Dog> dogs = ReadWriteHandler.readFromSP();
            for (Dog dog : dogs) {
                if (dog.checkWalkNeeded()) {
                    dog.setSmsDog(true);
                    return true;
                }
            }
            return false;
        }
    }//end MyService


    public Notification updateNotification(String details) {
        notificationBuilder.setContentText(details).setOnlyAlertOnce(false);
        Notification noti = notificationBuilder.build();
        noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(NOTIFICATION_ID1, noti);
        return noti;
    }

    public void smsSendMessage(Dog dog) {

        //Getting intent and PendingIntent instance
        Intent intentSms=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intentSms,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage("+1-555-521-5554", null,  dog.getName()+ " need to walk and eat!", pi,null);
    }



}
