package com.example.movieapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("Select * from favorite order by movie_title")
    LiveData<List<FavoriteEntry>> loadAllFavorites();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFavorite(FavoriteEntry favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteEntry favoriteEntry);

    @Query("Delete from favorite")
    void deleteAll();

    @Delete
    void deleteFavorite(FavoriteEntry favoriteEntry);
}
