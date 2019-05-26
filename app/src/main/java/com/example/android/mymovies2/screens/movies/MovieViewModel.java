package com.example.android.mymovies2.screens.movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.mymovies2.Constants;
import com.example.android.mymovies2.Database.MovieDatabase;
import com.example.android.mymovies2.api.ApiMovieFactory;
import com.example.android.mymovies2.api.ApiSearchFactory;
import com.example.android.mymovies2.api.ApiService;
import com.example.android.mymovies2.pojo.Movie;
import com.example.android.mymovies2.pojo.MovieResponse;
import com.example.android.mymovies2.pojo.SearchResponse;

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

    public MovieViewModel(@NonNull Application application) {
        super(application);
        db = MovieDatabase.getInstance(application);
        movies = db.movieDao().getAllMovies();
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    @SuppressWarnings("unchecked")
    private void insertMovies(List<Movie> movies) {
        new InsertMoviesTask().execute(movies);
    }

    private void deleteAllMovies() {
        AsyncTask.execute(new DeleteAllMoviesTask());
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

    private static class DeleteAllMoviesTask implements Runnable {
        @Override
        public void run() {
            db.movieDao().deleteAllMovies();
        }
    }

    public void loadData(int page) {
        ApiMovieFactory apiMovieFactory = ApiMovieFactory.getInstance();
        ApiService apiService = apiMovieFactory.getApiService();
        Disposable disposable = apiService.getMovies(Constants.API_KEY, Constants.LANGUAGE, Constants.SORT_BY_POPULARITY, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        if (page == 1) { deleteAllMovies(); }
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

