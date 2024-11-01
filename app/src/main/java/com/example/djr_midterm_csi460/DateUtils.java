package com.example.djr_midterm_csi460;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// This class is used for parsing and formatting dates,
// we have sectioned this out to isolate the logic for handling dates for readability
public class DateUtils
{
    public static final String DATE_FORMAT = "EEE, MM/dd/yyyy";

    // this method is used for parsing a string to a date
    public static Date parseDate(String dateString) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormat.parse(dateString);
    }

    // this method is used for formatting a date to a string
    public static String formatDate(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormat.format(date);
    }
}
