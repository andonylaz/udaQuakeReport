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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    //public static final String LOG_TAG = EarthquakeActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations. --- OLD CODE BUT GOOD REFERENCE
        /**
         * This is why I could not just use earthquake.add(etc, etc etc,):
         *
         * Because that would mean that I am trying to add a new earthquake
         * element to the array list without calling the constructor to create
         * a new instance of that object.
         * When what I needed to do was create a different
         * instance of the Earthquake class for each element in the array list.
         * That is why I am using the constructor new Earthquake(x, y, z) inside
         * the add(parenthesis), and the add belongs to the ArrayList class.
         */

        // create new instance of EarthquakeAsyncClass
        EarthquakeAsyncTask earthquakeTask = new EarthquakeAsyncTask();
        earthquakeTask.execute();

    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList>{
        @Override
        protected void onPostExecute(ArrayList arrayList) {
            // update the list of earthquakes
            updateEarthquakeList(arrayList);
        }

        @Override
        protected ArrayList doInBackground(String... params) {

            try {// open the connection and retrieve the data
                ArrayList<Earthquake> recentEarthquakes = QueryUtils.fetchEarthquakeData(QueryUtils.USGS_URL);
                // return the list to postExecute method
                return recentEarthquakes;
            } catch (Exception e){
                e.printStackTrace();

            }

            // return the list to postExecute method
            return null;


        }
    }

    private void updateEarthquakeList(ArrayList earthquakes){

        // creates a new ArrayList of Earthquake data types
        ArrayList<Earthquake> recentEarthquakes = new ArrayList<>(earthquakes);


        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        final EarthquakeAdapter myAdapter = new EarthquakeAdapter(
                this, recentEarthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(myAdapter);

        // set an onItemClickListener for the listView
        // so when a user clicks on the ListView this happens:
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
