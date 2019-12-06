package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<String> reviewsList;

    public ArrayList<String> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(ArrayList<String> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_review_list;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String reviewComment = reviewsList.get(position);
        holder.bind(reviewComment);
    }

    @Override
    public int getItemCount() {
        if (reviewsList == null){
            return 0;
        }
        return reviewsList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        private TextView listItemReviews;

        public ReviewViewHolder(View itemView){
            super(itemView);
            listItemReviews = itemView.findViewById(R.id.review_comment_tv);
        }

        public void bind(String reviewComment){
            listItemReviews.setText(reviewComment);
        }
    }
}
