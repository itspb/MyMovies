package com.example.android.mymovies2.screens.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.adapters.MoviePagedListAdapter;
import com.example.android.mymovies2.network.NetworkState;
import com.example.android.mymovies2.pojo.Movie;

public class MovieListActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private RecyclerView recyclerViewMovies;
    private MoviePagedListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        adapter = new MoviePagedListAdapter(this);
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(@Nullable PagedList<Movie> movies) {
                adapter.submitList(movies);
            }
        });
        viewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                adapter.setNetworkState(networkState);
            }
        });
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewMovies.setAdapter(adapter);
    }
}
