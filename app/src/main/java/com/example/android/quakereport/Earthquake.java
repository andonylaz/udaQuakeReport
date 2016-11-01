package com.example.android.quakereport;

/**
 * Created by monash on 10/27/2016.
 */

public class Earthquake{


    private String mMagnitude;
    private String mCity;
    private long mTimeInMilliseconds;

    /**
     *
     * @param magnitude represents the magnitude of earthquake
     * @param city represents the city it occured in
     * @param time represents the date of earthquake
     */

    public Earthquake(String magnitude, String city, long time){
        mMagnitude = magnitude;
        mCity = city;
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
    public String getEarthquakeCity(){
        return mCity;
    }


    /**
     * returns the time the earthquake occured
     * @return
     */
    public long getEarthquakeTime() { return mTimeInMilliseconds;}
}
