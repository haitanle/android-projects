package com.example.movieapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class FavoriteEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="api_id")
    private String apiId;

    @ColumnInfo(name="movie_title")
    private String movieTitle;

    public FavoriteEntry(String apiId, String movieTitle) {
        this.apiId = apiId;
        this.movieTitle = movieTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}
