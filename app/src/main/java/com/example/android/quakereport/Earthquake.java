package com.example.android.quakereport;

/**
 * Created by monash on 10/27/2016.
 */

public class Earthquake{


    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    /**
     *
     * @param magnitude represents the magnitude of earthquake
     * @param location represents the city it occured in
     * @param time represents the date of earthquake
     * @param url represents the url of the earthquake on the USGS website
     */

    public Earthquake(double magnitude, String location, long time, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = time;
        mUrl = url;
    }

    /**
     * Returns the magnitude of the earthquake
     * @return
     */
    public double getEarthquakeMagnitude(){
        return mMagnitude;
    }

    /**
     * returns the city hit by the earthquake
     * @return
     */
    public String getEarthquakeLocation(){
        return mLocation;
    }


    /**
     * returns the time the earthquake occured
     * @return
     */
    public long getEarthquakeTime() { return mTimeInMilliseconds;}

    /**
     * returns the url from USGS for the earthquake
     * @return
     */
    public String getEarthquakeUrl() { return mUrl;}
}
