package com.example.android.quakereport;

/**
 * Created by monash on 11/1/2016.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String USGS_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=10";

    /**JSON response for a USGS query */
    private static String JSON_RESPONSE;

    // the edited url query that can be edited by the user
    public static String NEW_USGS_QUERY;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    /** Creates a URL object from a String variable
     *
     * @param urlString
     * @return URL object to be used in creating a HTTP connection
     */
    public static URL createURL(String urlString){
        URL url = null;
        // if the string is invalid
        if (urlString == "" || urlString == null){
            return null;
        }

        // Must be situated in try/catch block
        try {
            // create the url
            url = new URL(urlString);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error creating URL object ");
        }
        return url;
    }


    private static String makeHttpRequest(URL quakeUrl) throws IOException{
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (quakeUrl == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) quakeUrl.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = getDataFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String getDataFromStream(InputStream inputStream) throws IOException{
        // So we have the connection, now we need to read in from the stream
        // We need inputstream to handle input, buffered reader to parse our input
        // string builder to hold our current input
        StringBuilder progressString = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));

            // BufferedReader is reading and parsing the data from InputStreamReader,
            // InputStreamReader is receiving the binary data from the InputStream
            // InputStream is the "pipe" that allows the data to be transferred
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // keep reading and appending the JSON text until there is no more
            String line = reader.readLine();
            while (line != null) {
                progressString.append(line);
                line = reader.readLine();
            }
        }
        // still works from this point
        //Log.e(LOG_TAG, "At the end of getDataInputStream is still works" + progressString.toString());
        // returns the string
        return progressString.toString();

    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Earthquake> extractEarthquakes(String jsonResponse) {

        // if the response is empty then return straight away
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }


        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        Log.e(LOG_TAG, "After creating ArrayList in earthquakes in extractEarthquakes");
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            // build up a list of Earthquake objects with the corresponding data.
            // Create new JSON object by parsing in a string containing JSON formatted text
            JSONObject rootEarthquakeObject = new JSONObject(jsonResponse);

            // get the JSON array linked to the 'features' key
            JSONArray featuresArray = rootEarthquakeObject.getJSONArray("features");

            // loop through each feature in the array
             for (int i = 0; i < featuresArray.length(); i++){
                // get earthquake object at position i
                JSONObject earthquakeObject = featuresArray.optJSONObject(i);
                // get properties JSON object
                JSONObject propertiesObject = earthquakeObject.optJSONObject("properties");

                // extract mag for magnitude
                double magnitude = propertiesObject.optDouble("mag");
                // extract place for location
                String location = propertiesObject.optString("place");
                // extract time for time of quake
                long time = propertiesObject.optLong("time");
                // extract url for the UGSG website link
                String url = propertiesObject.optString("url");

                // create Earthquake java objects from magnitude, location, time data
                earthquakes.add(new Earthquake(magnitude, location, time, url));

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Query the USGS dataset and return a list of {@link Earthquake} objects.
     */
    public static final ArrayList<Earthquake> fetchEarthquakeData(String requestUrl) {

        // we are putting the thread calling this function "to sleep" for 2 seconds
        // this simulates a slow internet connection
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Create URL object
        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        Log.e(LOG_TAG, "Before extractEarthquake");
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        ArrayList<Earthquake> earthquakes = extractEarthquakes(jsonResponse);
        Log.e(LOG_TAG, "Successful completion of extractEarthquakes");
        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
}
