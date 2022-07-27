package com.example.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;


//extern class which extends BroadcastReceiver.
//This class for dynamic and static registrations!!
//This declared in the manifest file and works even if the app is closed.
public class SMSReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private MainViewModel myViewModel;

    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage message = messages[0];
        if(message != null)
        {
            String sender = message.getDisplayOriginatingAddress();// get who write the sms.
            String body = message.getDisplayMessageBody();// get the content from the sms.

            //create string with the name of the sender and content as requested in lab.
            String senderAndContent = "New message from: " + sender + "\nThe message: " + body;
        }
        else {
            Log.e(SMS_RECEIVED, "message is null");
        }
    }
}