package com.example.android.moviezone.Json_Utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.moviezone.Movie_DATA.Review_DATA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by raghvendra on 7/6/18.
 */

public class ReviewsQueryUtils  {
    public static final String LOG_TAG = ReviewsQueryUtils.class.getSimpleName();

    private ReviewsQueryUtils() {

    }


    public static List<Review_DATA> fetchingTrailer_data(String request_url)

    {

        URL url = makeUrl(request_url);
        String jsonresponse = null;
        List<Review_DATA> k;
        try {
            jsonresponse = makehttprequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        k=extractData(jsonresponse);

        return k;
    }

    private static String makehttprequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {


            return jsonResponse;
        }


        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(150000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = reading_stream(inputStream);

            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());


            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {

            if (urlConnection != null) {

                urlConnection.disconnect();

            }
            if (inputStream != null) {


                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String reading_stream(InputStream inputStream) throws IOException {

        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        boolean hasInput = scanner.hasNext();
        String response = null;
        if (hasInput) {
            response = scanner.next();
        }
        scanner.close();
        return response;

    }


    private static URL makeUrl(String request_url) {
        URL url = null;
        try {
            url = new URL(request_url);

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Error with creating URL ", e);

        }

        return url;
    }


    private static List<Review_DATA> extractData(String json) {


        if (TextUtils.isEmpty(json)) {


            return null;
        }
        List<Review_DATA> movie_info = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(json);
            JSONArray result_array = root.getJSONArray("results");
            int array_length = result_array.length();
            for (int i = 0; i < array_length; i++) {

                JSONObject root1 = result_array.getJSONObject(i);
                String author=root1.optString("author","NONE");
                String content=root1.optString("content","NONE");
                movie_info.add(new Review_DATA(author,content));
                Log.v(LOG_TAG,content);

            }


        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the Movie JSON results", e);
        }


        return movie_info;
    }

}
