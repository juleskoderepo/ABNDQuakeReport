package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
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

        // Get the magnitude and format it to one decimal point
        DecimalFormat formatter = new DecimalFormat("0.0");
        String formattedMagnitude = formatter.format(currentEq.getMagnitude());
        // Set the magnitude on the TextView
        magTV.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTV.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEq.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Find the TextView with the ID eq_loc_offset
        TextView offsetView = listItemView.findViewById(R.id.eq_loc_offset);

        // Parse location offset from the place value or use "Near the" as the offset
        // place value for the current earthquake as returned in the JSON response
        final String placeValue = currentEq.getLocation();

        // length of the place value string
        final int placeValueLen = placeValue.length();

        // index value for the string " of " in the offset string
        int offsetIndex = placeValue.indexOf(" of ");

        // location offset string
        String locOffset;

        // primary location string
        String primLoc;

        if(offsetIndex != -1){
            locOffset = placeValue.substring(0, offsetIndex+4);
        } else {
            locOffset = "Near the";
        }

        // Display the location offset in the TextView
        offsetView.setText(locOffset);

        // Find the TextView with the ID eq_primary_loc
        TextView primaryLocView = listItemView.findViewById(R.id.eq_primary_loc);

        // Parse primary location from the place value
        if(offsetIndex == -1){
            primLoc = placeValue;
        } else {
            primLoc = placeValue.substring(offsetIndex+4, placeValueLen);
        }

        // Display the primary location in the TextView
        primaryLocView.setText(primLoc);

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

    /**
     * Return color value based on the current earthquake's magnitude.
     */

    private int getMagnitudeColor(double magnitude){

        int magInt = (int)magnitude;
        int magnitudeColor;

        switch(magInt){
            case 0:
            case 1:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude1);
                break;
            case 2:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude2);
                break;
            case 3:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude3);
                break;
            case 4:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude4);
                break;
            case 5:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude5);
                break;
            case 6:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude6);
                break;
            case 7:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude7);
                break;
            case 8:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude8);
                break;
            case 9:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude9);
                break;
            default:
                magnitudeColor = ContextCompat.getColor(getContext(),R.color.magnitude10plus);
                break;
        }

        return magnitudeColor;
    }
}

