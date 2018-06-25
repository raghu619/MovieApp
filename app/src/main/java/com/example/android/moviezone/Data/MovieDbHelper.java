package com.example.android.moviezone.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.moviezone.Data.MovieContract.MovieEntry;

/**
 * Created by raghvendra on 22/5/18.
 */

public class MovieDbHelper extends SQLiteOpenHelper{


    private static final String DATABASE_NAME="favoritesDb.db";
    private static final int VERSION=1;
    MovieDbHelper(Context context){

    super(context,DATABASE_NAME,null,VERSION);

}

    @Override
    public void onCreate(SQLiteDatabase db) {
    final String CREATE_TABLE="CREATE TABLE "+ MovieEntry.TABLE_NAME+" ("+
            MovieEntry._ID+" INTEGER PRIMARY KEY, "+
            MovieEntry.COLUMN_IMAGE+" TEXT NOT NULL, "+
            MovieEntry.COLUMN_OVERVIEW+" TEXT NOT NULL, "+
            MovieEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
            MovieEntry.COLUMN_RELEASE+" TEXT NOT NULL, "+
            MovieEntry.COLUMN_RATINGS+" REAL NOT NULL, " +
            MovieEntry.COLUMN_MOVIE_ID+" TEXT NOT NULL "+");";


    db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS "+MovieEntry.TABLE_NAME);

        onCreate(db);
    }
}
