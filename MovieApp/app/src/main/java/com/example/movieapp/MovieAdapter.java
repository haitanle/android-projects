package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {

    private JSONObject movieList;

    private Movie mMovieItem;

    private Movie[] moviesList;

//    public JSONObject getMovieList() {
//        return movieList;
//    }

//    public void setMovieList(Movie[] moviesList) {
//        this.moviesList = moviesList;
//    }


    public MovieAdapter(Movie[] moviesList){
        this.moviesList = moviesList;
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
        if (moviesList == null){
            return 0;
        }
        return moviesList.length;
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
