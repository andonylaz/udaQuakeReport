/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private EarthquakeAdapter myAdapter;
    private ListView mEarthquakeListView;
    TextView mEmptyView;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ArrayList<Earthquake> dummyData = new ArrayList<>();

        // Create a new {@link ArrayAdapter} of earthquakes
        myAdapter = new EarthquakeAdapter(this, dummyData);

        mEarthquakeListView = (ListView) findViewById(R.id.list);

        // hide the list view to show the ProgressBar
        // mEarthquakeListView.setVisibility(View.INVISIBLE);

        // get reference to and then hide the progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(VISIBLE);


        // check if there is an internet connection by first creating a way to use
        // the connectivity manager
        ConnectivityManager connectManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // See what type of connection there is
        NetworkInfo netInfo = connectManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()){
            // fetch data from network using loader

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else{
            // set the empty TextView to notify the user there is no internet connection

            // hide the progress bar
            progressBar.setVisibility(GONE);

            // get a reference for the empty view
            mEmptyView = (TextView) findViewById(R.id.no_data_available);
            mEmptyView.setText(R.string.no_internet_connection);
            //Set the empty state text view onto the list view
            mEarthquakeListView.setEmptyView(mEmptyView);
        }



    }



    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        Log.e(LOG_TAG, "Called onCreateLoader");
        //return new EarthquakeLoader(this, QueryUtils.USGS_URL);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(QueryUtils.USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        // save the new URI to the static variable in QueryUtils
        QueryUtils.NEW_USGS_QUERY = uriBuilder.toString();

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    /** This method is automatically called once the loadInBackground() completes pasing
     * the those parameters
     *
     * @param loader the instance of the loader that was used
     * @param data an ArrayList that contains recent Earthquake data
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {

        // get reference to and then hide the progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(GONE);



        Log.e(LOG_TAG, "In onLoadFinished() method");
        // update the list of earthquakes
        updateEarthquakeList(data);

        // Clear the adapter of previous earthquake data
        if (data != null) {
            myAdapter.clear();
        }
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            myAdapter.addAll(data);
        }

        // get a reference for the empty view
        mEmptyView = (TextView) findViewById(R.id.no_data_available);
        mEmptyView.setText(R.string.unavilable_data);
        //Set the empty state text view onto the list view
        mEarthquakeListView.setEmptyView(mEmptyView);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {

        if (loader != null) {
            myAdapter.clear();
        }
        Log.e(LOG_TAG, "In onLoaderReset() method");
    }

    private void updateEarthquakeList(ArrayList earthquakes){

        // if there is not list of objects
        if (earthquakes == null){
            return;
        }

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEarthquakeListView.setAdapter(myAdapter);

        // set an onItemClickListener for the listView
        // so when a user clicks on the ListView this happens:
        mEarthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //parent	AdapterView: The AdapterView where the click happened.
                //view	View: The view within the AdapterView that was clicked
                // (this will be a view provided by the adapter)
                //position	int: The position of the view in the adapter.
                //id   long: The row id of the item that was clicked.

                // Find the current earthquake that was clicked on
                // we call the custom adapters getItem() method
                Earthquake earthquakeSelected = myAdapter.getItem(position);
                // get the url of that list item
                String earthquakeUrl = earthquakeSelected.getEarthquakeUrl();

                // open that url in a browser:
                // create new Intent object and pass it the ACTION_VIEW constant this means the
                // activity action will display data to the user.
                Intent openUrl = new Intent(Intent.ACTION_VIEW);
                // Set the data of the Intent to the URL but first we
                // convert the string URL into a URI object
                openUrl.setData(Uri.parse(earthquakeUrl));
                // start a new activity with the intent, so that a web browser app on the
                // device will handle the intent and display the website for that earthquake.
                startActivity(openUrl);
            }
        });
    }

}
