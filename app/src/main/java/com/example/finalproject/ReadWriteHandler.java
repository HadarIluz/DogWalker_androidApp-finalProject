package com.example.finalproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteHandler {

    private static String file_path;
    public static final String FILE_NAME="isWaterNeeded.txt";
    private static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getAppContext());

    ReadWriteHandler(){}

    public static void writeToSP(List<Country> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("dogsList", json);
        editor.commit();
    }

    public static ArrayList<Country> readFromSP(){
        ArrayList<Country> dogs = new ArrayList<>();
        Gson gson = new Gson();
        String json = prefs.getString("dogsList", "");
        if(!json.isEmpty()){
            Type type = new TypeToken<List<Country>>(){
            }.getType();
            dogs = gson.fromJson(json, type);
        }
        return dogs;
    }
}


