package com.example.movieapp;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieapp.database.FavoriteRoomDatabase;

public class AddFavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final FavoriteRoomDatabase mDb;
    private final String mApiId;

    public AddFavoriteViewModelFactory(FavoriteRoomDatabase database, String apiId){
        mDb = database;
        mApiId = apiId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new AddFavoriteViewModel(mDb, mApiId);
    }


}
