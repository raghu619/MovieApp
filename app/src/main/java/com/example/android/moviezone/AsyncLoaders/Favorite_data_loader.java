package com.example.android.moviezone.AsyncLoaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.moviezone.Data.MovieContract;

/**
 * Created by raghvendra on 3/6/18.
 */

public class Favorite_data_loader extends AsyncTaskLoader<Cursor> {




    public Favorite_data_loader(Context context){

        super(context);



    }

    Cursor mTaskData = null;

    @Override
    protected void onStartLoading() {

        if (mTaskData != null) {


            deliverResult(mTaskData);
        } else {

            forceLoad();
        }

    }

    @Override
    public Cursor loadInBackground() {

        try {
            return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null
                    , null, null, null);
        } catch (Exception e) {

            Log.e("Main Fragment", "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }

    }


    public void deliverResult(Cursor data) {
        mTaskData = data;
        super.deliverResult(data);
    }





}
