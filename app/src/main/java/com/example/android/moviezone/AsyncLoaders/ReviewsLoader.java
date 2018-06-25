package com.example.android.moviezone.AsyncLoaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.moviezone.Json_Utils.ReviewsQueryUtils;
import com.example.android.moviezone.Movie_DATA.Review_DATA;

import java.util.List;

/**
 * Created by raghvendra on 7/6/18.
 */

public class ReviewsLoader extends AsyncTaskLoader<Object> {

    private  String murl;
    public ReviewsLoader(Context context,String url) {
        super(context);
        this.murl=url;
    }
    Object mfetchingdata=null;

    @Override
    protected void onStartLoading() {
        if (mfetchingdata != null) {


            deliverResult(mfetchingdata);
        } else {

            forceLoad();
        }
    }

    @Override
    public Object loadInBackground() {
        try{
            Thread.sleep(2000);

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(murl==null) {
            return null;
        }
        List<Review_DATA> result= ReviewsQueryUtils.fetchingTrailer_data(murl);

        return result;
    }
}
