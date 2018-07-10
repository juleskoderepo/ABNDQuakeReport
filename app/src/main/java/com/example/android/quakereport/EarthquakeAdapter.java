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

import java.util.ArrayList;

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

        // Find the TextView in the earthquake_listing.xml layout with the ID eq_datetime
        TextView datetimeTV = listItemView.findViewById(R.id.eq_datetime);

        // Get the magnitude from the current Earthquake object and set the text on the TextView
        datetimeTV.setText(currentEq.getDateTime());

        return listItemView;
    }
}
