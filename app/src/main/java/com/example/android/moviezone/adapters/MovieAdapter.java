package com.example.android.moviezone.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviezone.Movie_DATA.DATA;
import com.example.android.moviezone.DetailActivity;
import com.example.android.moviezone.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by raghvendra on 4/6/18.
 */

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{


    private Context mContext;
    private List<DATA>mMovieList;
    private static final String IMG_BASE_URL="https://image.tmdb.org/t/p/w500";


    public MovieAdapter(Context mContext){
        this.mContext=mContext;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StringBuilder builder=new StringBuilder();
        builder.append(IMG_BASE_URL);
        DATA movie=mMovieList.get(position);
        builder.append(movie.image);



        Picasso.with(mContext).load(builder.toString())
                .into(holder.iconView);
    }

    @Override
    public int getItemCount() {
        if(mMovieList!=null)
               return mMovieList.size();
        else
            return 0;
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
   public ImageView iconView;


        public MyViewHolder(View itemView) {
            super(itemView);
             iconView = itemView.findViewById(R.id.poster_image);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int position=getAdapterPosition();
                     if(position !=RecyclerView.NO_POSITION){

                         DATA sendingmovie=mMovieList.get(position);
                         Intent intent=new Intent(mContext,DetailActivity.class);
                         intent.putExtra(mContext.getString(R.string.sending_data),sendingmovie);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                         mContext.startActivity(intent);

                     }
                 }
             });

        }
    }


    public void setMoviesListValues(List<DATA>moviesListValues){

        this.mMovieList=moviesListValues;
        notifyDataSetChanged();
    }
}
