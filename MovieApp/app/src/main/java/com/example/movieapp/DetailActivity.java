package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private ImageView mPosterImageView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);

        mTitleTextView = (TextView) findViewById(R.id.tv_movieTitle);
        mTitleTextView.setText(movie.getTitle());

        mSynopsisTextView = (TextView) findViewById(R.id.tv_synopsis);
        mSynopsisTextView.setText(movie.getSynopsis());

        mPosterImageView = (ImageView) findViewById(R.id.iv_poster_thumbnail);
        Picasso.get().load("http://image.tmdb.org/t/p/w185//"+movie.getImageUrl()).into(mPosterImageView);

        mRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        mRatingTextView.setText(movie.getUserRating());

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mReleaseDateTextView.setText(movie.getReleaseDate());

    }
}
