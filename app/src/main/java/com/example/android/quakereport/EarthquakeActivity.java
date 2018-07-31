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
import android.content.AsyncTaskLoader;
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
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final int EQ_LOADER_ID = 0;
    //private static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

    private TextView emptyTV;
    private ProgressBar progressBar;

    // Declare global adapter variable
    static EarthquakeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        emptyTV = findViewById(R.id.empty_textview);
        progressBar = findViewById(R.id.loading_spinner);

        // Check for network connectivity
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            // Prepare the loader
            //Log.e(LOG_TAG, "initLoader method called");
            getLoaderManager().initLoader(EQ_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyTV.setText(getString(R.string.no_internet_connection));
        }


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter(
                this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        // Set the empty view on the {@link ListView) if the adapter is empty
        View emptyView  = (View)emptyTV;
        earthquakeListView.setEmptyView(emptyView);

        // open URL for select earthquake
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = adapter.getItem(position);
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(currentEarthquake.getWebsite()));
                startActivity(openURL);
            }
        });

    }

    @Override
    // This method initializes the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        // Log.e(LOG_TAG,"Creating new EarthquakeLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the
        // default value for this preference.
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it.
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, 'format=geojson'
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("eventtype","earthquake");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby","time");

        // Return the completed uri
        return new EarthquakeLoader(EarthquakeActivity.this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        //Log.e(LOG_TAG,"Loader work completed. onLoadFinished method called");

        progressBar.setVisibility(View.GONE);
        emptyTV.setText(getString(R.string.no_earthquakes_found));

        // exit early if no response
        if(earthquakes == null){
            return;
        }

        // update the UI
        updateUI(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Log.e(LOG_TAG,"Loader reset");
        // Loader reset so clear the adapter of previous earthquake data
        adapter.clear();

    }

    public static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>{

        private String url;

        private EarthquakeLoader(Context context, String requestUrl) {
            super(context);
            url = requestUrl;

        }

        @Override
        protected void onStartLoading() {
            // Log.e(LOG_TAG,"Start loading called");
            forceLoad();
        }

        @Override
        public List<Earthquake> loadInBackground() {
            // Log.e(LOG_TAG,"Load on background thread started");
            // return early if URL is null
            if(url == null){
                return null;
            }

            List<Earthquake> earthquakesList = null;
            earthquakesList = QueryUtils.fetchEarthquakeData(url);

            return earthquakesList;
        }
    }

    private static void updateUI(List<Earthquake> earthquakes){
        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if(earthquakes != null && !earthquakes.isEmpty()){
            adapter.addAll(earthquakes);
        }
    }

}
