package com.example.android.mymovies2.screens.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.adapters.MovieAdapter;
import com.example.android.mymovies2.adapters.MyRVScrollListener;
import com.example.android.mymovies2.pojo.Movie;

import java.util.List;
import java.util.Objects;

public class MovieListActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerViewMovies;

    private boolean isLoading = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter();
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(onItemClickListener);
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    movieAdapter.updateMoviesListItems(movies);
                    isLoading = false;
                }
            }
        });
        viewModel.loadData(page++);
        recyclerViewMovies.addOnScrollListener(new MyRVScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore() {
                if (!isLoading) {
                    Log.i("Scroll", "page: " + page);
                    isLoading = true;
                    viewModel.loadData(page++);
                }
            }
        });
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Movie movie = movieAdapter.getMovies().get(position);
            Toast.makeText(MovieListActivity.this, "You Clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };
}
