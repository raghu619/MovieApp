package com.example.android.moviezone.AsyncLoaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.moviezone.Json_Utils.TrailerQueryUtils;
import com.example.android.moviezone.Movie_DATA.Trailer_DATA;

import java.util.List;

/**
 * Created by raghvendra on 6/6/18.
 */

public class TrailersLoader extends AsyncTaskLoader<Object> {
    private  String murl;
    public TrailersLoader(Context context,String url) {
        super(context);
        murl=url;
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
        List<Trailer_DATA> result= TrailerQueryUtils.fetchingTrailer_data(murl);
        return result;
    }

    @Override
    public void deliverResult(Object data) {
        mfetchingdata=data;
        super.deliverResult(data);
    }
}
