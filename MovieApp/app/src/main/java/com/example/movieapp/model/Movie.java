package com.example.movieapp.model;

import java.io.Serializable;

public class Movie implements Serializable {

    public Movie(String title, String imagePath, String synopsis, String userRating, String releaseDate) {

        this.title = title;
        this.imagePath = imagePath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    private String title;
    private String imagePath;
    private String synopsis;
    private String userRating;
    private String releaseDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imagePath;
    }

    public void setImageUrl(String imagePath) {
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

}

