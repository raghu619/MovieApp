package com.example.android.moviezone;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviezone.AsyncLoaders.DATA_Loader;
import com.example.android.moviezone.AsyncLoaders.Favorite_data_loader;
import com.example.android.moviezone.Data.MovieContract;
import com.example.android.moviezone.Movie_DATA.DATA;
import com.example.android.moviezone.adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity    implements LoaderManager.LoaderCallbacks<Object>,SharedPreferences.OnSharedPreferenceChangeListener{


    LoaderManager.LoaderCallbacks<Cursor> mCursorLoderManager;
    private static final int MOVIE_LOADER_ID = 1;
    private List<DATA>movieslist;
    private static  final int MOVIE_CURSOR_LOADER=2;
       private TextView memptytextview;
    private SharedPreferences msharedPefs;
    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;

    private SwipeRefreshLayout swipeContainer;
    private  View mloadingIndicator;
    private static final String API_KEY=BuildConfig.API_KEY;
    public static final String JSON_RESPONSE="https://api.themoviedb.org/3/movie?api_key="+API_KEY;

    private static   boolean PREFERENCES_HAVE_BEEN_UPDATED=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =  findViewById( R.id.recycler_view );
        mloadingIndicator = findViewById(R.id.loading_indicator);
        memptytextview=findViewById(R.id.empty_view);
        mAdapter=new MovieAdapter(this);



        msharedPefs= PreferenceManager.getDefaultSharedPreferences(this);
        mCursorLoderManager=new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new Favorite_data_loader(getApplicationContext());
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mloadingIndicator = findViewById(R.id.loading_indicator);
                mloadingIndicator.setVisibility(View.GONE);

                if(data !=null) {
                    movieslist = getting_favorite_details(data);
                    mAdapter.setMoviesListValues(null);
                    mAdapter.setMoviesListValues(movieslist);


                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

                mAdapter.setMoviesListValues(null);

            }
        };
        swipeContainer =  findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){

                display();
                Toast.makeText(MainActivity.this, getString(R.string.REFRESHING_MOVIES), Toast.LENGTH_SHORT).show();
            }
        });

        display();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.action_settings)    {



            Intent settingsIntent=new Intent(this,SettingActivity.class);

            startActivity(settingsIntent);


            return true;
        }


        return super.onOptionsItemSelected(item);
    }

       public void  display() {

    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    } else {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

    }
    recyclerView.setItemAnimator(new DefaultItemAnimator());



    if (ISOnline()) {


        String favorite_sel = msharedPefs.getString(getString(R.string.settings_sort_by_key), getString(R.string.settings_sort_by_default));
        if (favorite_sel.equals(getString(R.string.setting_favorite_value))) {

            calling_favorites_view();
                  }
        else {
            if(favorite_sel.equals(getString(R.string.setting_sort_by_popular_value))){

                this.setTitle(R.string.setting_sort_by_popular_label);

            }
            else {
                this.setTitle(R.string.setting_sort_by_top_rated_label);

            }



            recyclerView.setAdapter(mAdapter);
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
            mAdapter.notifyDataSetChanged();
        }

    }
    else {


        String favorite_sel = msharedPefs.getString(getString(R.string.settings_sort_by_key), getString(R.string.settings_sort_by_default));
        if (favorite_sel.equals(getString(R.string.setting_favorite_value))) {

            calling_favorites_view();
        }

        else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            memptytextview.setVisibility(View.VISIBLE);
            memptytextview.setText(R.string.no_internet_connection);
            if (swipeContainer.isRefreshing()){
                mAdapter.setMoviesListValues(null);
                swipeContainer.setRefreshing(false);
            }

        }
    }
}

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {


        String sortby=msharedPefs.getString(getString(R.string.settings_sort_by_key),getString(R.string.settings_sort_by_default));

        Uri baseUri=Uri.parse(JSON_RESPONSE);
        Uri.Builder uriBuilder= baseUri.buildUpon();

        uriBuilder.appendPath(sortby);




        return new DATA_Loader(this,uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

        mloadingIndicator.setVisibility(View.GONE);


        if(data!=null){

          movieslist=(ArrayList<DATA>)data;
            mAdapter.setMoviesListValues(null);
          mAdapter.setMoviesListValues(movieslist);
          memptytextview.setVisibility(View.INVISIBLE);
            if (swipeContainer.isRefreshing()){

                swipeContainer.setRefreshing(false);
            }



      }

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
    mAdapter.setMoviesListValues(null);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED=true;
    }



    @Override
    protected void onStart() {
        super.onStart();

        if(PREFERENCES_HAVE_BEEN_UPDATED){

            memptytextview.setVisibility(View.INVISIBLE);
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            msharedPefs= PreferenceManager.getDefaultSharedPreferences(this);
            String value=msharedPefs.getString(getString(R.string.settings_sort_by_key),getString(R.string.settings_sort_by_default));
            if(value.equals(getString(R.string.setting_favorite_value))){

                  this.setTitle(R.string.setting_favorite_label);
                getLoaderManager().restartLoader(MOVIE_CURSOR_LOADER,null,mCursorLoderManager);
                PREFERENCES_HAVE_BEEN_UPDATED = false;
            }
            else {
//
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                PREFERENCES_HAVE_BEEN_UPDATED = false;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    public void onResume(){

        super.onResume();

        String value=msharedPefs.getString(getString(R.string.settings_sort_by_key),getString(R.string.settings_sort_by_default));
        if(value.equals(getString(R.string.setting_favorite_value)))
                getLoaderManager().restartLoader(MOVIE_CURSOR_LOADER,null,mCursorLoderManager);

    }


    private void calling_favorites_view(){

        this.setTitle(R.string.setting_favorite_label);

        recyclerView.setAdapter(mAdapter);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(MOVIE_CURSOR_LOADER, null, mCursorLoderManager);
        mAdapter.notifyDataSetChanged();
        swipeContainer.setEnabled(false);

    }
    private boolean ISOnline(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){

            return  true;
        }
        else {
            return  false;
        }

    }

    public static  List<DATA> getting_favorite_details( Cursor cursor){

        List<DATA> data=new ArrayList<>();
        if(cursor!=null){


            int ColumnImageIndex=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE);
            int ColumnTitleIndex=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            int ColumnOverView=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            int ColumnRatings=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATINGS);
            int ColumnRelease=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE);
            int ColumnMovie_id=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);


            while (cursor.moveToNext()){


                String Image=cursor.getString(ColumnImageIndex);
                String Title=cursor.getString(ColumnTitleIndex);
                String Overview=cursor.getString(ColumnOverView);
                String Ratings=cursor.getString(ColumnRatings);
                String Release=cursor.getString(ColumnRelease);
                String movie_id=cursor.getString(ColumnMovie_id);




                data.add(new DATA(Title,Release,Image,Ratings,Overview,movie_id));


            }

        }

        return data;
    }



}
