package com.example.movieapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.database.FavoriteEntry;
import com.example.movieapp.database.FavoriteRoomDatabase;

public class AddFavoriteViewModel extends ViewModel {

    private LiveData<FavoriteEntry> favorite;

    public AddFavoriteViewModel(FavoriteRoomDatabase database, String apiId){
        favorite = database.favoriteDao().getMovieById(apiId);
    }

    public LiveData<FavoriteEntry> getFavorite(){
        return favorite;
    }
}
