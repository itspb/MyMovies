package com.example.android.mymovies2.screens.movies;

import android.app.Activity;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
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
    private ArrayList<String> searchResultsList;
    private CursorAdapter cursorAdapter;
    private ProgressBar progressBarSearching;
    private ProgressBar progressBarLoading;

    private boolean isLoading = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        progressBarSearching = findViewById(R.id.progressBarSearching);
        progressBarLoading = findViewById(R.id.progressBarLoading);
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
                    progressBarLoading.setVisibility(View.INVISIBLE);
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
                    progressBarLoading.setVisibility(View.VISIBLE);
                    isLoading = true;
                    movieViewModel.loadData(page++);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Activity activity = this;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.menuSearch);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        AutoCompleteTextView searchAutoCompleteTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoCompleteTextView.setThreshold(2);
        String [] columNames = { SearchManager.SUGGEST_COLUMN_TEXT_1 };
        int [] viewIds = { android.R.id.text1 };
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.query_suggestion, null, columNames, viewIds);

        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setOnSuggestionListener(getOnSuggestionClickListener());
        searchView.setOnQueryTextListener(getOnQueryTextListener(activity, cursorAdapter));
        searchView.setIconifiedByDefault(false);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                progressBarSearching.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        searchResultsList = new ArrayList<>();
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getSearchResults().observe(this, new Observer<List<SearchResult>>() {
            @Override
            public void onChanged(@Nullable List<SearchResult> searchResults) {
                if (searchResults != null) {
                    searchResultsList.clear();
                    for (SearchResult result : searchResults) {
                        searchResultsList.add((result.getMediaType().equals("movie")) ? result.getTitle() : result.getName());
                    }
                    Cursor cursor = createCursorFromResult(searchResultsList);
                    cursorAdapter.swapCursor(cursor);
                    progressBarSearching.setVisibility(View.INVISIBLE);
                }
            }
        });

        return true;
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener(Activity activity, CursorAdapter adapter) {
        return new SearchView.OnQueryTextListener() {

            private int waitingTime = 1000;
            private CountDownTimer timer;

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // TODO: clear cursorAdapter in a better way
                cursorAdapter.swapCursor(new MatrixCursor(new String[]{ BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1 }));
                if (s.isEmpty()) progressBarSearching.setVisibility(View.INVISIBLE);
                if (s.length() > 0) {
                    progressBarSearching.setVisibility(View.VISIBLE);
                    if(timer != null){
                        timer.cancel();
                    }
                    timer = new CountDownTimer(waitingTime, 500) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            searchViewModel.doSearch(s);
                        }
                    };
                    timer.start();
                }
               return false;
            }
        };
    }

     private SearchView.OnSuggestionListener getOnSuggestionClickListener() {
        return new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int index) {
                // TODO: handle suggestion item click
                return true;
            }
        };
    }


    private Cursor createCursorFromResult(ArrayList<String> searchResultsList)  {
        String[] menuCols = { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1 };
        MatrixCursor cursor = new MatrixCursor(menuCols);
        int counter = 0;
        for (String s : searchResultsList) {
            cursor.addRow(new Object[] { counter, s});
            counter++;
        }
        return cursor;
    }
}
