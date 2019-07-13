package com.example.android.mymovies2.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String KEY_TOTAL_PAGES = "total_pages";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_ID = "id";
    public static ArrayList<Integer> nowPlayingIds;

    public static int getTotalPagesNowPlayingFromJSON (JSONObject jsonObject) {
    int result = 0;
    if (jsonObject == null) {
        return result;
    }
    try {
        result = jsonObject.getInt(KEY_TOTAL_PAGES);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return result;
    }

    public static ArrayList<Integer> getNowPlayingFromJSON(JSONObject jsonObject) {
        ArrayList<Integer> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                result.add(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void loadNowPlayingIds() {
        JSONObject jsonObjectTotalPagesNowPlaying = NetworkUtils.getJSONForNowPlaying(1);
        int totalPagesNowPlaying = JsonUtils.getTotalPagesNowPlayingFromJSON(jsonObjectTotalPagesNowPlaying);
        nowPlayingIds = new ArrayList<>();
        for (int page = 1; page <= totalPagesNowPlaying; page++) {
            JSONObject jsonObjectNowPlaying = NetworkUtils.getJSONForNowPlaying(page);
            ArrayList<Integer> onePageIds = JsonUtils.getNowPlayingFromJSON(jsonObjectNowPlaying);
            Log.i("loading", String.valueOf(page));
            nowPlayingIds.addAll(onePageIds);
        }
    }
}
