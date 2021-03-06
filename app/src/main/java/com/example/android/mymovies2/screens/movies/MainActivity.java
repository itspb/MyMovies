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
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.pojo.Movie;
import com.example.android.mymovies2.pojo.SearchResult;
import com.example.android.mymovies2.pojo.TV;
import com.example.android.mymovies2.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnFragmentInteractionListener {

    private SearchViewModel searchViewModel;
    private ArrayList<String> searchResultsTitles;
    private List<SearchResult> searchResultsMovies;
    private CursorAdapter cursorAdapter;
    private ProgressBar progressBarSearching;
    private ProgressBar progressBarLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JsonUtils.loadNowPlayingIds();
        FragmentManager fragmentManager = getSupportFragmentManager();
        MovieListFragment movieListFragment = MovieListFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.activityMainFrame, movieListFragment, "fragment_movie_list")
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressBarSearching = findViewById(R.id.progressBarSearching);
        progressBarLoading = findViewById(R.id.progressBarLoading);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Activity activity = this;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.menuSearch);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        AutoCompleteTextView searchAutoCompleteTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoCompleteTextView.setThreshold(2);
        String [] columnNames = { SearchManager.SUGGEST_COLUMN_TEXT_1 };
        int [] viewIds = { android.R.id.text1 };
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.query_suggestion, null, columnNames, viewIds);

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

        searchResultsTitles = new ArrayList<>();
        searchResultsMovies = new ArrayList<>();
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getSearchResults().observe(this, new Observer<List<SearchResult>>() {
            @Override
            public void onChanged(@Nullable List<SearchResult> searchResults) {
                if (searchResults != null) {
                    searchResultsMovies.clear();
                    searchResultsTitles.clear();
                    searchResultsMovies = searchResults;

                    for (SearchResult result : searchResults) {
                        searchResultsTitles.add((result.getMediaType().equals("movie")) ? result.getTitle() : result.getName());
                    }
                    Cursor cursor = createCursorFromResult(searchResultsTitles);
                    cursorAdapter.swapCursor(cursor);
                    progressBarSearching.setVisibility(View.INVISIBLE);
                }
            }
        });

        return true;
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener(Activity activity, CursorAdapter adapter) {
        return new SearchView.OnQueryTextListener() {

            private int waitingTime = 500;
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
                SearchResult sResult = searchResultsMovies.get(index);
                String type = sResult.getMediaType();
                Parcelable parcelable = null;
                if (type.equals("movie")) {
                    parcelable = new Movie(
                            sResult.getId(),
                            sResult.getVoteAverage(),
                            sResult.getTitle(),
                            sResult.getPosterPath(),
                            sResult.getOriginalTitle(),
                            sResult.getOverview(),
                            sResult.getReleaseDate());
                } else if (type.equals("tv")) {
                    parcelable = new TV(
                            sResult.getOriginalName(),
                            sResult.getName(),
                            sResult.getFirstAirDate(),
                            sResult.getId(),
                            sResult.getVoteAverage(),
                            sResult.getOverview(),
                            sResult.getPosterPath());
                } else {
                    //TODO person
                }

                MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(parcelable);
                FragmentManager fragmentManager = getSupportFragmentManager();
                // TODO add animation
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .hide(fragmentManager.findFragmentByTag("fragment_movie_list"))
                        .add(R.id.activityMainFrame, movieDetailFragment, "fragment_movie_detail")
                        .commit();
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


    @Override
    public void setProgressbarLoadingVisibility(boolean isVisible) {
        if (isVisible) progressBarLoading.setVisibility(View.VISIBLE);
        else progressBarLoading.setVisibility(View.INVISIBLE);
    }

}
