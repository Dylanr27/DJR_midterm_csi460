package com.example.djr_midterm_csi460.trip_management;

import com.example.djr_midterm_csi460.DateUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

// This class defines the database helper for the application, it creates the database and tables,
// and provides methods for CRUD operations.
public class DBHelper extends SQLiteOpenHelper
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

    // Constructor for creating a new DBHelper object
    public DBHelper(Context context)
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
    // because id is auto-incremented it is not included,
    // we take in the dates as strings since they come from the text views,
    // and then parse them into Dates to be used in the creation
    public void createTrip(
            String destination,
            String startDate,
            String endDate,
            String notes,
            int review
    ) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        putTripValues(
                destination,
                DateUtils.parseDate(startDate),
                DateUtils.parseDate(endDate),
                notes,
                review,
                values
        );

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    // getTrips method used for creating trips using the data from the columns in the trip table,
    // adding them to an ArrayList, then sorting them based on the endDate, and finally returning
    // the ArrayList
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
                        new Date(tripsCursor.getLong(2)),
                        new Date(tripsCursor.getLong(3)),
                        tripsCursor.getString(4),
                        tripsCursor.getInt(5)
                ));
            }
            while (tripsCursor.moveToNext());
        }

        tripsCursor.close();

        db.close();

        tripModelArrayList.sort(
                (endDate1, endDate2) -> endDate2.getEndDate().compareTo(endDate1.getEndDate()));

        return tripModelArrayList;
    }

    // getTrip method used for creating a single trip using the data
    // from the trip table based on the id, and then returning the trip
    public Trip getTrip(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor tripsCursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        if (tripsCursor.moveToFirst())
        {

            return new Trip(
                    tripsCursor.getInt(0),
                    tripsCursor.getString(1),
                    new Date(tripsCursor.getLong(2)),
                    new Date(tripsCursor.getLong(3)),
                    tripsCursor.getString(4),
                    tripsCursor.getInt(5)
            );
        }

        tripsCursor.close();

        db.close();

        return null;
    }

    // updateTrip method used for retrieving a trip in the trip table based on the id
    // we take in the dates as strings since they come from the text views,
    // and then parse them into Dates to be used in the update
    public void updateTrip(
            int id,
            String destination,
            String startDate,
            String endDate,
            String notes,
            int review
    )
    throws ParseException
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        putTripValues(
                destination,
                DateUtils.parseDate(startDate),
                DateUtils.parseDate(endDate),
                notes,
                review,
                values
        );

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
            Date startDate,
            Date endDate,
            String notes,
            int review,
            ContentValues values
    )
    {
        values.put(COLUMN_DESTINATION, destination);
        values.put(COLUMN_START_DATE, startDate.getTime());
        values.put(COLUMN_END_DATE, endDate.getTime());
        values.put(COLUMN_NOTES, notes);
        values.put(COLUMN_REVIEW, review);
    }
}
