package com.example.movieapp.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Respository to handle network call
 */
public class FavoriteRepository {

    private FavoriteDao mFavoriteDao;
    private LiveData<List<FavoriteEntry>> mAllFavorites;

    public FavoriteRepository(Application application){
        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);
        mFavoriteDao = db.favoriteDao();
        mAllFavorites = mFavoriteDao.loadAllFavorites();
    }

    public LiveData<List<FavoriteEntry>> getAllFavorites(){
        return mAllFavorites;
    }

    public void insert(FavoriteEntry favoriteEntry){
        new insertAsyncTask(mFavoriteDao).execute(favoriteEntry);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteEntry, Void, Void>{

        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteEntry... params){
            mAsyncTaskDao.insertFavorite(params[0]);
            return null;
        }
    }
}
