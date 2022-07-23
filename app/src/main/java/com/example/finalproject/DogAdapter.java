package com.example.finalproject;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//change
public class DogAdapter extends RecyclerView.Adapter<DogAdapter.InnerAdapterDog> implements LifecycleOwner {
    public static List<Dog> dogs;
    private int focusedItem;
    DogDetailsFrag dogDetailsFrag;
    MainViewModel viewModel;


    public DogAdapter(List<Dog> dogsList, MainViewModel mainViewModel, int itemSelected) {
        dogs = dogsList;
        viewModel = mainViewModel;
        focusedItem = itemSelected;
    }

    @NonNull
    @Override
    public InnerAdapterDog onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.dog_item, parent, false);

        // Return a new holder instance
        InnerAdapterDog viewHolder = new InnerAdapterDog(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAdapterDog holder, int position) {
        InnerAdapterDog viewHolder = (InnerAdapterDog)holder;

        Dog dog = dogs.get(position);
        if(position == focusedItem){
            holder.itemView.setBackgroundColor(Color.parseColor("#EC96EC"));
        } else{
            holder.itemView.setBackgroundColor(Color.parseColor("#E4E4E4"));
        }
        holder.BindData(position, dog);
    }


    @Override
    public int getItemCount() {
        return dogs.size();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    //---------------------------INNER CLASS---------------------------
    public class InnerAdapterDog extends RecyclerView.ViewHolder{

        public TextView tvDogName;
        public TextView tvOwnerName;
        public TextView tvWalkEvery;
        public TextView tvNextWalk;

        public ImageView imFlag;
        public View itemView;
        public String details;

        public InnerAdapterDog(@NonNull View itemView) {
            super(itemView);
            imFlag = (ImageView) itemView.findViewById(R.id.imageDog);

            tvDogName = (TextView) itemView.findViewById(R.id.tvDogName);
            tvOwnerName = (TextView) itemView.findViewById(R.id.tvOwnerName);
            tvNextWalk = (TextView) itemView.findViewById(R.id.tvDate);
            tvWalkEvery = (TextView) itemView.findViewById(R.id.tvWalkEvery);

            this.itemView = itemView;
            itemView.setClickable(true);
        }

        public void BindData(int position, Dog dog){
            tvDogName.setText(dog.getName());
            tvOwnerName.setText("Owner`s Name: " + dog.getOwnerName());
            //tvNextWalk.setText("Next Walk: " + dog.getNextWalkDate().getDate() + "/" + dog.getNextWalkDate().getMonth() + "/" + dog.getNextWalkDate().getYear());
            tvWalkEvery.setText("Walk Every: " + dog.getWalkEvery());

            Context context =imFlag.getContext();

            //int id = context.getResources().getIdentifier(country.getFlag(), "drawable", context.getPackageName());
            //imFlag.setImageResource(id);

            details = dog.getDetails();

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
                        .replace(R.id.fragContainer, DogDetailsFrag.class, null, "dogDetails")
                        .addToBackStack("BBB")
                        .commit();
                ((AppCompatActivity)context).getSupportFragmentManager().executePendingTransactions();
                dogDetailsFrag = (DogDetailsFrag) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag("dogDetails");
            }
            else{
                try{
                    dogDetailsFrag = (DogDetailsFrag)((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(R.id.dog_details);
                } catch (Exception e){
                    ((AppCompatActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.dog_details, DogDetailsFrag.class, null, "dogDetails").addToBackStack("BBB")
                            .commit();
                }

            }
        }

        private void onLongClick(int position){
            //-----SharedPreferences-----------
            // delete the plant
            if(MyBroadcastReceiver.dogsWalkeLArrayList.contains(dogs.get(position)))
                MyBroadcastReceiver.dogsWalkeLArrayList.remove(dogs.get(position));
            dogs.remove(position);
            ReadWriteHandler.writeToSP(dogs);
            notifyDataSetChanged();


            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(viewModel.getApplication().getApplicationContext());
            //SharedPreferences.Editor editor = sharedPreferences.edit();
            //editor.putString(viewModel.getDogMutableLiveData().getValue().get(position).getName(),viewModel.getDogMutableLiveData().getValue().get(position).getName());
            //editor.commit();
            //------End SharedPreferences------

            //-----Raw file-----------
            //viewModel.writeData(viewModel.getCountryMutableLiveData().getValue().get(position).getName());
            //-----End Raw file-------

            //dogs.remove(position);
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
