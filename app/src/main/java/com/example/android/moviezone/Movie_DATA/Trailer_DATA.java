package com.example.android.moviezone.Movie_DATA;

import java.io.Serializable;

/**
 * Created by raghvendra on 6/6/18.
 */

public class Trailer_DATA implements Serializable {

    public String TrailerKey;

    public  String name;

    public Trailer_DATA ( String key, String name) {
        this.TrailerKey = key;
        this.name = name;
    }



}
