package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.database.FavoriteEntry;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        private final TextView favoriteItemView;

        private FavoriteViewHolder(View itemView){
            super(itemView);
            favoriteItemView = itemView.findViewById(R.id.favorite_item_textV);
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInfalter.inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position){
        if (mFavorites != null){
            FavoriteEntry current = mFavorites.get(position);
            holder.favoriteItemView.setText(current.getMovieTitle());
        }else{
            holder.favoriteItemView.setText("No Word");
        }
    }

    private final LayoutInflater mInfalter;
    private List<FavoriteEntry> mFavorites;

    FavoriteListAdapter(Context context){
        mInfalter = LayoutInflater.from(context);
    }


    void setFavorites(List<FavoriteEntry> favorites){
        mFavorites = favorites;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mFavorites != null) return mFavorites.size();
        else return 0;
    }
}
