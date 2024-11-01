package com.example.djr_midterm_csi460.trip_management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djr_midterm_csi460.R;

import java.util.ArrayList;

// This class defines the adapter for the recycler view in the main activity
public class TripRVAdapter extends RecyclerView.Adapter<TripRVAdapter.ViewHolder>
{
    private ArrayList<Trip> tripModelArrayList;
    private Context context;
    private static final int REQUEST_CODE = 1;

    // Constructor for creating a new TripRVAdapter object
    public TripRVAdapter(ArrayList<Trip> tripModelArrayList, Context context)
    {
        this.tripModelArrayList = tripModelArrayList;
        this.context = context;
    }

    // onCreateViewHolder method used for creating a new view holder for the recycler view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trip_rv_item, parent, false);

        return new ViewHolder(view);
    }

    // onBindViewHolder method used for binding the data to the view holder
    // and setting up the click listener for each item in the recycler view.
    // when an item is clicked it opens the TripCUDActivity to edit or delete the trip,
    // and passes the EditMode flag as well as the id in order to get the trip from the database
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Trip model = tripModelArrayList.get(position);

        holder.tvDestination.setText("Destination: " + model.getDestination());
        holder.tvStartDate.setText("Start-Date: \n" + model.getStartDateString());
        holder.tvEndDate.setText("End-Date: \n" + model.getEndDateString());
        holder.tvNotes.setText("Notes: " + model.getNotes());
        holder.rbReview.setRating(model.getReview());
        holder.rbReview.setIsIndicator(true);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, TripCUDActivity.class);

                intent.putExtra("id", model.getId());

                intent.putExtra("EditMode", true);

                // Start the TripCUDActivity for result.
                // I do not quite understand why i need to cast the context to an activity,
                // but this is how i was able to run the startActivityForResult method,
                // which is what I am doing in the main activity for the create form.
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    // getItemCount method used for getting the number of items in the recycler view
    @Override
    public int getItemCount()
    {
        return tripModelArrayList.size();
    }


    // ViewHolder class used for holding the views to represent each item in the tripModelArrayList
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvDestination, tvStartDate, tvEndDate, tvNotes;
        private RatingBar rbReview;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            rbReview = itemView.findViewById(R.id.rbReview);
        }
    }
}
