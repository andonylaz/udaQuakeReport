package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.R.id.date;

/**
 * Created by monash on 10/31/2016.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the data for this position
        Earthquake quake = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        TextView magnitude_text = (TextView)convertView.findViewById(R.id.magnitude);
        magnitude_text.setText(quake.getEarthquakeMagnitude());

        TextView city_text = (TextView)convertView.findViewById(R.id.location);
        city_text.setText(quake.getEarthquakeCity());

        // get the unix time value for that particular object
        long unixTime = quake.getEarthquakeTime();
        // First convert the time in milliseconds into a Date object
        Date dateObject = new Date(unixTime);

        // find textview with id date
        TextView date_text = (TextView)convertView.findViewById(date);
        // call helper method to properly format date
        String dateToDisplay = formatDate(dateObject);
        // set the proper formatted date to the textview
        date_text.setText(dateToDisplay);

        // find textview with id time
        TextView time_text = (TextView)convertView.findViewById(R.id.time);
        // call helper method to properly format time
        String timeToDisplay = formatTime(dateObject);
        // set the proper formatted date to the textview
        time_text.setText(timeToDisplay);


        // return the completed view to render on screen
        return convertView;
    }


    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject){
        // initialize a SimpleDateFormat instance and configure it to provide a
        // more readable representation according to the given format
        SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMM, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormatter.format(dateObject);
        return timeToDisplay;
    }
}
