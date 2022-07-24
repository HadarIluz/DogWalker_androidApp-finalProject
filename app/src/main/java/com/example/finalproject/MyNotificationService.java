package com.example.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class MyNotificationService extends Service {

    private MyService service;
    public static final String FOREGROUND_PROGRESS = "com.example.finalProject.foreground-progress";

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
                        //if (cl.get(Calendar.HOUR_OF_DAY) == 6 && cl.get(Calendar.MINUTE) == 26) {
                            updateNotification("You need to take a dog to a walk\nEnter into app to see who!");
                            Thread.sleep(1000 * 60);
                            stopSelf();
                        //}
                        intent = new Intent(FOREGROUND_PROGRESS);
                        sendBroadcast(intent);
                    } else {
                        ReadWriteHandler.writeToRAW("0", getApplicationContext());
                    }

                    Thread.sleep(1000 * 3);

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
                    return true;
                }
            }
            return false;
        }
    }//end


    public Notification updateNotification(String details) {
        notificationBuilder.setContentText(details).setOnlyAlertOnce(false);
        Notification noti = notificationBuilder.build();
        //noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(NOTIFICATION_ID1, noti);
        return noti;
    }


}
