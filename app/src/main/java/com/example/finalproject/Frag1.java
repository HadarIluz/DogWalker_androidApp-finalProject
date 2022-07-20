package com.example.finalproject;

import android.os.Bundle;
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
    ArrayList<com.example.finalproject.Country> countries;
    com.example.finalproject.MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*-->Informs the operating system that there is a menu.*/
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.frag_1, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(getActivity()).get(com.example.finalproject.MainViewModel.class);

        // Lookup the recyclerview in activity layout
        RecyclerView rvCountry = (RecyclerView) view.findViewById(R.id.rvCountries);

        // Create adapter passing in the sample user data
        DogAdapter adapter = new DogAdapter(viewModel.getCountryMutableLiveData().getValue(), viewModel, viewModel.getItemSelectedLiveData().getValue());

        viewModel.getCountryMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<com.example.finalproject.Country>>() {
            @Override
            public void onChanged(ArrayList<com.example.finalproject.Country> countries) {
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getItemSelectedLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                adapter.notifyDataSetChanged();
            }
        });

        // Attach the adapter to the recyclerview to populate items
        rvCountry.setAdapter(adapter);
        // Set layout manager to position the items
        rvCountry.setLayoutManager(new LinearLayoutManager(getContext()));
        // That's all!

        super.onViewCreated(view, savedInstanceState);
    }

}
