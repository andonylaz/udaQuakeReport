package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

        TextView date_text = (TextView)convertView.findViewById(R.id.date);
        date_text.setText(quake.getEarthquakeDate());

        // return the completed view to render on screen
        return convertView;
    }
}
