package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.R.id.date;
import static com.example.android.quakereport.R.id.magnitude;
import static com.example.android.quakereport.R.id.offset_location;
import static com.example.android.quakereport.R.id.primary_location;

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
        TextView magnitude_text = (TextView)convertView.findViewById(magnitude);
        // create instance of DoubleFormat with correct format ie (7.2, 5.1, etc)
        DecimalFormat magnitudeFormatter = new DecimalFormat("0.0");
        // format the earthquakes raw data value for magnitude using helper method
        String magntitudToDisplay = formatMagnitude(quake.getEarthquakeMagnitude());
        magnitude_text.setText(magntitudToDisplay);

        // Get the original location string from the Earthquake object,
        // which can be in the format of "5km N of Cairo, Egypt" or "Pacific-Antarctic Ridge".
        String originalLocation = quake.getEarthquakeLocation();

        // Create array and initialise the contents by calling the formatLocations() helper method
        String[] locationArray = formatLocations(originalLocation);

        // find and set the first location text view
        TextView primary_location_text = (TextView) convertView.findViewById(primary_location);
        primary_location_text.setText(locationArray[1]);
        // find and set the second location text view
        TextView offset_location_text = (TextView) convertView.findViewById(offset_location);
        offset_location_text.setText(locationArray[0]);

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


        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude_text.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(quake.getEarthquakeMagnitude());

        // set the background with the correct color
        magnitudeCircle.setColor(magnitudeColor);
        // return the completed view to render on screen
        return convertView;
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        // create instance of DoubleFormat with correct format ie (7.2, 5.1, etc)
        DecimalFormat magnitudeFormatter = new DecimalFormat("0.0");
        // format the earthquakes raw data value for magnitude
        String magntitudToDisplay = magnitudeFormatter.format(magnitude);
        // return proper formatted value
        return magntitudToDisplay;
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

    /**
     *
     * @param locations entire string that is stored in Earthquake object
     * @return array that contains pieces of the string in each index
     */
    private String[] formatLocations(String locations) {

        // declare array to return
        String[] locationArray= {"", ""};

        // declare format seperator
        final String FORMAT_SEPARATOR = " of ";

        // split string in two, and store in a string array
        if (locations.contains(FORMAT_SEPARATOR)) {
            // now offset-location is in index[0], primary location is in index[1]
            locationArray = locations.split(FORMAT_SEPARATOR);
            // add " of " after the offset location text
            String temp = locationArray[0] + FORMAT_SEPARATOR;
            locationArray[0] = temp;
        } else {
            locationArray[1] = locations;
            locationArray[0] = getContext().getString(R.string.near_the);

        }
        return locationArray;
    }

    private int getMagnitudeColor(double magnitude){

        ContextCompat context = new ContextCompat();

        int magnitudeValue = (int) magnitude;
        switch (magnitudeValue){
            case 0:
                return ContextCompat.getColor(getContext(), R.color.magnitude1);
            case 1:
                return ContextCompat.getColor(getContext(), R.color.magnitude1);
            case 2:
                return ContextCompat.getColor(getContext(), R.color.magnitude2);
            case 3:
                return ContextCompat.getColor(getContext(), R.color.magnitude3);
            case 4:
                return ContextCompat.getColor(getContext(), R.color.magnitude4);
            case 5:
                return ContextCompat.getColor(getContext(), R.color.magnitude5);
            case 6:
                return ContextCompat.getColor(getContext(), R.color.magnitude6);
            case 7:
                return ContextCompat.getColor(getContext(), R.color.magnitude7);
            case 8:
                return ContextCompat.getColor(getContext(), R.color.magnitude8);
            case 9:
                return ContextCompat.getColor(getContext(), R.color.magnitude9);
            case 10:
                return ContextCompat.getColor(getContext(), R.color.magnitude10plus);
            default:
                return ContextCompat.getColor(getContext(), R.color.magnitude10plus);


        }

    }
}
