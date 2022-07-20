package com.example.ex8;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class AddNewDogDialog extends DialogFragment {
    DatePickerDialog picker;
    private static String file_path;
    public static final String FILE_NAME="dogs_data.srl";

    public AddNewDogDialog() {}


    public static AddNewDogDialog newInstance(String title) {
        AddNewDogDialog frag = new AddNewDogDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

//    public void show(FragmentManager fm1, String fragment_alert) {
//    }




    //main

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        CharSequence[] walkEverySpinnerArr = {"1 Day", "2 Day", "3 Day", "4 Day", "5 Day", "6 Day", "7 Day"};
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
                if(isDogFieldsValid(edDogName, edOwnerName , edDogDetails, edNextWalk)){
                    Dog dog = new Dog(edDogName.getText().toString(),edOwnerName.getText().toString(), edNextWalk.getText().toString(), edDogDetails.getText().toString(), spinner.getSelectedItem().toString());
                    //add the Dog into array of dogs - edit adapter list
                /*   //TODO:THINK HOW
                    DogAdapter.dogs.add(dog);
                    // add to SP (json type)
                    ReadWriteHandler.writeToSP(DogAdapter.dogs);
                    // add notify adapter
                    FragCycle.adapter.notifyDataSetChanged();
                    dialog.dismiss();

                 */
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
        String[] walkEvery = { "1 Day", "2 Day", "3 Day", "4 Day", "5 Day", "6 Day", "7 Day"};
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

    private void onClickDatePicker(View view, EditText edNextWalk) {
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
        /*
        for (Dog dog : DogAdapter.dogs) {
            if(edDogName.getText().toString().compareTo(dog.getName()) == 0){
                showToast("This dog's name all ready exist!", getContext());
                return false;
            }
        }
        */
        return true;
    }

    private void showToast(String str, Context context) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }


}
