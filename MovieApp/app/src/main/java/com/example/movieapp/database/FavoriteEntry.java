package com.example.movieapp.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class FavoriteEntry implements Parcelable {

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


    /**
     *Functions to handle Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.apiId);
        dest.writeString(this.movieTitle);
    }

    protected FavoriteEntry(Parcel in) {
        this.id = in.readInt();
        this.apiId = in.readString();
        this.movieTitle = in.readString();
    }

    public static final Creator<FavoriteEntry> CREATOR = new Creator<FavoriteEntry>() {
        @Override
        public FavoriteEntry createFromParcel(Parcel source) {
            return new FavoriteEntry(source);
        }

        @Override
        public FavoriteEntry[] newArray(int size) {
            return new FavoriteEntry[size];
        }
    };
}
