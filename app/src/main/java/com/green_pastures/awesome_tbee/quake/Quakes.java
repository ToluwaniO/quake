package com.green_pastures.awesome_tbee.quake;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tbee on 3/1/17.
 */

public class Quakes {
    String location, distance, url;
    String time, date;
    double magnitude;
    Date dateObj;
    SimpleDateFormat simpleDateFormat;
    String separator = " of ";

    public Quakes(String location, long time, double magnitude, String url) {
        dateObj = new Date(time);
        simpleDateFormat = new SimpleDateFormat("MMM DD, yyyy");

        String[] loc = location.split(separator);

        if(loc.length > 1) {
            this.distance = loc[0] + separator;
            this.location = loc[1];
        }
        else{
            this.distance = "0 KM of ";
            this.location = location;
        }
        this.date = simpleDateFormat.format(dateObj);
        this.time = "2:45";
        this.magnitude = magnitude;
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getDistance(){
        return distance;
    }

    public String getUrl() {
        return url;
    }
}
