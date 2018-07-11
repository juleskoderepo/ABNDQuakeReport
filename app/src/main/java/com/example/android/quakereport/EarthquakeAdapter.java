package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    /**
     * Custom constructor for a new EarthquakeAdapter object.
     * The context is used to inflate the layout file and the list is the data we want to populate
     * into the lists.
     *
     * @param context
     * @param earthquakes
     */
    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_listing, parent, false);
        }

        // Get the {@link Earthquake} object located at this position in the list
        Earthquake currentEq = getItem(position);

        // Find the TextView in the earthquake_listing.xml layout with the ID eq_mag
        TextView magTV = listItemView.findViewById(R.id.eq_mag);

        // Get the magnitude from the current Earthquake object and set the text on the TextView
        magTV.setText(currentEq.getMagnitude());

        // Find the TextView in the earthquake_listing.xml layout with the ID eq_location
        TextView locationTV = listItemView.findViewById(R.id.eq_location);

        // Get the magnitude from the current Earthquake object and set the text on the TextView
        locationTV.setText(currentEq.getLocation());

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEq.getDateTime());

        // Find the TextView with view ID date
        TextView dateView = listItemView.findViewById(R.id.eq_date);

        // Format the date string
        String formattedDate = formatDate(dateObject);

        // Display the date in the TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = listItemView.findViewById(R.id.eq_time);

        // Format the time string
        String formattedTime = formatTime(dateObject);

        // Display the time in the TextView
        timeView.setText(formattedTime);

        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}
