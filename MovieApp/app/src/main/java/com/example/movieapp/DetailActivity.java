package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DetailActivity extends AppCompatActivity {

    private final String URL_MOVIE_POSTER_SMALL = "https://image.tmdb.org/t/p/w185//";
    private final String TRAILER_QUERY_PATH_PT1 = "https://api.themoviedb.org/3/movie/";
    private final String TRAILER_QUERY_PATH_PT2 = "/videos?language=en-US&api_key=e4da10679254ee5d37b6f371a66acccf";

    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private ImageView mPosterImageView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mTrailerTv;

    private ArrayList<String> trailers;

    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerList;

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
        Picasso.get().load(URL_MOVIE_POSTER_SMALL+movie.getImageUrl()).into(mPosterImageView);

        mRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        mRatingTextView.setText(movie.getUserRating());

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mReleaseDateTextView.setText(movie.getReleaseDate());

        makeTrailerUri(movie.getMovieApiID());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerList = (RecyclerView) findViewById(R.id.trailer_list_recyclerView);
        mTrailerList.setLayoutManager(layoutManager);
        mTrailerList.setHasFixedSize(true);
//
        mTrailerAdapter = new TrailerAdapter();
        mTrailerList.setAdapter(mTrailerAdapter);
//
        ActionBar actionBar = this.getActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void makeTrailerUri(String movieId){

        String movieTrailerQuery = TRAILER_QUERY_PATH_PT1+movieId+TRAILER_QUERY_PATH_PT2;

        Uri builtUri = Uri.parse(movieTrailerQuery).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.d(MainActivity.class.getSimpleName(), "PATH TRAILER-----"+builtUri.toString());
        new TrailerQueryTask().execute(url);
    }

    public class TrailerQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL trailerQueryURL = urls[0];

            String searchQueryResults = null;
            searchQueryResults = NetworkUtil.getResponseFromHttpURL(trailerQueryURL);

            Log.d(DetailActivity.class.getSimpleName(), "searchQueryResults"+searchQueryResults);
            return searchQueryResults;
        }

        @Override
        protected void onPostExecute(String queryResults){

            JSONObject json = null;

            Log.d(MainActivity.class.getSimpleName(), "Starting onPostExecute - for trailer app");

            try {
                json = new JSONObject(queryResults);

                JSONArray result = json.getJSONArray("results");

                ArrayList<String> trailerList = new ArrayList<>();

                for (int i = 0; i< result.length(); i++){
                    JSONObject trailerObj = result.getJSONObject(i);

                    String trailerTitle = trailerObj.get("name").toString();

                    Log.d(MainActivity.class.getSimpleName(), "Trailer Name---"+trailerTitle);

                    trailerList.add(trailerTitle);
                }
                mTrailerAdapter.setMovieTrailers(trailerList);
                mTrailerAdapter.notifyDataSetChanged();

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
