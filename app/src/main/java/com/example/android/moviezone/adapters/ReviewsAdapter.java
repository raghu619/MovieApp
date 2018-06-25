package com.example.android.moviezone.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviezone.R;
import com.example.android.moviezone.Movie_DATA.Review_DATA;

import java.util.List;

/**
 * Created by raghvendra on 7/6/18.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private Context mContext;
    private List<Review_DATA> reviewsList;



    public ReviewsAdapter(Context context){

        this.mContext=context;
    }
    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);


        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.mcontent.setText(reviewsList.get(position).Content);
        holder.mAuthor.setText(reviewsList.get(position).Author);
    }

    @Override
    public int getItemCount() {

        if(reviewsList!=null)
            return  reviewsList.size();
        else
           return 0;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{

        public TextView mcontent;
        public TextView mAuthor;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mcontent = (TextView) itemView.findViewById(R.id.review_content);
            mAuthor = (TextView) itemView.findViewById(R.id.review_author);
        }
    }


    public  void setReviewsList(List<Review_DATA> data){
        this.reviewsList=data;
        notifyDataSetChanged();

    }
}
