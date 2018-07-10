package com.example.android.quakereport;

/**
 * {@link Earthquake} represents an occurrence of an earthquake. It contains
 * a magnitute, location, and time.
 */
public class Earthquake  {

    /**
     * Magnitude of an earthquake.
     */
    private String Magnitude;

    /**
     * The location closest to where the earthquake occurred.
     */
    private String Location;

    /**
     * Date and time when the earthquake occurred.
     */
    private String DateTime;

    /**
     * Contructs a new Earthquake object with a magnitude, location and date/time.
     * @param magnitude magnitude of the earthquake.
     * @param location location closest to the earthquake epicenter.
     * @param dateTime when the earthquake occurred.
     */
    public Earthquake(String magnitude, String location, String dateTime){
        Magnitude = magnitude;
        Location = location;
        DateTime = dateTime;
    }

    /**
     * Gets the magnitude of the earthquake.
     *
     * @return Magnitude to display in the app.
     */
    public String getMagnitude(){return Magnitude;}

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
    public String getDateTime() {return DateTime;}
}
