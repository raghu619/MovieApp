package com.example.android.moviezone.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.moviezone.R;
import com.example.android.moviezone.Movie_DATA.Trailer_DATA;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by raghvendra on 6/6/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{


    private Context mContext;
    private List<Trailer_DATA> trailerList;


    public TrailerAdapter(Context mContext){

        this.mContext=mContext;

    }
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {

        String videoClip = "https://img.youtube.com/vi/"+trailerList.get( position ).TrailerKey +"/0.jpg";

        Picasso.with(mContext)
                .load( videoClip )
                .into( holder.thumbnail );
    }

    @Override
    public int getItemCount() {

         if(trailerList!=null)
             return trailerList.size();
         else
             return 0;
    }

    public  class TrailerViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail;


        public TrailerViewHolder(View itemView) {

            super(itemView);
            thumbnail =(ImageView)itemView.findViewById(R.id.trailerImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        String videoId = trailerList.get(pos).TrailerKey;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(mContext.getString(R.string.movie_id), videoId);
                        mContext.startActivity(intent);


                    }
                }
            });

        }
    }

    public void set_Trailer_data(List<Trailer_DATA> data){

        this.trailerList=data;
        notifyDataSetChanged();

    }
}
