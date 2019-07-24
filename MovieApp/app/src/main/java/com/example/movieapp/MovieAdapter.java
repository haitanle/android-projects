package com.example.movieapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {

    private Movie mMovieItem;

    public MovieAdapter(Movie movieItem){
        mMovieItem = movieItem;
    }


    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        PosterViewHolder viewHolder = new PosterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position){
    }

    @Override
    public int getItemCount(){
        return 1;
    }


    class PosterViewHolder extends RecyclerView.ViewHolder{

        private ImageView listItemImageView;

        public PosterViewHolder(View itemView){
            super(itemView);
            listItemImageView = (ImageView) itemView.findViewById(R.id.tv_poster_image);
        }

        public void bind(String imgUrl){
            //todo: parse url string
            //listItemImageView.setImageURI(imgUrl);
        }
    }
}
