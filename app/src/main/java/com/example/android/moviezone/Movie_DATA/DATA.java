package com.example.android.moviezone.Movie_DATA;

import java.io.Serializable;

/**
 * Created by poornima-udacity on 6/26/15.
 */
public class DATA implements Serializable {
 public    String movie_title;
    public String movie_release;
    public String movie_vote;
    public String movie_plot;
   public String image;
    public String detail_img;
    public String movie_id;// drawable reference id

    public DATA(String movie_title, String movie_release, String image,String movie_vote,
                String movie_plot,String detail_img,String movie_id)
    {
        this.movie_title = movie_title;
        this.movie_release=movie_release;
        this.image = image;
        this.movie_vote = movie_vote;
        this.movie_plot=movie_plot;
        this.detail_img=detail_img;
        this.movie_id=movie_id;


    }

    public DATA(String movie_title, String movie_release, String image,String movie_vote,  String movie_plot,String movie_id){

        this.movie_title = movie_title;
        this.movie_release=movie_release;
        this.image = image;
        this.movie_vote = movie_vote;
        this.movie_plot=movie_plot;
        this.movie_id=movie_id;

    }

}