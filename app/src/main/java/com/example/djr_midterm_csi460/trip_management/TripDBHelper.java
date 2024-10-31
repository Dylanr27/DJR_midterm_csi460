package com.example.djr_midterm_csi460.trip_management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// This class defines the database helper for the application, it creates the database and tables,
// and provides methods for CRUD operations.
public class TripDBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "travelDB";
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE_NAME = "trips";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_REVIEW = "review";

    // Constructor for creating a new TripDBHelper object
    public TripDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate method used for creating the tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESTINATION + " TEXT, " +
                COLUMN_START_DATE + " INTEGER, " +
                COLUMN_END_DATE + " INTEGER, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_REVIEW + " TEXT" +
        ")";

        db.execSQL(query);
    }

    // createTrip method used for inserting trips into the trip table,
    // because id is auto-incremented it is not included
    public void createTrip(
            String destination,
            String startDate,
            String endDate,
            String notes,
            int review
    )
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        putTripValues(destination, startDate, endDate, notes, review, values);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }



    // getTrips method used for creating trips using the data from the columns in the trip table,
    // and returning them as an ArrayList
    public ArrayList<Trip> getTrips()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor tripsCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Trip> tripModelArrayList = new ArrayList<>();

        if (tripsCursor.moveToFirst())
        {
            do
            {
                tripModelArrayList.add(new Trip(
                        tripsCursor.getInt(0),
                        tripsCursor.getString(1),
                        tripsCursor.getString(2),
                        tripsCursor.getString(3),
                        tripsCursor.getString(4),
                        tripsCursor.getInt(5)
                ));
            }
            while (tripsCursor.moveToNext());
        }

        tripsCursor.close();

        db.close();

        return tripModelArrayList;
    }

    // getTrip method used for retrieving a single trip from the trip table based on the id
    public Trip getTrip(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst())
        {

            return new Trip(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
        }

        cursor.close();

        db.close();

        return null;
    }

    // updateTrip method used for retrieving for updating trips in the trip table based on the id
    public void updateTrip(
            int id,
            String destination,
            String startDate,
            String endDate,
            String notes,
            int review
    )
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        putTripValues(destination, startDate, endDate, notes, review, values);

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});

        db.close();
    }

    // deleteTrip method used for deleting trips from the trip table based on the id
    public void deleteTrip(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});

        db.close();
    }

    // onUpgrade method used for updating the database version, or drop tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    // putTripValues method used for putting values into the columns of the trip table
    private static void putTripValues(
            String destination,
            String startDate,
            String endDate,
            String notes,
            int review,
            ContentValues values
    )
    {
        values.put(COLUMN_DESTINATION, destination);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_NOTES, notes);
        values.put(COLUMN_REVIEW, review);
    }
}
