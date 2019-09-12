package com.example.movieapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movieapp.database.FavoriteEntry;
import com.example.movieapp.database.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteRepository mRepository;
    private LiveData<List<FavoriteEntry>> mAllFavorites;
    private LiveData<FavoriteEntry> favoriteEntry;

    public FavoriteViewModel(Application application){
        super(application);
        mRepository = new FavoriteRepository(application);
        mAllFavorites = mRepository.getAllFavorites();
    }

    public LiveData<List<FavoriteEntry>> getAllFavorites(){
        return mAllFavorites;
    }

    public void insert(FavoriteEntry favoriteEntry){
        mRepository.insert(favoriteEntry);
    }

    public void delete(FavoriteEntry favoriteEntry){
        mRepository.delete(favoriteEntry);
    }

    public FavoriteEntry retrievebyId(String apiId){
        return null;
    }

    public LiveData<FavoriteEntry> getMovieById(String apiId){
        return mRepository.getMovieById(apiId);
    }
}
