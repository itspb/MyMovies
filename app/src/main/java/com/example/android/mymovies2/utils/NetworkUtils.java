package com.example.android.mymovies2.utils;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.mymovies2.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL_NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing";
    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_REGION = "region";

    public static JSONObject getJSONForNowPlaying(int page) {
        JSONObject result = null;
        URL url = buildURLForNowPlaying(page);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static URL buildURLForNowPlaying(int page) {
        Uri uri = Uri.parse(BASE_URL_NOW_PLAYING).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, Constants.API_KEY)
                // TODO Region must depends on OS lang
                .appendQueryParameter(PARAMS_REGION, "RU")
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page)).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            if (urls == null || urls.length == 0){
                return result;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while(line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }
}
