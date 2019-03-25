package com.example.android.mymovies2.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mymovies2.BuildConfig;
import com.example.android.mymovies2.network.ApiFactory;
import com.example.android.mymovies2.network.ApiService;
import com.example.android.mymovies2.network.JSONParser;
import com.example.android.mymovies2.network.NetworkState;
import com.example.android.mymovies2.pojo.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    private static final String TAG = "MovieDataSource";
    private ApiService apiService;
    private Executor retryExecutor;
    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private static final String API_KEY = BuildConfig.ApiKey;
    private static final String LANGUAGE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_RATING = "vote_average.desc";


    public MovieDataSource(ApiService apiService, Executor retryExecutor) {
        this.apiService = apiService;
        this.retryExecutor = retryExecutor;
        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        apiService.getMovies(API_KEY, LANGUAGE, SORT_BY_POPULARITY, ApiFactory.API_DEFAULT_PAGE_KEY).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseStr;
                List<Movie> movies;
                if (response.isSuccessful() && response.code() == 200) {
                    try {
                        initialLoading.postValue(NetworkState.LOADING);
                        networkState.postValue(NetworkState.LOADED);
                        responseStr = response.body().string();
                        movies = JSONParser.getMovies(responseStr);
                        callback.onResult(movies, null, 2);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onResponse error: " + response.message());
                    initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMsg = t.getMessage();
                Log.e(TAG, "onFailure: " + errorMsg);
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMsg));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) { }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);
        apiService.getMovies(API_KEY, LANGUAGE, SORT_BY_POPULARITY, params.key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject responseJson;
                String responseStr;
                List<Movie> movies;
                Integer nextKey;

                if (response.isSuccessful() && response.code() == 200) {
                    try {
                        initialLoading.postValue(NetworkState.LOADING);
                        networkState.postValue(NetworkState.LOADED);
                        responseStr = response.body().string();
                        movies = JSONParser.getMovies(responseStr);
                        responseJson = new JSONObject(responseStr);
                        nextKey = (params.key == responseJson.getInt("total_pages")) ? null : params.key + 1;
                        callback.onResult(movies, nextKey);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onResponse error: " + response.message());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMsg = t.getMessage();
                Log.e(TAG, "onFailure: " + errorMsg);
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMsg));
            }
        });
    }
}
