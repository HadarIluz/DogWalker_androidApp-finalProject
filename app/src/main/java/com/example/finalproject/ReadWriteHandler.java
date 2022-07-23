package com.example.finalproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteHandler {

    private static String file_path;
    public static final String FILE_NAME="isWalkNeeded.txt";
    private static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getAppContext());

    ReadWriteHandler(){}

    //-------------------------START SP---------------------------------

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
    //-------------------------END SP---------------------------------




    //-------------------------START RAW---------------------------------

    public static String readFromRAW(Context context) {
        file_path = context.getFilesDir().getAbsolutePath();
        StringBuilder text = null;
        String line;
        //Get the text file
        File file = new File(file_path,File.separator+FILE_NAME);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            //Read text from file
            InputStream inputStream = new FileInputStream(file);
            text = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            inputStream.close();
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}


