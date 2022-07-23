package com.example.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static ArrayList<Dog> dogsWalkeLArrayList = new ArrayList<>();

    /*
    Broadcast Receiver: Dynamic Registration -> we still don`t add something related to MyBroadcastReceiver in the manifests.
    This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String isWaterNeeded = ReadWriteHandler.readFromRAW(context);
        if(isWaterNeeded.compareTo("1") == 0){
            // create an array list for all the next dogs that need to take to a walk.
            ArrayList<Dog> dogs = ReadWriteHandler.readFromSP();
            for(Dog dog: dogs){
                if (dog.checkWalkNeeded() && (!dogsWalkeLArrayList.contains(dog))){
                    dogsWalkeLArrayList.add(dog);
                }
            }

        //****_Frag1 is FragCycle_***
            Frag1.adapter.notifyDataSetChanged();

        }
    }

}
