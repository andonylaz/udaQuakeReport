package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by monash on 11/10/2016.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>>{


    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /** Query URL */
    private String mUrl;


    // constructor
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.e(LOG_TAG, "In the loaders constructor");
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG, "Inside the onStartLoading() method");
    }

    /**
     *
     * @return a recently updated ArrayList of Earthquake objects from the server
     */
    @Override
    public ArrayList<Earthquake> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        Log.e(LOG_TAG, "In the loadInBackground() method");

        try {// open the connection and retrieve the data
            ArrayList<Earthquake> recentEarthquakes = QueryUtils.fetchEarthquakeData(QueryUtils.USGS_URL);
            // return the list to postExecute method
            return recentEarthquakes;
        } catch (Exception e){
            e.printStackTrace();

        }
        // keep compiler happy as I do not have access to "recent earthquakes" from this scope
        return null;
    }
}
