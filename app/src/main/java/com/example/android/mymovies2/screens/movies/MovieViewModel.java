package com.example.android.mymovies2.screens.movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.mymovies2.BuildConfig;
import com.example.android.mymovies2.Database.MovieDao;
import com.example.android.mymovies2.Database.MovieDatabase;
import com.example.android.mymovies2.api.ApiFactory;
import com.example.android.mymovies2.api.ApiService;
import com.example.android.mymovies2.pojo.Movie;
import com.example.android.mymovies2.pojo.MovieResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends AndroidViewModel {

    private static MovieDatabase db;
    private LiveData<List<Movie>> movies;
    private MutableLiveData<Throwable> errors;
    private CompositeDisposable compositeDisposable;

    private static final String API_KEY = BuildConfig.ApiKey;
    private static final String LANGUAGE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_RATING = "vote_average.desc";

    public MovieViewModel(@NonNull Application application) {
        super(application);
        db = MovieDatabase.getInstance(application);
        movies = db.movieDao().getAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    @SuppressWarnings("unchecked")
    private void insertMovies(List<Movie> movies) {
        new InsertMoviesTask().execute(movies);
    }

    private void deleteAllMovies() {
        new DeleteAllMoviesTask().execute();
    }

    private static class InsertMoviesTask extends AsyncTask<List<Movie>, Void, Void> {

        @Override
        protected Void doInBackground(List<Movie>... lists) {
            if (lists != null && lists.length > 0) {
                db.movieDao().insertMovies(lists[0]);
            }
            return null;
        }
    }

    private static class DeleteAllMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            db.movieDao().deleteAllMovies();
            return null;
        }
    }

    public void loadData(int page) {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();
        Disposable disposable = apiService.getMovies(API_KEY, LANGUAGE, SORT_BY_POPULARITY, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        //deleteAllMovies();
                        insertMovies(movieResponse.getMovies());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}

