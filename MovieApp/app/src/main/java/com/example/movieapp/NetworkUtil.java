package com.example.movieapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

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
}
