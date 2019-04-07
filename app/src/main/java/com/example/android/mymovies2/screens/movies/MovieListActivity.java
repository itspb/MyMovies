package com.example.android.mymovies2.screens.movies;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.adapters.MovieAdapter;
import com.example.android.mymovies2.adapters.MyRVScrollListener;
import com.example.android.mymovies2.pojo.Movie;
import com.example.android.mymovies2.pojo.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerViewMovies;
    private SearchViewModel searchViewModel;

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
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    movieAdapter.updateMoviesListItems(movies);
                    isLoading = false;
                }
            }
        });
        movieViewModel.loadData(page++);
        recyclerViewMovies.addOnScrollListener(new MyRVScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore() {
                if (!isLoading) {
                    Log.i("Scroll", "page: " + page);
                    isLoading = true;
                    movieViewModel.loadData(page++);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem searchMenu = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) searchMenu.getActionView();

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLACK);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);

        ArrayList<String> searchResultsList = new ArrayList<>();
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchResultsList);
        searchAutoComplete.setAdapter(searchAdapter);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getSearchResults().observe(this, new Observer<List<SearchResult>>() {
            @Override
            public void onChanged(@Nullable List<SearchResult> searchResults) {
                if (searchResults != null) {
                    Log.i("Search", "onChanged IF triggered");
                    searchResultsList.clear();
                    for (SearchResult result : searchResults) {
                        searchResultsList.add((result.getMediaType().equals("movie")) ? result.getTitle() : result.getName());
                    }
                    Log.i("Search", searchResultsList.toString());


                    searchAdapter.addAll(searchResultsList);
                    Log.i("Search", "Count before: " + searchAdapter.getCount());
                    searchAdapter.notifyDataSetChanged();
                    Log.i("Search", "Count after: " + searchAdapter.getCount());

                }
            }
        });


        // Listen to search view item on click event.
//        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
//                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
//                searchAutoComplete.setText("" + queryString);
//                Toast.makeText(MovieListActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 3) {
                    new CountDownTimer(5000, 5000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            Log.i("Search", "doSearch triggered");
                            searchViewModel.doSearch(newText);
                        }

                    }.start();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
