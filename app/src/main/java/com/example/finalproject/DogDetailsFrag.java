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

public class DogDetailsFrag extends Fragment implements LifecycleOwner {
    TextView tvDetails;
    private MainViewModel viewModel;

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
        return inflater.inflate(R.layout.dog_details, container, false);
    }

    /*Makes sure we save the specific row the user is stand on when turning the cell phone over.
    *In case the user don`t select any dog`s row we don`t display any details when turning the cell phone over.
     * otherwise  all the dog`s details will display.
    * */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tvDetails = (TextView) view.findViewById(R.id.tvDetails);

        viewModel = MainViewModel.getInstance(getActivity().getApplication(), getContext(), getActivity());

        viewModel.getItemSelectedLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > -1) {
                    tvDetails.setText("Name: " + viewModel.getDogMutableLiveData().getValue().get(viewModel.getItemSelectedLiveData().getValue()).getName() +
                            "\nOwner`s Name: " + viewModel.getDogMutableLiveData().getValue().get(viewModel.getItemSelectedLiveData().getValue()).getOwnerName() +
                            "\nDetails: " + viewModel.getDogMutableLiveData().getValue().get(viewModel.getItemSelectedLiveData().getValue()).getDetails() +
                            "\nWalk Every: " + viewModel.getDogMutableLiveData().getValue().get(viewModel.getItemSelectedLiveData().getValue()).getWalkEvery() +
                            printNextWalk(viewModel.getDogMutableLiveData().getValue().get(viewModel.getItemSelectedLiveData().getValue())));
                } else {
                    /*In case the user don`t select any dog`s row we don`t display any details when turning the cell phone over.*/
                    tvDetails.setText("");
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void setDetailsInContainer(Dog dog) {
        tvDetails.setText(dog.toString());
    }

    private String printNextWalk(Dog dog) {
        String nextWalk = "\nNext Walk: " + dog.getNextWalkDate().getDate() + "/" + dog.getNextWalkDate().getMonth() + "/" + dog.getNextWalkDate().getYear();
        return nextWalk;
    }

}
