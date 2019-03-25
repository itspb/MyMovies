package com.example.android.mymovies2.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.example.android.mymovies2.network.ApiService;

import java.util.concurrent.Executor;

public class MovieDataSourceFactory extends DataSource.Factory {
    MovieDataSource movieDataSource;
    MutableLiveData<MovieDataSource> mutableLiveData;
    Executor executor;
    ApiService apiService;

    public MovieDataSourceFactory(Executor executor, ApiService apiService) {
        this.executor = executor;
        this.apiService = apiService;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSource(apiService, executor);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
