package com.example.android.quakereport;

/**
 * Created by monash on 10/27/2016.
 */

public class Earthquake{


    private String mMagnitude;
    private String mCity;
    private String mDate;

    /**
     *
     * @param magnitude represents the magnitude of earthquake
     * @param city represents the city it occured in
     * @param date represents the date of earthquake
     */

    public Earthquake(String magnitude, String city, String date){
        mMagnitude = magnitude;
        mCity = city;
        mDate = date;
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
     * returns the date the earthquake occured
     * @return
     */
    public String getEarthquakeDate(){
        return mDate;
    }
}
