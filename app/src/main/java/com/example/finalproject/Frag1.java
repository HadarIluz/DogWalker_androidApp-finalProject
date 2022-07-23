package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Frag1 extends Fragment implements LifecycleOwner {
    ArrayList<Dog> dogs;
    com.example.finalproject.MainViewModel viewModel;
    public static DogAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*-->Informs the operating system that there is a menu.*/
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("**** in start Frag1 ****", "**** in start Frag1 ****");

        viewModel = new ViewModelProvider(getActivity()).get(com.example.finalproject.MainViewModel.class);
        Log.d("**** in start Frag1 after viewModel ****", "**** in start Frag1 after viewModel ****");
        // Lookup the recyclerview in activity layout(id of frag_1.xml)
        RecyclerView rvDog = (RecyclerView) view.findViewById(R.id.rvDogs);

        // Create adapter passing in the sample user data
        adapter = new DogAdapter(viewModel.getDogMutableLiveData().getValue(), viewModel, viewModel.getItemSelectedLiveData().getValue());

        //observer that will notify if any change has happened in order to save data.
        viewModel.getDogMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Dog>>() {
            @Override
            public void onChanged(ArrayList<Dog> dogs) {
                adapter.notifyDataSetChanged();
            }
        });

//### observer that will notify if we select new item. ###
        viewModel.getItemSelectedLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                adapter.notifyDataSetChanged();
            }
        });
        Log.d("**** in Frag1 ****", "**** in Frag1 ****");

        // Attach the adapter to the recyclerview to populate items
        rvDog.setAdapter(adapter);
        // Set layout manager to position the items
        rvDog.setLayoutManager(new LinearLayoutManager(getContext()));
        super.onViewCreated(view, savedInstanceState);
    }
}