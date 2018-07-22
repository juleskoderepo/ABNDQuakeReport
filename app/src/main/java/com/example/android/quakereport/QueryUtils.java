package com.example.android.quakereport;


import android.content.Context;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the USGS dataset and return a {@link List} object of earthquakes
     *
     * @param requestUrl URL string to request data from the USGS
     * @return List of earthquake objects
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl){
        Log.e(LOG_TAG,"Fetching earthquake data.");

        // Simulate slow network response
        try{
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract fields from the JSON response and create a {@link List} object
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        // Return the {@link List}
        return earthquakes;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;

        try{
            url = new URL(stringUrl);
        } catch(MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating URL", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // if URL is null, then return early.
        if(url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream and parse response if the response was successful (code 200)
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        } catch(IOException e){
            Log.e(LOG_TAG,"Problem retrieving earthquake JSON results", e);
        } finally {
            // Release the connection
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            // Close the input stream. Closing the input stream could throw an IOException,
            // which is why the makeHttpRequest(URL url) method signature specifies that an
            // IOException could be thrown.
            if(inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the entire JSON response
     * from the server.
     * @param inputStream JSON response from the server
     * @return String containing entire JSON response
     * @throws IOException exception could be thrown processing the input stream
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a string containing the JSON response from the server.
     */
    private static List<Earthquake> extractEarthquakes(String jsonResponse) {
        // return early if JSON string is null
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject quakeResultsObject = new JSONObject(jsonResponse);
            JSONArray quakeFeaturesArray = quakeResultsObject.getJSONArray("features");

            for (int i=0; i < quakeFeaturesArray.length();i++){

                JSONObject quakeObject = (JSONObject) quakeFeaturesArray.get(i);
                JSONObject quakeProperties = quakeObject.getJSONObject("properties");

                double mag = quakeProperties.getDouble("mag");
                String place = quakeProperties.getString("place");
                long time = quakeProperties.getLong("time");
                String url = quakeProperties.getString("url");

                Earthquake quakeItem = new Earthquake(mag,place,time,url);

                earthquakes.add(quakeItem);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return earthquakes;
    }

}