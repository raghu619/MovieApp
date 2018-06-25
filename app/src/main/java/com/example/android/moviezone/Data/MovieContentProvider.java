package com.example.android.moviezone.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.android.moviezone.Data.MovieContract.MovieEntry.*;
import com.example.android.moviezone.R;

import java.net.URI;

import static com.example.android.moviezone.Data.MovieContract.MovieEntry.CONTENT_URI;
import static com.example.android.moviezone.Data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by raghvendra on 23/5/18.
 */

public class MovieContentProvider   extends ContentProvider {


   public  static  final int MOVIES=100;
   public  static  final int MOVIE_ID=101;
   private  static  final UriMatcher sUriMatcher=buildUriMatcher();


           public static UriMatcher buildUriMatcher(){

               UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
               uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TASKS,MOVIES);
               uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TASKS+"/#",MOVIE_ID);

               return uriMatcher;



           }


           private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {

        Context context =getContext();
        mMovieDbHelper=new MovieDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){

            case MOVIES:
                retCursor=db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.UnKnown_URI) + uri);



        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIES:

                // directory
                return "vnd.android.cursor.dir" + "/" +MovieContract.AUTHORITY+"/"+MovieContract.PATH_TASKS;
            case MOVIE_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" +MovieContract.AUTHORITY+"/"+MovieContract.PATH_TASKS;



            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.UnKnown_URI) + uri);

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db=mMovieDbHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){

            case MOVIES:
                long id=db.insert(TABLE_NAME,null,values);

                if(id>0){


                    returnUri= ContentUris.withAppendedId(CONTENT_URI,id);
                }
                else {

                    throw new android.database.SQLException(getContext().getString(R.string.Failed_to_insert) + uri);

                }
                break;

                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.UnKnown_URI) + uri);



        }

        getContext().getContentResolver().notifyChange(returnUri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db=mMovieDbHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        int tasksDeleted;
        switch (match){

            case MOVIE_ID:
                String id=uri.getPathSegments().get(1);
                tasksDeleted=db.delete(TABLE_NAME,"_id=?",new String[]{id});
                break;

            default:

                throw new  UnsupportedOperationException(getContext().getString(R.string.UnKnown_URI)+uri);




        }


        if(tasksDeleted!=0){

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int  tasksUpdated;
        int match=sUriMatcher.match(uri);
        switch (match) {

            case MOVIE_ID:
            String id = uri.getPathSegments().get(1);
            tasksUpdated = mMovieDbHelper.getWritableDatabase().update(TABLE_NAME, values, "_id=?", new String[]{id});
            break;


            default:
                throw  new UnsupportedOperationException(getContext().getString(R.string.Not_yet_implemented));


        }

        if(tasksUpdated!=0){
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return  tasksUpdated;



    }
}
