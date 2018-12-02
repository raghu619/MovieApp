package com.example.android.moviezone.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by raghvendra on 22/5/18.
 */

public class MovieContract {


    public static  final  String AUTHORITY="com.example.android.moviezone.Data.MovieContentProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TASKS = "favorites";


    public static final class MovieEntry implements BaseColumns{


        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();



        public static final String TABLE_NAME = "favorites";

        public static  final  String COLUMN_IMAGE="image";
        public static  final String  COLUMN_TITLE="title";
        public static  final  String COLUMN_OVERVIEW="overview";

        public static  final  String COLUMN_RATINGS="ratings";
        public static final String COLUMN_RELEASE="release";
        public static final String COLUMN_MOVIE_ID="movie_id";


    }


}
