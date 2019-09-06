//package com.example.movieapp;
//
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Scanner;
//
//public class MovieUtil {
//
//    private final static String BASE_URL = "https://api.themoviedb.org";
//
//
//    private final static String API_KEY = "e4da10679254ee5d37b6f371a66acccf";
//    private final static String API_PATH_POPULAR= "3/movie/popular";
//    private final static String API_PATH_TOP_RATED = "3/movie/top_rated";
//    private final static String API_PATH_TRAILER = "movies/{id}/videos";
//    private final static String API_REGION = "us";
//    private final static String API_LANGUAGE = "en-US";
//    private final static String API_RELEASE_YEAR = "2019";
//
//
//    /*
//    Perform MovieDB api request
//    @Parameter String sortBy - the query parameter to sort the results
//     */
//    public static void makeMovieDbRequest(String sortBy){
//
//        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
//                .path(sortBy)
//                //.appendQueryParameter("region", API_REGION)
//                //.appendQueryParameter("language", API_LANGUAGE)
//                .appendQueryParameter("primary_release_year",API_RELEASE_YEAR)
//                .appendQueryParameter("api_key",API_KEY)
//                .build();
//
//        URL url = null;
//        try {
//            url = new URL(builtUri.toString());
//        }catch (MalformedURLException e){
//            e.printStackTrace();
//        }
//
//        Log.d(MainActivity.class.getSimpleName(), "PATH------"+builtUri.toString());
//        new MovieDbTask().execute(url);
//    }
//
//    public static class MovieDbTask extends AsyncTask<URL, Void, JSONArray> {
//
//        @Override
//        protected String doInBackground(URL... urls){
//
//            if (!isOnline()){
//                return "There is no internet connection!";
//            }
//
//            URL searchUrl = urls[0];
//
//            String searchQueryResult = null;
//            searchQueryResult = getResponseFromHttpURL(searchUrl);
//
//            Log.d(MainActivity.class.getSimpleName(), "Getting API data from URL: "+searchUrl);
//            Log.d(MainActivity.class.getSimpleName(), "API's JSON data results: "+searchQueryResult);
//            return searchQueryResult;
//        }
//
//        @Override
//        protected JSONArray onPostExecute(String queryResults){
//
//            JSONObject json = null;
//
//            Log.d(MainActivity.class.getSimpleName(), "Starting onPostExecute - set API data to Movie object");
//
//            try {
//                json = new JSONObject(queryResults);
//
//                JSONArray results = json.getJSONArray("results");
//
//                return results;
//
//            }catch(JSONException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /*
//    Retrieves data from API using HTTPURLConnection
//    @Parameter URL url - URL for API request
//    @Return String - results from API
//     */
//    public static String getResponseFromHttpURL(URL url){
//
//        HttpURLConnection urlConnection = null;
//
//        try {
//            urlConnection = (HttpURLConnection) url.openConnection();
//            InputStream in = urlConnection.getInputStream();
//
//            Scanner scanner = new Scanner(in);
//            scanner.useDelimiter("\\A");
//
//            boolean hasInput = scanner.hasNext();
//            if (hasInput){
//                return scanner.next();
//            }else{
//                return null;
//            }
//        }catch(IOException e){
//            e.printStackTrace();
//        }finally {
//            urlConnection.disconnect();
//        }
//
//        return null;
//    }
//
//}
