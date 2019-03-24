package com.example.android.mymovies2.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.mymovies2.Database.MovieDao;
import com.example.android.mymovies2.api.ApiFactory;
import com.example.android.mymovies2.pojo.Movie;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }
}
