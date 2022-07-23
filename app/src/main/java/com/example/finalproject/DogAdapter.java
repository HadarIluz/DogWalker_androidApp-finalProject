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

/*
DogAdapter class is; the middle block between the ‘display data’ and the ‘data source’.
Must define an inner (non static) class("InnerAdapterDog") that extends from ViewHolder.
We added handling of writing to SP
And reading from RAW file. for the longClickEvent in the InnerAdapterDog class.
 */
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

    /* ##########_3 functions from Adapter abstract class_##########*/
    /*3 function that we must to implement because we extends (Heirs) the RecyclerView.Adapter class.
     * 1. onCreateViewHolder
     * 2. onBindViewHolder
     * 3. getItemCount
     *   */
//1.
/*In this function we do inflate to the layout: "dog_item" and sent it to the constructor*/
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
//2.
/*This function gets (1).the 'InnerAdapterDog' that we want to fill with all the data and
                     (2).the location in the data structure that from there(from the location in the array) we take the data.*/
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

//3.
/*This function return how data from this specific type there is in the database */
    @Override
    public int getItemCount() {
        return dogs.size();
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    /*
    -------------------------------------------------------------------
    ---------------------------INNER CLASS---------------------------
    -------------------------------------------------------------------
     */
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
            //TODO: (1) OPEN CAMERA AND SAVE DOGS PIC
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
            tvNextWalk.setText("Next Walk: " + dog.getNextWalkDate().getDate() + "/" + dog.getNextWalkDate().getMonth() + "/" + dog.getNextWalkDate().getYear());
            tvWalkEvery.setText("Walk Every: " + dog.getWalkEvery());

            Context context =imFlag.getContext();
            //TODO: (1)
            //int id = context.getResources().getIdentifier(country.getFlag(), "drawable", context.getPackageName());
            //imFlag.setImageResource(id);

            details = dog.getDetails();

            itemView.setOnClickListener((view)->{
                onClickShowDetails(position);
            });
            itemView.setOnLongClickListener((view)->{
                onLongClickDeleteDog(position);
                return false;
            });
        }

        private void onClickShowDetails(int position){
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

        private void onLongClickDeleteDog(int position){
            //-----SharedPreferences-----------
            // Deletes the specific dog
            if(MyBroadcastReceiver.dogsWalkeLArrayList.contains(dogs.get(position)))
                MyBroadcastReceiver.dogsWalkeLArrayList.remove(dogs.get(position));
            dogs.remove(position);
            ReadWriteHandler.writeToSP(dogs);
            //notifyDataSetChanged();

/*
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(viewModel.getApplication().getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(viewModel.getDogMutableLiveData().getValue().get(position).getName(),viewModel.getDogMutableLiveData().getValue().get(position).getName());
            editor.commit();
*/
            //------End SharedPreferences------

            //-----Raw file-----------
            //viewModel.writeData(viewModel.getCountryMutableLiveData().getValue().get(position).getName());
            //-----End Raw file-------

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
    /*
    -------------------------------------------------------------------
    ----------------------END OF INNER CLASS---------------------------
    -------------------------------------------------------------------
     */
}
