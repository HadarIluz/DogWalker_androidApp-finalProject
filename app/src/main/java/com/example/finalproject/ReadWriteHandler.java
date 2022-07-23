package com.example.finalproject;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteHandler {

    private static String file_path;
    public static final String FILE_NAME="isWaterNeeded.txt";
    private static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getAppContext());

    ReadWriteHandler(){}

    public static void writeToSP(List<Dog> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("dogsList", json);
        editor.commit();
    }

    public static ArrayList<Dog> readFromSP(){
        ArrayList<Dog> dogs = new ArrayList<>();
        Gson gson = new Gson();
        String json = prefs.getString("dogsList", "");
        if(!json.isEmpty()){
            Type type = new TypeToken<List<Dog>>(){
            }.getType();
            dogs = gson.fromJson(json, type);
        }
        return dogs;
    }
}


