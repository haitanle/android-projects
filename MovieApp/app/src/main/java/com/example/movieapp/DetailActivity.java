package com.example.movieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movieapp.database.FavoriteDao;
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
    private final String TRAILER_QUERY_PATH_PT1 = "https://api.themoviedb.org/3/movie/";
    private final String TRAILER_QUERY_PATH_PT2 = "/videos?language=en-US&api_key=e4da10679254ee5d37b6f371a66acccf";

    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private ImageView mPosterImageView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mTrailerTv;

    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerList;

    private ReviewAdapter mReviewAdapter;
    private RecyclerView mReviewList;

    private ImageView mStarImageV;

    private FavoriteViewModel favoriteViewModel;

    private FavoriteViewModel mFavoriteViewModel;

    private AddFavoriteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final Movie movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);

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

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerList.setAdapter(mTrailerAdapter);

        getReviewList(movie.getMovieApiID());

        layoutManager = new LinearLayoutManager(this);
        mReviewList = (RecyclerView) findViewById(R.id.review_list_recyclerView);
        mReviewList.setLayoutManager(layoutManager);
        mReviewList.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();
        mReviewList.setAdapter(mReviewAdapter);

        mStarImageV = (ImageView) findViewById(R.id.star_image_view);

        mFavoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

//        mFavoriteViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteEntry>>() {
//            @Override
//            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
//               for (FavoriteEntry favoriteEntry: favoriteEntries){
//                   if (favoriteEntry.getApiId().equals(movie.getMovieApiID())){
//                       mStarImageV.setSelected(true);
//                       return;
//                   }
//               }
//               mStarImageV.setSelected(false);
//            }
//        });

        final FavoriteRoomDatabase mdb = FavoriteRoomDatabase.getDatabase(getApplication());

//        favoriteViewModel = ViewModelProviders.of(DetailActivity.this).get(FavoriteViewModel.class);
//
//        favoriteViewModel.insert(new FavoriteEntry(movie.getMovieApiID(),movie.getTitle()));
//
//        LiveData<FavoriteEntry> favoriteEntry = favoriteViewModel.getMovieById(movie.getMovieApiID());
//
//        favoriteEntry.observe(this, new Observer<FavoriteEntry>() {
//
//            @Override
//            public void onChanged(@final FavoriteEntry favoriteMovie) {
//
//                mReleaseDateTextView.setText(favoriteMovie.getMovieTitle());
////                if (favoriteMovie == null){
////                    mStarImageV.setSelected(false);
////                }else{
////                    mStarImageV.setSelected(true);
////                }
//            }
//        });

//        if (favoriteEntry == null){
//            mStarImageV.setSelected(false);
//        }else{
//            mStarImageV.setSelected(true);
//        }

        AddFavoriteViewModelFactory factory = new AddFavoriteViewModelFactory(mdb,movie.getMovieApiID());
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

//        FavoriteEntry favoriteEntry = mFavoriteViewModel.retrievebyId(movie.getMovieApiID());
//        mFavoriteViewModel.delete(favoriteEntry);

        mStarImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mStarImageV.setBackground(getResources().getDrawable(R.drawable.star_selector));
                // mStarImageV.setSelected(true);
//                Intent replyIntent = new Intent(DetailActivity.this, MainActivity.class);
//                String movieTitle = "Home Alone 2";
//
//                replyIntent.putExtra("Movie",movieTitle);
//                setResult(RESULT_OK, replyIntent);

                //favoriteViewModel = ViewModelProviders.of(DetailActivity.this).get(FavoriteViewModel.class);
                //LiveData<FavoriteEntry> favoriteEntry = favoriteViewModel.getMovieById(movie.getMovieApiID());

                ImageView favoriteIcon = (ImageView) findViewById(R.id.star_image_view);
                if (favoriteIcon.isSelected()){

                    //AddFavoriteViewModelFactory factory = new AddFavoriteViewModelFactory(mdb,movie.getMovieApiID());
                    FavoriteEntry favoriteEntry = viewModel.getFavorite().getValue();

                    mFavoriteViewModel.delete(favoriteEntry);
                    //favoriteIcon.setSelected(false);
                }else{
                    FavoriteEntry favoriteMovie = new FavoriteEntry(movie.getMovieApiID(), movie.getTitle());
                    mFavoriteViewModel.insert(favoriteMovie);
                }






                //mStarImageV.setSelected(true);

//                if (mStarImageV.isSelected()){
//                    mStarImageV.setSelected(false);
//                }else{
//                    mStarImageV.setSelected(true);
//                }

                // favoriteViewModel.insert(new FavoriteEntry(movie.getMovieApiID(),movie.getTitle()));

                //startActivity(replyIntent);
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

        String reviewQueryPath = "https://api.themoviedb.org/3/movie/"+movieId+"/reviews?language=en-US&api_key=e4da10679254ee5d37b6f371a66acccf";

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
}
