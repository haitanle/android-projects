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
    private LiveData<FavoriteEntry> mFavoriteMovie;

    public FavoriteRepository(Application application){
        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);
        mFavoriteDao = db.favoriteDao();
        mAllFavorites = mFavoriteDao.loadAllFavorites();
    }

    public LiveData<List<FavoriteEntry>> getAllFavorites(){
        return mAllFavorites;
    }

    public LiveData<FavoriteEntry> getMovieById(String apiId){
        return mFavoriteDao.getMovieById(apiId);
    }

    public void insert(FavoriteEntry favoriteEntry){
        new insertAsyncTask(mFavoriteDao).execute(favoriteEntry);
    }

    public void delete(FavoriteEntry favoriteEntry){
        new deleteAysncTask(mFavoriteDao).execute(favoriteEntry);
    }

    public LiveData<FavoriteEntry> retrieveId(String apiId){
        new retrieveAysncTask(mFavoriteDao).execute(apiId);
        return null;
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

    private static class deleteAysncTask extends AsyncTask<FavoriteEntry, Void, Void>{

        private FavoriteDao mAsyncTaskDao;

        deleteAysncTask(FavoriteDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteEntry... params){
            mAsyncTaskDao.deleteFavorite(params[0]);
            return null;
        }
    }

    private static class retrieveAysncTask extends AsyncTask<String, Void, LiveData<FavoriteEntry>>{

        private FavoriteDao mAsyncTaskDao;

        retrieveAysncTask(FavoriteDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<FavoriteEntry> doInBackground(final String... params){
            return mAsyncTaskDao.getMovieById(params[0]);
        }


    }
}
