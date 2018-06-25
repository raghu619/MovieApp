package com.example.android.moviezone.Movie_DATA;

import java.io.Serializable;

/**
 * Created by raghvendra on 7/6/18.
 */

public class Review_DATA  implements Serializable {

    public String Author;
    public String Content;
    public  Review_DATA(String Author,String Content){
        this.Author=Author;
        this.Content=Content;

    }

}
