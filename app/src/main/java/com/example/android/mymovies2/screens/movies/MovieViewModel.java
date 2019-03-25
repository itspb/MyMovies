package com.example.android.mymovies2.screens.movies;


import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.android.mymovies2.adapters.MovieDataSource;
import com.example.android.mymovies2.adapters.MovieDataSourceFactory;
import com.example.android.mymovies2.network.ApiFactory;
import com.example.android.mymovies2.network.ApiService;
import com.example.android.mymovies2.network.NetworkState;
import com.example.android.mymovies2.pojo.Movie;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieViewModel extends ViewModel {
    private LiveData<PagedList<Movie>> movies;
    private LiveData<NetworkState> networkState;
    private Executor executor;
    private LiveData<MovieDataSource> dataSource;

    public MovieViewModel() {
        executor = Executors.newFixedThreadPool(5);
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        MovieDataSourceFactory factory = new MovieDataSourceFactory(executor, apiService);
        dataSource = factory.getMutableLiveData();

        networkState = Transformations.switchMap(factory.getMutableLiveData(), new Function<MovieDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(MovieDataSource input) {
                return input.getNetworkState();
            }
        });

        PagedList.Config pageConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .build();

        movies = (new LivePagedListBuilder<Integer, Movie>(factory, pageConfig))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Movie>> getMovies() {
        return movies;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }
}

