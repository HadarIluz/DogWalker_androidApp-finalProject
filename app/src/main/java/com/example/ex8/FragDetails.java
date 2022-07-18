package com.example.ex8;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class FragDetails extends Fragment implements LifecycleOwner {
    TextView tvDetails;
    com.example.ex8.MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.country_details, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.tvDetails = view.findViewById(R.id.tvDetails);
        viewModel = new ViewModelProvider(getActivity()).get(com.example.ex8.MainViewModel.class);

        //viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
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
