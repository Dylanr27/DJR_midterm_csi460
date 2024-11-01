package com.example.djr_midterm_csi460;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djr_midterm_csi460.trip_management.DBHelper;
import com.example.djr_midterm_csi460.trip_management.Trip;
import com.example.djr_midterm_csi460.trip_management.TripCUDActivity;
import com.example.djr_midterm_csi460.trip_management.TripRVAdapter;

import java.util.ArrayList;


// This class defines the main activity for the application,
// it also displays a list of trips in card form
public class MainActivity extends AppCompatActivity
{
    private DBHelper dbHelper;
    private Button btnAddTrip;
    private ArrayList<Trip> tripModelArrayList;
    private TripRVAdapter tripRVAdapter;
    private RecyclerView rvTrips;
    private static final int REQUEST_CODE = 1;

    // onCreate method used for setting up the activity, creating the database helper,
    // setting up the add button and running the refreshTripList method to
    // display the trips, if there are any, in the recycler view when the app starts
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        btnAddTrip = findViewById(R.id.btnAddTrip);

        // when the add button is clicked, it opens the TripCUDActivity to create a new trip
        btnAddTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, TripCUDActivity.class);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        rvTrips = findViewById(R.id.rvTrips);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvTrips.setLayoutManager(linearLayoutManager);

        refreshTripList();
    }

    // refreshTripList method is used for refreshing the list of trips in the recycler view
    private void refreshTripList()
    {
        tripModelArrayList = new ArrayList<>();

        tripModelArrayList = dbHelper.getTrips();

        tripRVAdapter = new TripRVAdapter(tripModelArrayList, this);

        rvTrips.setAdapter(tripRVAdapter);
    }

    // this onActivityResult method is used for handling the result of the TripCUDActivity
    // when it returns to the main activity by calling refreshTripList to update the list of trips
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Refresh data when returning from TripCUDActivity
            refreshTripList();
        }
    }
}