package com.example.djr_midterm_csi460.trip_management;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.djr_midterm_csi460.DateUtils;
import com.example.djr_midterm_csi460.R;

import java.text.ParseException;
import java.util.Calendar;
import jp.wasabeef.richeditor.RichEditor;

// This class defines the activity for creating, updating, and deleting trips
public class TripCUDActivity extends AppCompatActivity
{
    private TripDBHelper tripDbHelper;
    private EditText etDestination, etNotes;
    private Button btnSelectStartDate, btnSelectEndDate, btnCreateUpdateTrip, btnDelete;
    private TextView tvStartDate, tvEndDate;
    private RichEditor reNotes;
    private RatingBar rbReview;
    private Calendar calendar = Calendar.getInstance();

    // this method is called when the activity is created. Initializes the UI elements,
    // retrieves trip data if in edit mode, and sets up the click listeners for the buttons.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_trip);

        tripDbHelper = new TripDBHelper(this);
        etDestination = findViewById(R.id.etDestination);
        btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        reNotes = findViewById(R.id.reNotes);
        rbReview = findViewById(R.id.rbReview);
        btnCreateUpdateTrip = findViewById(R.id.btnCreateEditTrip);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();

        boolean isEditMode = intent.getBooleanExtra("EditMode", false);

        // If the Edit mode flag was provided, fill in the views with the trips data,
        // and change button text to update
        if (isEditMode)
        {
            btnDelete.setVisibility(View.VISIBLE);

            Trip trip = tripDbHelper.getTrip(intent.getIntExtra("id", -1));

            // if this check fails, we know the trip exists and the id is valid, therefore we will
            // not be checking if id is -1 or null for the other methods in this class
            if (trip == null)
            {
                toast("Trip not found");

                finish();

                return;
            }

            etDestination.setText(trip.getDestination());
            tvStartDate.setText(trip.getStartDateString());
            tvEndDate.setText(trip.getEndDateString());
            reNotes.setHtml(trip.getNotes());
            rbReview.setRating(trip.getReview());
            btnCreateUpdateTrip.setText("Update Trip");
        }

        // shows a date picker dialog and sets the selected date to the text view provided
        btnSelectStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){ showDatePickerDialog(tvStartDate);}
        });

        // shows a date picker dialog and sets the selected date to the text view provided
        btnSelectEndDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog(tvEndDate);
            }
        });

        // takes the entered data and creates a new trip in the database, or updates an existing trip.
        // In edit mode, it gets the trip id from the intent and uses it to update the trip in the database.
        // If not in edit mode, it simply creates a new trip in the database,
        // without being provided with an id because the id is auto-incremented.
        btnCreateUpdateTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String destination = etDestination.getText().toString();
                String startDate = tvStartDate.getText().toString();
                String endDate = tvEndDate.getText().toString();
                String notes = reNotes.getHtml();
                int review = (int) rbReview.getRating();

                // we arent checking for review score because 0 is an acceptable score
                if (destination.isEmpty()
                        || startDate.isEmpty()
                        || endDate.isEmpty()
                        || notes.isEmpty()
                )
                {
                    toast("Please enter all fields");

                    return;
                }

                if (isEditMode)
                {
                    Intent intent = getIntent();

                    int tripId = intent.getIntExtra("id", -1);

                    try {
                        tripDbHelper.updateTrip(tripId, destination, startDate, endDate, notes, review);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    toast("Trip updated successfully");

                    sendResult();

                    return;
                }

                try {
                    tripDbHelper.createTrip(destination, startDate, endDate, notes, review);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                toast("Trip created successfully");

                sendResult();
            }
        });

        // deletes the trip from the database if the delete button is clicked
        // based on the id provided in the intent.
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = getIntent();

                int tripId = intent.getIntExtra("id", -1);

                tripDbHelper.deleteTrip(tripId);

                toast("Trip deleted successfully");

                sendResult();
            }
        });

    }

    // this method is used for showing a date picker dialog and setting the
    // selected date to the text view provided
    private void showDatePickerDialog(TextView textView)
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) ->
                {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    textView.setText(DateUtils.formatDate(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    // this method is used for sending the result_ok result to the main activity,
    // while we are not passing any data with it, the resultIntent is still necessary for setResult
    private void sendResult()
    {
        Intent resultIntent = new Intent();

        setResult(RESULT_OK, resultIntent);

        finish();
    }

    // this method is used for showing a toast message to the user
    private void toast (String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}