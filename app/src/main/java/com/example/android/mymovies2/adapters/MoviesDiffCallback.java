package com.example.android.mymovies2.adapters;

import android.support.v7.util.DiffUtil;

import com.example.android.mymovies2.pojo.Movie;

import java.util.List;

public class MoviesDiffCallback extends DiffUtil.Callback {

    private final List<Movie> oldMoviesList;
    private final List<Movie> newMoviesList;

    public MoviesDiffCallback(List<Movie> oldMoviesList, List<Movie> newMoviesList) {
        this.oldMoviesList = oldMoviesList;
        this.newMoviesList = newMoviesList;
    }

    @Override
    public int getOldListSize() {
        return oldMoviesList.size();
    }

    @Override
    public int getNewListSize() {
        return newMoviesList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMoviesList.get(oldItemPosition).getId() == newMoviesList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Movie oldMovie = oldMoviesList.get(oldItemPosition);
        final Movie newMovie = newMoviesList.get(newItemPosition);
        return oldMovie.getPosterPath().equals(newMovie.getPosterPath());
    }
}
