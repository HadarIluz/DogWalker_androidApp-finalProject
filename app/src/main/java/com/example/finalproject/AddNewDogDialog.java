package com.example.finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/*
This class crate new dog, creates a window that is built dynamically and allows the user to enter all the details of the dog.
If one of the details is not entered there is a message and the dog is not saved.
The dog's details are kept inside the SP.
Long press - delete dog item.
Short press - presentation of the dog's details.
All data is saved and can be operated in the phone's length and width mode.
*/

public class AddNewDogDialog extends DialogFragment {
    DatePickerDialog picker;
    private static String file_path;
    //in this file all the data about the dogs will saved.
    public static final String FILE_NAME="dogs_data.srl";

    public AddNewDogDialog() {}


    public static AddNewDogDialog newInstance(String title) {
        AddNewDogDialog frag = new AddNewDogDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    //main:
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        CharSequence[] walkEverySpinnerArr = {"1 Day", "2 Days", "3 Days", "4 Days", "5 Days", "6 Days", "Week"};
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);

// ------------------create the dialog view------------------
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30,0,30,0);

        final EditText edDogName = new EditText(this.getContext());
        edDogName.setHint("Dog Name");

        final EditText edOwnerName = new EditText(this.getContext());
        edOwnerName.setHint("Owner Name");

        final EditText edDogDetails = new EditText(this.getContext());
        edDogDetails.setHint("Dog Details");

        final EditText edNextWalk = new EditText(this.getContext());
        edNextWalk.setHint("Next Walk: dd/mm/yyyy");

        edNextWalk.setInputType(InputType.TYPE_NULL);
        edNextWalk.setOnClickListener((view)->{
            onClickDatePicker(view, edNextWalk);
        });

        //create spinner
        final Spinner spinner = createSpinner();
        ((ViewGroup)spinner.getParent()).removeView(spinner);

        final TextView tvWalk = new TextView(this.getContext());
        tvWalk.setText("Walk Every:");

        layout.addView(edDogName);
        layout.addView(edOwnerName);
        layout.addView(edDogDetails);
        layout.addView(tvWalk);
        layout.addView(spinner);
        layout.addView(edNextWalk);

        alertDialogBuilder.setView(layout);

// ------------------set button in the dialog------------------
        alertDialogBuilder.setPositiveButton("Save",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("******* Enter in on click dialog dog ***********", "******* Enter in on click dialog dog ***********");

                if(isDogFieldsValid(edDogName, edOwnerName , edDogDetails, edNextWalk)) {
                    Dog dog = new Dog(edDogName.getText().toString(), edOwnerName.getText().toString(), edNextWalk.getText().toString(), edDogDetails.getText().toString(), spinner.getSelectedItem().toString());

                    //add the Dog into array of dogs - edit adapter list
                    DogAdapter.dogs.add(dog);
                    //Log.d("the new list is",DogAdapter.dogs.toString() );

                    // add to SP (json type)
                    ReadWriteHandler.writeToSP(DogAdapter.dogs);
                    // add notify adapter
                    Frag1.adapter.notifyDataSetChanged(); //Frag1 is FragCycle
                    dialog.dismiss();
                }

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
// ---------------------------------------------------------

        return alertDialogBuilder.create();
    }

    private Spinner createSpinner() {
        View view;
        view = this.getLayoutInflater().inflate(R.layout.spinner, null);
        Spinner spinner = (Spinner) view.findViewById(R.id.walkEveryS);
        String[] walkEvery = { "1 Day", "2 Days", "3 Days", "4 Days", "5 Days", "6 Days", "Week"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, walkEvery);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return spinner;
    }

//Open Calendar for the user to select a date.
    private void onClickDatePicker(View view, EditText edNextWalk) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() { // second Listener method
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edNextWalk.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        //Adding a limitation on the calendar to display dates starting from the current day only.
        //picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }


    private boolean isDogFieldsValid(EditText edDogName, EditText edOwnerName, EditText edDogDetails, EditText edNextWalk){
        if (edDogName.getText().toString().isEmpty()) {
            showToast("Enter Dog's name!", getContext());
            return false;
        }
        else if (edOwnerName.getText().toString().isEmpty()){
            showToast("Enter Owner`s details!", getContext());
            return false;
        }
        else if (edDogDetails.getText().toString().isEmpty()){
            showToast("Enter Dog's details!", getContext());
            return false;
        }
        else if (edNextWalk.getText().toString().isEmpty()){
            showToast("Enter Dog's next Walk!", getContext());
            return false;
        }

        //in case there is a dog white the same data base, we display a toast to user.
        for (Dog dog : DogAdapter.dogs) {
            if(edDogName.getText().toString().compareTo(dog.getName()) == 0){
                showToast("This dog's name all ready exist!", getContext());
                return false;
            }
        }
        return true;
    }

    private void showToast(String str, Context context) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }


}
