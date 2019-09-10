package com.example.movieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.movieapp.database.FavoriteEntry;
import com.example.movieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener{

    private final String BASE_URL = "https://api.themoviedb.org";

    private final String API_KEY = "e4da10679254ee5d37b6f371a66acccf";
    private final String API_PATH_POPULAR= "3/movie/popular";
    private final String API_PATH_TOP_RATED = "3/movie/top_rated";
    private final String API_PATH_TRAILER = "movies/{id}/videos";
    private final String API_REGION = "us";
    private final String API_LANGUAGE = "en-US";
    private final String API_RELEASE_YEAR = "2019";

    private MovieAdapter movieAdapter;
    private RecyclerView mPosterList;

    private FavoriteViewModel mFavoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.favorite_recycleView);
        final FavoriteListAdapter adapter = new FavoriteListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mFavoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        mFavoriteViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                adapter.setFavorites(favoriteEntries);
            }
        });

        makeMovieDbRequest(API_PATH_POPULAR);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mPosterList = (RecyclerView) findViewById(R.id.rv_posters);
        mPosterList.setLayoutManager(layoutManager);
        mPosterList.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this);
        mPosterList.setAdapter(movieAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == 1 && resultCode == RESULT_OK){
            FavoriteEntry favoriteEntry = new FavoriteEntry("1234","Helloooo");
            mFavoriteViewModel.insert(favoriteEntry);
        }
    }

    /*
    Perform MovieDB api request
    @Parameter String sortBy - the query parameter to sort the results
     */
    public void makeMovieDbRequest(String sortBy){

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .path(sortBy)
                //.appendQueryParameter("region", API_REGION)
                //.appendQueryParameter("language", API_LANGUAGE)
                .appendQueryParameter("primary_release_year",API_RELEASE_YEAR)
                .appendQueryParameter("api_key",API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.d(MainActivity.class.getSimpleName(), "PATH------"+builtUri.toString());
        new MovieDbTask().execute(url);
    }

    public class MovieDbTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls){

            if (!isOnline()){
                return "There is no internet connection!";
            }

            URL searchUrl = urls[0];

            String searchQueryResult = null;
            searchQueryResult = getResponseFromHttpURL(searchUrl);

            Log.d(MainActivity.class.getSimpleName(), "Getting API data from URL: "+searchUrl);
            Log.d(MainActivity.class.getSimpleName(), "API's JSON data results: "+searchQueryResult);
            return searchQueryResult;
        }

        @Override
        protected void onPostExecute(String queryResults){

            JSONObject json = null;

            Log.d(MainActivity.class.getSimpleName(), "Starting onPostExecute - set API data to Movie object");

            try {
                json = new JSONObject(queryResults);

                JSONArray results = json.getJSONArray("results");

                setToMovieListObject(results);

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    /*
    Retrieves data from API using HTTPURLConnection
    @Parameter URL url - URL for API request
    @Return String - results from API
     */
    public static String getResponseFromHttpURL(URL url){

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }

        return null;
    }

    /*
    Set API's request data to Adapter's Movie object
    @Parameter JSONArray results - data from API
     */
    private void setToMovieListObject(JSONArray results){

        ArrayList<Movie> movieList = new ArrayList<>();
        try {

            for (int i = 0; i < results.length(); i++) {
                Log.d(MainActivity.class.getSimpleName(), results.getJSONObject(i).get("title").toString());

                JSONObject movieObj = results.getJSONObject(i);

                String movieApiId = movieObj.get("id").toString();
                String title = movieObj.get("title").toString();
                String posterPath = movieObj.get("poster_path").toString();
                String synopsis = movieObj.get("overview").toString();
                String userRating = movieObj.get("vote_average").toString();
                String releaseDate = movieObj.get("release_date").toString();

                Movie movie = new Movie(movieApiId, title, posterPath, synopsis, userRating, releaseDate);
                movieList.add(movie);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        movieAdapter.setMoviesList(movieList);
        movieAdapter.notifyDataSetChanged();
        Log.d(MainActivity.class.getSimpleName(), "Finished setting movie list from API");

    }

    @Override
    public void onListItemClick(Movie movie){
        Context context = MainActivity.this;
        Class destinationActivity = DetailActivity.class;

        Intent detailActivityIntent = new Intent(context, destinationActivity);
        detailActivityIntent.putExtra(Intent.EXTRA_TEXT, movie);

        startActivity(detailActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_sort_popular){
            makeMovieDbRequest(API_PATH_POPULAR);
            return true;
        }
        else if( item.getItemId() == R.id.action_sort_topRated){
            makeMovieDbRequest(API_PATH_TOP_RATED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    Function checks for an internet connection using Google's server
    @Return boolean isOnline - results true/false
     */
    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

}
