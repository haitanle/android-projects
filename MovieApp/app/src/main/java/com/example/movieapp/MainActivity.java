package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    // private final String BASE_URL = "http://api.themoviedb.org/3/movie/popular";
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2019-07-11&primary_release_date.lte=2019-07-25";
    private final String API_KEY = "e4da10679254ee5d37b6f371a66acccf";

    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        movieAdapter = new MovieAdapter();

        makeMovieDbSearchQuery();
    }

    public void makeMovieDbSearchQuery(){

        // http://api.themoviedb.org/3/movie/popular?api_key=[KEY]
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("api_key",API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

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

            Log.d(MainActivity.class.getSimpleName(), "THERE!-----------");

            try {
                json = new JSONObject(queryResults);

                JSONArray results = json.getJSONArray("results");

                for (int i = 0; i < results.length(); i++){
                    Log.d(MainActivity.class.getSimpleName(), results.getJSONObject(i).get("title").toString());
                    //results.getJSONObject(i).getString("title");
                }
//
//                Iterator<String> keys = results.keys();
//
//                while(keys.hasNext()){
//                    String key = keys.next();
//                    if (results.get(key) instanceof JSONObject){
//                        Log.d(MainActivity.class.getSimpleName(), "THERE!-----------");
//                        Log.d(MainActivity.class.getSimpleName(), ((JSONObject) results.get(key)).toString());
//                    }
//                }

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
