package com.example.finalproject;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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

    public MutableLiveData<ArrayList<com.example.finalproject.Country>> getCountryMutableLiveData() {
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
        boolean remember = prefs.getBoolean("switch_preference_1",false);

        //--------SharedPreferences-----------
        //create array list of all the countries wee removed
        remove_dogs = new ArrayList<>();
        for(Map.Entry<String,?> entry : prefs.getAll().entrySet()){
            if (entry.getValue() instanceof String) {
                remove_dogs.add(String.valueOf(entry.getValue()));
            }
        }
        //-----End SharedPreferences-----------

        //---------Raw file-----------
        //remove_countries = new ArrayList<String>(Arrays.asList(readFile().split("\n")));
        //-----End Raw file-----------

        if(remember == true)
        {
            for (Country country : dogsLiveData.getValue()) {
                //if the country dont show in the list of the cuntries we have been deleted so we add him/
                if (!remove_dogs.contains(country.getName())) {
                    tempDogList.add(country);
                }
            }
            //update the countriesLiveData we the current new data.
            dogsLiveData.setValue(tempDogList);
        }
        else
        {
        //if we select false we clear the SP file.
            //-----SharedPreferences-----------
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            //-------End SharedPreferences------

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
