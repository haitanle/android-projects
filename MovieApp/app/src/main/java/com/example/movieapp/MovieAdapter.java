package com.example.movieapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {

    private final ListItemClickListener mOnClickListener;
    private ArrayList<Movie> moviesList;

    public MovieAdapter(ListItemClickListener clickListener){
        mOnClickListener = clickListener;
    }

    public ArrayList<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public interface ListItemClickListener{
        void onListItemClick(Movie movie);
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
        Movie movie = getMoviesList().get(position);
        Log.d(MovieAdapter.class.getSimpleName(), "BIND--------"+movie.getImageUrl());

        holder.bind("http://image.tmdb.org/t/p/w780//"+movie.getImageUrl());
    }

    @Override
    public int getItemCount(){
        if (moviesList == null){
            return 0;
        }
        return getMoviesList().size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView listItemImageView;

        public PosterViewHolder(View itemView){
            super(itemView);
            listItemImageView = (ImageView) itemView.findViewById(R.id.tv_poster_image);
            itemView.setOnClickListener(this);
        }

        public void bind(String imgUrl){
            Picasso.get().load(imgUrl).into(listItemImageView);
        }

        @Override
        public void onClick(View view){
            int clickPosition = getAdapterPosition();
            Movie movie = getMoviesList().get(clickPosition);
            mOnClickListener.onListItemClick(movie);
        }
    }
}
