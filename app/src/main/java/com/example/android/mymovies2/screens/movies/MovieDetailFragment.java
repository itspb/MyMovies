package com.example.android.mymovies2.screens.movies;


import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.pojo.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    private static final String MOVIE_ID = "movieId";
    private static final String NOW_PLAYING_IDS = "nowPlayingIds";

    private Movie movie;
    private int movieId;
    private ArrayList<Integer> nowPlayingIds;

    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewReleaseDate;
    private TextView textViewRating;
    private TextView textViewStatus;
    private ImageView imageViewPoster;
    private Button buttonAddToFav;
    private Button buttonShowSessions;
    private TextView textViewDescription;
    private MovieViewModel movieViewModel;

    public static MovieDetailFragment newInstance(int movieId, ArrayList<Integer> nowPlayingIds) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID, movieId);
        bundle.putIntegerArrayList(NOW_PLAYING_IDS, nowPlayingIds);
        movieDetailFragment.setArguments(bundle);
        return movieDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(MOVIE_ID);
            nowPlayingIds = getArguments().getIntegerArrayList(NOW_PLAYING_IDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewOriginalTitle = view.findViewById(R.id.textViewOriginalTitle);
        textViewReleaseDate = view.findViewById(R.id.textViewReleaseDate);
        textViewRating = view.findViewById(R.id.textViewRating);
        textViewStatus = view.findViewById(R.id.textViewStatus);
        imageViewPoster = view.findViewById(R.id.imageViewPoster);
        buttonAddToFav = view.findViewById(R.id.buttonAddToFav);
        buttonShowSessions = view.findViewById(R.id.buttonShowSessions);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movie = movieViewModel.getMovieById(movieId);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        textViewStatus.setText(getStatus());
        textViewDescription.setText(movie.getOverview());
        buttonShowSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "Showtimes for " + movie.getTitle();
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
            }
        });
        Picasso.get().load(movie.getFullSmallPosterPath()).into(imageViewPoster); // Повторная загрузка не происходит, picasso берет из своего кеша.
    }

    private String getStatus() {
        String result = "Not at the box office";
        if (nowPlayingIds.contains(movie.getId())) {
            result = "Now playing";
            buttonShowSessions.setVisibility(View.VISIBLE);
        }
        return result;
    }



}
