package com.example.android.mymovies2.screens.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.adapters.MovieAdapter;
import com.example.android.mymovies2.adapters.MyRVScrollListener;
import com.example.android.mymovies2.pojo.Movie;

import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerViewMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter();
        recyclerViewMovies.setAdapter(movieAdapter);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    movieAdapter.setMovies(movies);
                }
            }
        });
        viewModel.loadData(1);
        recyclerViewMovies.addOnScrollListener(new MyRVScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                viewModel.loadData(currentPage);
            }
        });
    }
}
