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

    @ColumnInfo(name="image_path")
    private String imagePath;

    @ColumnInfo(name="synopsis")
    private String synopsis;

    @ColumnInfo(name="user_rating")
    private String userRating;

    @ColumnInfo(name="release_date")
    private String releaseDate;


    public FavoriteEntry(String apiId, String movieTitle, String imagePath, String synopsis, String userRating, String releaseDate) {
        this.apiId = apiId;
        this.movieTitle = movieTitle;
        this.imagePath = imagePath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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
        dest.writeString(this.imagePath);
        dest.writeString(this.synopsis);
        dest.writeString(this.userRating);
        dest.writeString(this.releaseDate);
    }

    protected FavoriteEntry(Parcel in) {
        this.id = in.readInt();
        this.apiId = in.readString();
        this.movieTitle = in.readString();
        this.imagePath = in.readString();
        this.synopsis = in.readString();
        this.userRating = in.readString();
        this.releaseDate = in.readString();
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
