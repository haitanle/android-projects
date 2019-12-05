package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.database.FavoriteEntry;
import com.example.movieapp.database.FavoriteRoomDatabase;
import com.example.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener{

    private final String URL_MOVIE_POSTER_SMALL = "https://image.tmdb.org/t/p/w185//";
    //todo: fix trailer query part1/2
    private final String TRAILER_QUERY_PATH_PT1 = "https://api.themoviedb.org/3/movie/";
    private final String TRAILER_QUERY_PATH_PT2 = "/videos?language=en-US&api_key="+MainActivity.API_KEY;

    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private ImageView mPosterImageView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;

    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerList;

    private ReviewAdapter mReviewAdapter;
    private RecyclerView mReviewList;

    private ImageView mStarImageV;

    private FavoriteViewModel mFavoriteViewModel;
    private AddFavoriteViewModel viewModel;

    private AddFavoriteViewModelFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Movie movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);

        //todo: create function to set DetailActivity UI
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

        //todo: fix trailer/review reuse code
        makeTrailerUri(movie.getMovieApiID());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailerList = (RecyclerView) findViewById(R.id.trailer_list_recyclerView);
        mTrailerList.setLayoutManager(layoutManager);
        mTrailerList.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerList.setAdapter(mTrailerAdapter);

        getReviewList(movie.getMovieApiID());

        layoutManager = new LinearLayoutManager(this);
        mReviewList = (RecyclerView) findViewById(R.id.review_list_recyclerView);
        mReviewList.setLayoutManager(layoutManager);
        mReviewList.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mReviewList.setAdapter(mReviewAdapter);

        //there has to be some easy way to tie this into the liveData selector
        mStarImageV = (ImageView) findViewById(R.id.star_image_view);

        mFavoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);


        final FavoriteRoomDatabase mdb = FavoriteRoomDatabase.getDatabase(getApplication());

        //sommme reallly badddd code
        /*



         */

        factory = new AddFavoriteViewModelFactory(mdb,movie.getMovieApiID());
        viewModel = ViewModelProviders.of(this, factory).get(AddFavoriteViewModel.class);

        viewModel.getFavorite().observe(this, new Observer<FavoriteEntry>() {
            @Override
            public void onChanged(FavoriteEntry favoriteEntry) {
                if (favoriteEntry == null){
                    mStarImageV.setSelected(false);
                }else{
                    mStarImageV.setSelected(true);
                }
            }
        });


        mStarImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView favoriteIcon = (ImageView) findViewById(R.id.star_image_view);
                if (favoriteIcon.isSelected()){

                    FavoriteEntry favoriteEntry = viewModel.getFavorite().getValue();
                    mFavoriteViewModel.delete(favoriteEntry);


                }else{
                    FavoriteEntry favoriteMovie = new FavoriteEntry(movie.getMovieApiID(), movie.getTitle());
                    mFavoriteViewModel.insert(favoriteMovie);
                }

            }
        });

//
        ActionBar actionBar = this.getActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * Function to call AysncTask for movie trailers
     * @param movieId movie id to retrieve trailers
     */
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

                    String trailerKey = trailerObj.get("key").toString();

                    Log.d(MainActivity.class.getSimpleName(), "Trailer key---"+trailerKey);

                    trailerList.add(trailerKey);
                }
                mTrailerAdapter.setMovieTrailers(trailerList);
                mTrailerAdapter.notifyDataSetChanged();

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onListItemClick(String trailerKey) {

        String url = "https://www.youtube.com/watch?v="+trailerKey;

        Uri builtUri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, builtUri);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    /**
     * Method to call AsyncTask for movie reviews
     * @param movieId id of the movie
     */
    private void getReviewList(String movieId){

        String reviewQueryPath = "https://api.themoviedb.org/3/movie/"+movieId+"/reviews?language=en-US&api_key="+MainActivity.API_KEY;

        Uri builtUri = Uri.parse(reviewQueryPath).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.d(MainActivity.class.getSimpleName(), "PATH TRAILER-----"+builtUri.toString());
        new ReviewQueryTask().execute(url);
    }

    public class ReviewQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL reviewQueryUrl = urls[0];

            String searchQueryResults = null;
            searchQueryResults = NetworkUtil.getResponseFromHttpURL(reviewQueryUrl);

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

                ArrayList<String> reviewList = new ArrayList<>();

                for (int i = 0; i< result.length(); i++){
                    JSONObject trailerObj = result.getJSONObject(i);

                    String trailerKey = trailerObj.get("content").toString();

                    Log.d(MainActivity.class.getSimpleName(), "comment---"+trailerKey);

                    reviewList.add(trailerKey);
                }
                mReviewAdapter.setReviewsList(reviewList);
                mReviewAdapter.notifyDataSetChanged();

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
