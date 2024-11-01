package com.example.djr_midterm_csi460.trip_management;

import androidx.annotation.NonNull;

import com.example.djr_midterm_csi460.DateUtils;

import java.util.Date;

// This class defines the Trip model for the database.
public class Trip
{
    private int id;
    private String destination;
    private Date startDate;
    private Date endDate;
    private String notes;
    private int review;

    // constructor for creating a new Trip object
    public Trip(
            int id,
            String destination,
            Date start_date,
            Date end_date,
            String notes,
            int review
    )
    {
        this.id = id;
        this.destination = destination;
        this.startDate = start_date;
        this.endDate = end_date;
        this.notes = notes;
        this.review = review;
    }

    // get and set methods for each field, id has only a getter method
    public int getId()
    {
        return id;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public String getStartDateString()
    {
        return DateUtils.formatDate(startDate);
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public String getEndDateString()
    {
        return DateUtils.formatDate(endDate);
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public int getReview()
    {
        return review;
    }

    public void setReview(int review)
    {
        this.review = review;
    }

    // overridden toString method for debugging purposes
    @NonNull
    @Override
    public String toString()
    {
        return "\nID: " + getId() + "\n" +
                "Destination: " + destination + "\n" +
                "Start Date: " + getStartDate() + "| End Date: " + getEndDate() + "\n" +
                "Notes: " + getNotes() + "\n" +
                "Review score: " + getReview();

    }
}
