package com.example.android.moviezone.AsyncLoaders;

import android.content.Context;
import android.content.AsyncTaskLoader;

import com.example.android.moviezone.Movie_DATA.DATA;

import java.util.List;

import static com.example.android.moviezone.Json_Utils.Query_utils.fetching_data;

/**
 * Created by raghvendra on 29/4/18.
 */

public class DATA_Loader extends AsyncTaskLoader<Object> {
    private  String murl;
    public DATA_Loader(Context context,String url) {
        super(context);
        murl=url;
    }
    private Object mfetchingdata=null;
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
        List<DATA> result=fetching_data(murl);
        return result;
    }

    @Override
    public void deliverResult(Object data) {
        mfetchingdata=data;
        super.deliverResult(data);
    }
}
