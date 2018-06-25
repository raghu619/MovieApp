package com.example.android.moviezone;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.moviezone.AsyncLoaders.ReviewsLoader;
import com.example.android.moviezone.AsyncLoaders.TrailersLoader;
import com.example.android.moviezone.Data.MovieContract;
import com.example.android.moviezone.Movie_DATA.DATA;
import com.example.android.moviezone.Movie_DATA.Review_DATA;
import com.example.android.moviezone.Movie_DATA.Trailer_DATA;
import com.example.android.moviezone.adapters.ReviewsAdapter;
import com.example.android.moviezone.adapters.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
    private static final String IMG_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String API_KEY=BuildConfig.API_KEY;
    public static final String JSON_RESPONSE="https://api.themoviedb.org/3/movie?api_key="+API_KEY;
    private  static ProgressBar mprogressbar;

    private ImageView mImage_view;
    private TextView mtitle_view;
    private TextView mrelease_view;
    private TextView mrating_view;
    private TextView mplot_view;
   private ToggleButton toggleButton;
   private List<Trailer_DATA> mtrailerlist;
   private List<Review_DATA> mreviewslist;
   private RecyclerView recyclerView1;
   private TrailerAdapter mTrailerAdapter;
   private  static  final int Trailer_Loader_Id=1;
   private static  final  int Review_Loader_id=2;
   private  RecyclerView recyclerView2;
   private ReviewsAdapter mReviewsAdapter;
   

   private DATA mDATA;

   private  String MovieId;

   private SharedPreferences msharedPefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String name = DetailActivity.class.getSimpleName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toggleButton = findViewById(R.id.myToggleButton);
        msharedPefs= PreferenceManager.getDefaultSharedPreferences(this);


        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final StringBuilder builder = new StringBuilder();
        builder.append(IMG_BASE_URL);
        Bundle extras = getIntent().getExtras();

        mtitle_view = findViewById(R.id.title_view);
        mImage_view = findViewById(R.id.imageview);
        mrelease_view = findViewById(R.id.date_view);
        mrating_view = findViewById(R.id.rating_view);
        mplot_view = findViewById(R.id.plot_view);
        if (extras != null) {
            mDATA = (DATA) extras.getSerializable(getString(R.string.sending_data));
            builder.append(mDATA.image);


            Log.v("DetailActivity",builder.toString());


            Picasso.with(this).load(builder.toString()).into(mImage_view);
            mtitle_view.setText(mDATA.movie_title);
            mrelease_view.setText(mDATA.movie_release);
            mrating_view.setText(mDATA.movie_vote);
            mplot_view.setText(mDATA.movie_plot);
            trailerView();
            Review_display();



            // do something with the customer
        }

if(Is_Favorite()){

    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.btn_star_on_disabled_focused_holo_dark));
    toggleButton.setChecked(true);
}

else {

    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.btn_star_off_disabled_holo_light));
 toggleButton.setChecked(false);
}

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    try {


                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_star_on_disabled_focused_holo_dark));
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, mDATA.image);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mDATA.movie_plot);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_RATINGS, mDATA.movie_vote);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE, mDATA.movie_release);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mDATA.movie_title);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,mDATA.movie_id);
                        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                        if (uri != null) {
                            Toast.makeText(getBaseContext(),  getString(R.string.Added_to_Favorite), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){

                        Toast.makeText(getBaseContext(),getString(R.string.Already_Added),Toast.LENGTH_LONG).show();
                    }

                } else {


                    Is_Favorite();
                      if(!MovieId.isEmpty()) {

                          Uri currentMovieUri= ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,Integer.parseInt(MovieId));
                          getContentResolver().delete(currentMovieUri,null,null);

                          Toast.makeText(getBaseContext(),getString(R.string.Removed_From_Favorite),Toast.LENGTH_LONG).show();
                          toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_star_off_disabled_holo_light));
                      }


                }
            }
        });


    }




    private boolean Is_Favorite(){

         boolean present=false;
            try {
                Cursor check = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, MovieContract.MovieEntry.COLUMN_TITLE + "=?", new String[]{mDATA.movie_title}, null);
                check.moveToFirst();
                if(check.getCount()!=0) {
                    present = true;
                    MovieId=check.getString(check.getColumnIndex(MovieContract.MovieEntry._ID));
                    Log.v("Checking Id Data", mDATA.movie_id);
                }

                check.close();
            }

            catch (Exception e){

                Log.v("Checking Id Data",String.valueOf(present));

            }

        return present;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){

                Intent mainIntent = new Intent(this, MainActivity.class);
                String value= msharedPefs.getString(getString(R.string.settings_sort_by_key),getString(R.string.settings_sort_by_default));

                    if(value.equals(getString(R.string.setting_favorite_value)))
                    {
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity(mainIntent);

                    }
                    else {
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(mainIntent);
                    }
                }




        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if(id==Trailer_Loader_Id) {
            Uri baseUri = Uri.parse(JSON_RESPONSE);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendPath(mDATA.movie_id);
            uriBuilder.appendPath(getString(R.string.PATH_VIDEOS));

            return new TrailersLoader(this, uriBuilder.toString());
        }
        else {
            Uri baseUri = Uri.parse(JSON_RESPONSE);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendPath(mDATA.movie_id);
            uriBuilder.appendPath(getString(R.string.PATH_REVIWS));


            return new ReviewsLoader(this,uriBuilder.toString());

        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

        if(loader.getId()==Trailer_Loader_Id) {
            if (data != null) {

                mtrailerlist = (ArrayList<Trailer_DATA>) data;
                mTrailerAdapter.set_Trailer_data(mtrailerlist);
                mprogressbar.setVisibility(View.INVISIBLE);
            }
        }
        else {
            if(data!=null){
                mreviewslist=(ArrayList<Review_DATA>)data;
                mReviewsAdapter.setReviewsList(mreviewslist);

            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
        mTrailerAdapter.set_Trailer_data(null);
    }


    public void  trailerView(){
        mprogressbar=findViewById(R.id.loading_indicator1);
        recyclerView1=findViewById(R.id.recycler_view1);
         mTrailerAdapter=new TrailerAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView1.setLayoutManager(mLayoutManager);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(trailerLayoutManager);
        recyclerView1.setAdapter(mTrailerAdapter);
        mTrailerAdapter.notifyDataSetChanged();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(Trailer_Loader_Id, null, this);


    }


    public void Review_display(){
        recyclerView2 =  findViewById(R.id.recycler_view2);
        mReviewsAdapter=new ReviewsAdapter(this);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(reviewLayoutManager);
        recyclerView2.setAdapter( mReviewsAdapter);
        mReviewsAdapter.notifyDataSetChanged();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(Review_Loader_id, null, this);







    }
}
