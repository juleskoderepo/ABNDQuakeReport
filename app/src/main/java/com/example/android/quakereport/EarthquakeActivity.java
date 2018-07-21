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
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private static final int EQ_LOADER_ID = 0;

    // Declare global adapter variable
    static EarthquakeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Prepare the loader
        getLoaderManager().initLoader(EQ_LOADER_ID, null, this);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter(
                this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

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
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(EarthquakeActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // exit early if no response
        if(earthquakes == null){
            return;
        }

        // update the UI
        updateUI(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
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
            forceLoad();
        }

        @Override
        public List<Earthquake> loadInBackground() {
            // return early if URL is null
            if(url == null){
                return null;
            }

            List<Earthquake> earthquakesList = QueryUtils.fetchEarthquakeData(url);

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
