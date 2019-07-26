package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.movieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    // private final String BASE_URL = "http://api.themoviedb.org/3/movie/now_playing?primary_release_date.gte=2019-07-11&primary_release_date.lte=2019-07-25";
    private final String BASE_URL = "http://api.themoviedb.org";

    private final String API_KEY = "[API_KEY]";
    private final String API_REGION = "us";
    private final String PATH_NOW_PLAING = "3/movie/now_playing";

    private MovieAdapter movieAdapter;
    private RecyclerView mPosterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeMovieDbSearchQuery();

        mPosterList = (RecyclerView) findViewById(R.id.rv_posters);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mPosterList.setLayoutManager(layoutManager);
        mPosterList.setHasFixedSize(true);

        movieAdapter = new MovieAdapter();
        mPosterList.setAdapter(movieAdapter);

    }

    public void makeMovieDbSearchQuery(){

        // http://api.themoviedb.org/3/movie/popular?api_key=[KEY]
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .path(PATH_NOW_PLAING)
                .appendQueryParameter("region", API_REGION)
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
            URL searchUrl = urls[0];

            String searchQueryResult = null;
            searchQueryResult = getResponseFromHttpURL(searchUrl);

            Log.d(MainActivity.class.getSimpleName(), "HERE!-----------"+searchUrl);
            Log.d(MainActivity.class.getSimpleName(), searchQueryResult);
            return searchQueryResult;
        }

        @Override
        protected void onPostExecute(String queryResults){

            JSONObject json = null;

            ArrayList<Movie> movieList = new ArrayList<>();

            Log.d(MainActivity.class.getSimpleName(), "THERE!-----------");

            try {
                json = new JSONObject(queryResults);

                JSONArray results = json.getJSONArray("results");

                for (int i = 0; i < results.length(); i++){
                    Log.d(MainActivity.class.getSimpleName(), results.getJSONObject(i).get("title").toString());

                    JSONObject movieObj = results.getJSONObject(i);

                    String title = movieObj.get("title").toString();
                    String posterPath = movieObj.get("poster_path").toString();
                    String synopsis = movieObj.get("overview").toString();
                    String userRating = movieObj.get("vote_average").toString();
                    String releaseDate = movieObj.get("release_date").toString();

                    Movie movie = new Movie(title,posterPath, synopsis, userRating, releaseDate);
                    movieList.add(movie);
                }

                movieAdapter.setMoviesList(movieList);
                movieAdapter.notifyDataSetChanged();
                Log.d(MainActivity.class.getSimpleName(), "DONE!-----------");


            }catch(JSONException e){
                e.printStackTrace();
            }



        }
    }

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
}
