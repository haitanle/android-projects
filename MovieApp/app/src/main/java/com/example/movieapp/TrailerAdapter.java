package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final ListItemClickListener mOnClickListener;

    private ArrayList<String> movieTrailers;

    public ArrayList<String> getMovieTrailers() {
        return movieTrailers;
    }

    public void setMovieTrailers(ArrayList<String> movieTrailers) {
        this.movieTrailers = movieTrailers;
    }

    public interface ListItemClickListener{
        void onListItemClick(String string);
    }


    public TrailerAdapter(ListItemClickListener clickListener){
        mOnClickListener = clickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailer_list;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        TrailerViewHolder trailerHolder = new TrailerViewHolder(view);
        return trailerHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        String movieTrailerTitle = movieTrailers.get(position);
        holder.bind("Trailer " + (position + 1));
    }

    @Override
    public int getItemCount() {
        if (movieTrailers == null){
            return 0;
        }
        return movieTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView listItemTrailerView;

        public TrailerViewHolder(View itemView){
            super(itemView);
            listItemTrailerView = itemView.findViewById(R.id.tv_trailer_id);
            itemView.setOnClickListener(this);
        }

        public void bind(String movieTrailerTitle){
            listItemTrailerView.setText(movieTrailerTitle);
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            String key = getMovieTrailers().get(clickPosition);
            mOnClickListener.onListItemClick(key);
        }
    }


}
