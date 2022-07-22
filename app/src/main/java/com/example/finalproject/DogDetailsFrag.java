package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class DogDetailsFrag extends Fragment implements LifecycleOwner {
    TextView tvDetails;
    com.example.finalproject.MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*-->Informs the operating system that there is a menu.*/
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.country_details, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.tvDetails = view.findViewById(R.id.tvDetails);
        viewModel = new ViewModelProvider(getActivity()).get(com.example.finalproject.MainViewModel.class);

        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.getItemSelectedLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer > -1)
                    tvDetails.setText(viewModel.getCountryMutableLiveData().getValue().get(viewModel.getItemSelectedLiveData().getValue()).getDetails());
                else
                    tvDetails.setText("");
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}