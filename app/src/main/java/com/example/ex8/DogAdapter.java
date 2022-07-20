package com.example.ex8;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//change
public class DogAdapter extends RecyclerView.Adapter<DogAdapter.InnerAdapterDog> implements LifecycleOwner {
    private List<Country> countries;
    private int focusedItem;
    DogDetailsFrag dogDetailsFrag;
    MainViewModel viewModel;


    public DogAdapter(List<Country> countriesList, MainViewModel mainViewModel, int itemSelected) {
        countries = countriesList;
        viewModel = mainViewModel;
        focusedItem = itemSelected;
    }

    @NonNull
    @Override
    public InnerAdapterDog onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.country_item, parent, false);

        // Return a new holder instance
        InnerAdapterDog viewHolder = new InnerAdapterDog(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAdapterDog holder, int position) {
        Country country = countries.get(position);
        if(position == focusedItem){
            holder.itemView.setBackgroundColor(Color.parseColor("#EC96EC"));
        } else{
            holder.itemView.setBackgroundColor(Color.parseColor("#E4E4E4"));
        }
        holder.BindData(position, country);
    }


    @Override
    public int getItemCount() {
        return countries.size();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    //---------------------------INNER CLASS---------------------------
    public class InnerAdapterDog extends RecyclerView.ViewHolder{
        public ImageView imFlag;
        public TextView countryName, population;
        public View itemView;
        public String details;

        public InnerAdapterDog(@NonNull View itemView) {
            super(itemView);
            imFlag = (ImageView) itemView.findViewById(R.id.imageFlag);
            countryName = (TextView) itemView.findViewById(R.id.tvCountyName);
            population = (TextView) itemView.findViewById(R.id.tvPopulation);
            this.itemView = itemView;
            itemView.setClickable(true);
        }

        public void BindData(int position, Country country){
            countryName.setText(country.getName());
            population.setText(country.getShorty());

            Context context =imFlag.getContext();
            //int id = context.getResources().getIdentifier(country.getFlag(), "drawable", context.getPackageName());
            //imFlag.setImageResource(id);

            details = country.getDetails();

            itemView.setOnClickListener((view)->{
                onClick(position);
            });
            itemView.setOnLongClickListener((view)->{
                onLongClick(position);
                return false;
            });
        }

        private void onClick(int position){
            Context context = itemView.getContext();
            focusedItem = getLayoutPosition();
            notifyItemChanged(focusedItem);
            viewModel.getItemSelectedLiveData().setValue(focusedItem);

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragContainer, DogDetailsFrag.class, null, "countryDetails")
                        .addToBackStack("BBB")
                        .commit();
                ((AppCompatActivity)context).getSupportFragmentManager().executePendingTransactions();
                dogDetailsFrag = (DogDetailsFrag) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag("countryDetails");
            }
            else{
                try{
                    dogDetailsFrag = (DogDetailsFrag)((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(R.id.country_details);
                } catch (Exception e){
                    ((AppCompatActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.country_details, DogDetailsFrag.class, null, "countryDetails").addToBackStack("BBB")
                            .commit();
                }

            }
        }

        private void onLongClick(int position){
            //-----SharedPreferences-----------
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(viewModel.getApplication().getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(viewModel.getCountryMutableLiveData().getValue().get(position).getName(),viewModel.getCountryMutableLiveData().getValue().get(position).getName());
            editor.commit();
            //------End SharedPreferences------

            //-----Raw file-----------
            //viewModel.writeData(viewModel.getCountryMutableLiveData().getValue().get(position).getName());
            //-----End Raw file-------

            countries.remove(position);
            if(focusedItem == position){
                focusedItem = -1;
                viewModel.getItemSelectedLiveData().setValue(-1);
            }
            if(position < focusedItem){
                focusedItem --;
                viewModel.getItemSelectedLiveData().setValue(focusedItem);
            }
            notifyDataSetChanged();
        }

    }
//------------------------------------------------------------------
}
