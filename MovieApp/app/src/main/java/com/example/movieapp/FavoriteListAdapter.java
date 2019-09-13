package com.example.movieapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.database.FavoriteEntry;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        private final ImageView favoriteItemView;


        private FavoriteViewHolder(View itemView){
            super(itemView);
            favoriteItemView = (ImageView) itemView.findViewById(R.id.favorite_iv);
        }

        public void bind(String posterId){
            Picasso.get().load(posterId).into(favoriteItemView);
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInfalter.inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position){
        if (mFavorites != null){
            FavoriteEntry current = mFavorites.get(position);

            String uri = "https://api.themoviedb.org/3/movie/"+current.getApiId()+"?api_key=e4da10679254ee5d37b6f371a66acccf";

            String posterId = callForPoster(uri);

           holder.bind("https://image.tmdb.org/t/p/w780"+posterId);

        }else{
            Log.d(FavoriteListAdapter.class.getSimpleName(), "NO IMAGE");
        }
    }

    private final LayoutInflater mInfalter;
    private List<FavoriteEntry> mFavorites;
    private FavoriteViewHolder mholder;

    FavoriteListAdapter(Context context){
        mInfalter = LayoutInflater.from(context);
    }

    void setFavorites(List<FavoriteEntry> favorites){
        mFavorites = favorites;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mFavorites != null) return mFavorites.size();
        else return 0;
    }

    private String callForPoster(String uri){

        String posterId = null;

        try {
            String jsonObject = new PosterTask().execute(uri).get();

            JSONObject json = new JSONObject(jsonObject);
            posterId = json.getString("poster_path");

            Log.d(FavoriteListAdapter.class.getSimpleName(), "PosterId: "+posterId);

        }catch (Exception e){
            e.printStackTrace();
        }

        return posterId;
    }

    public class PosterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){

            String searchUrl = urls[0];

            URL posterUrl = null;

            try {
                posterUrl = new URL(searchUrl);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            String searchQueryResult = null;
            searchQueryResult = NetworkUtil.getResponseFromHttpURL(posterUrl);

            return searchQueryResult;
        }

        @Override
        protected void onPostExecute(String queryResults){

            JSONObject json = null;

            try {
                json = new JSONObject(queryResults);

                String results = json.getString("poster_path");

                Log.d(FavoriteListAdapter.class.getSimpleName(), "IMAGE POSTER"+results);

                //setUrlToBinder(results);

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

}
