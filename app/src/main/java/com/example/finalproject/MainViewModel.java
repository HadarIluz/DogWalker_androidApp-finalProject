package com.example.finalproject;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//change
public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<com.example.finalproject.Country>> dogsLiveData;
    private ArrayList<com.example.finalproject.Country> dogs;
    private MutableLiveData<Integer> itemSelectedLiveData;
    private int itemSelected;
    //------------------file objects-------------------------------
    private static String file_path;
    public static final String FILE_NAME="remove_dogs.txt";
    private static ArrayList<String> remove_dogs;
    private ArrayList<Country> tempDogList;
    //--------------------------------------------------------------

    public MainViewModel(@NonNull Application application) {
        super(application);
        dogsLiveData = new MutableLiveData<>();
        itemSelectedLiveData = new MutableLiveData<>();
        init(application);
    }

    public MutableLiveData<ArrayList<com.example.finalproject.Country>> getDogMutableLiveData() {
        return dogsLiveData;
    }

    public MutableLiveData<Integer> getItemSelectedLiveData() {
        return itemSelectedLiveData;
    }

    public void init(Application application){
        itemSelected = -1;
        itemSelectedLiveData.setValue(itemSelected);
        dogs = new ArrayList<com.example.finalproject.Country>();
        dogsLiveData.setValue(dogs);
        // initially files params
        file_path = application.getApplicationContext().getFilesDir().getAbsolutePath();
        tempDogList = new ArrayList<Country>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());


        //--------SharedPreferences-----------
        /* Read all the dogs from the sp file and will show them when the app is start */
        tempDogList = ReadWriteHandler.readFromSP();
        dogsLiveData.setValue(tempDogList);
        //-----End SharedPreferences-----------

        //---------Raw file-----------
        //remove_dogs = new ArrayList<String>(Arrays.asList(readFile().split("\n")));
        //-----End Raw file-----------




        //-----Raw file-----------
//            try {
//                FileOutputStream writer = new FileOutputStream(file_path + File.separator + FILE_NAME);
//                writer.write(("").getBytes());
//                writer.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        //-----End Raw file-----------

    }

    //---------function for raw file---------
    public void writeData(String data) {
        File directory = new File(file_path);
        if(!directory.exists())
            directory.mkdir();

        File newFile = new File(file_path,File.separator + FILE_NAME);
        try  {
            if(!newFile.exists())
                newFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(newFile,true);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fOut);
            outputWriter.write(data + "\n");
            outputWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //---------function for raw file---------
    private String readFile() {
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

            /////////////----read line after line
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
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
