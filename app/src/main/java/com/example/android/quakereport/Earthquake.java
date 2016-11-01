package com.example.android.quakereport;

/**
 * Created by monash on 10/27/2016.
 */

public class Earthquake{


    private String mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;

    /**
     *
     * @param magnitude represents the magnitude of earthquake
     * @param location represents the city it occured in
     * @param time represents the date of earthquake
     */

    public Earthquake(String magnitude, String location, long time){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = time;
    }

    /**
     * Returns the magnitude of the earthquake
     * @return
     */
    public String getEarthquakeMagnitude(){
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
}
