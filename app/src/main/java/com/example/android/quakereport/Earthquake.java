package com.example.android.quakereport;

import java.util.Date;

/**
 * {@link Earthquake} represents an occurrence of an earthquake. It contains
 * a magnitute, location, and time.
 */
public class Earthquake  {

    /**
     * Magnitude of an earthquake.
     */
    private double Magnitude;

    /**
     * The location closest to where the earthquake occurred.
     */
    private String Location;

    /**
     * Date and time when the earthquake occurred.
     */
    private long DateTime;

    /**
     * URL on the USGS website for the specific earthquake
     */
    private String Website;

    /**
     * Contructs a new Earthquake object with a magnitude, location, date/time, and URL.
     *
     * @param magnitude magnitude of the earthquake.
     * @param location location closest to the earthquake epicenter.
     * @param timeInMillisecs time in milliseconds of when the earthquake occurred.
     * @param url USGS website for the specific earthquake
     */
    public Earthquake(double magnitude, String location, long timeInMillisecs, String url){
        Magnitude = magnitude;
        Location = location;
        DateTime = timeInMillisecs;
        Website = url;
    }

    /**
     * Gets the magnitude of the earthquake.
     *
     * @return Magnitude to display in the app.
     */
    public double getMagnitude(){return Magnitude;}

    /**
     * Gets the location of the earthquake.
     *
     * @return Location closest to the earthquake's epicenter to display in the app.
     */
    public String getLocation() {return Location;}

    /**
     * Gets the Date/time when the earthquake occurred.
     *
     * @return Date/time when the earthquake occurred.
     */
    public long getDateTime() {return DateTime;}

    /**
     * Gets the USGS web page for the specific earthquake
     * @return URL for the web page containing additional data for a specific earthquake
     */
    public String getWebsite() {return Website;}
}
