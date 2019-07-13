package com.example.android.mymovies2.screens.movies;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.android.mymovies2.pojo.TV;
import com.example.android.mymovies2.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    private Movie movie;
    private TV tv;
    private boolean isMovie;
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
    //private MovieViewModel movieViewModel;

    public static MovieDetailFragment newInstance(Parcelable parcelable) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcelable", parcelable);
        movieDetailFragment.setArguments(bundle);
        return movieDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Parcelable parcelable = getArguments().getParcelable("parcelable");
            if (parcelable instanceof Movie) {
                movie = (Movie) parcelable;
                isMovie = true;
            } else if (parcelable instanceof TV) {
                tv = (TV) parcelable;
                isMovie = false;
            }
        }
        nowPlayingIds = JsonUtils.nowPlayingIds;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        //movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        if (isMovie) fillViewsForMovie();
            else fillViewsForTV();

        buttonShowSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "Showtimes for " + movie.getTitle();
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
            }
        });

    }

    private String getStatus() {
        String result = "Not at the box office";
        if (nowPlayingIds.contains(movie.getId())) {
            result = "Now playing";
            buttonShowSessions.setVisibility(View.VISIBLE);
        }
        return result;
    }

    private void fillViewsForMovie() {
        Picasso.get().load(movie.getFullSmallPosterPath()).into(imageViewPoster); // Повторная загрузка не происходит, picasso берет из своего кеша.
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewReleaseDate.setText(movie.getReleaseDate());
        String voteAverage = Double.toString(movie.getVoteAverage());
        textViewRating.setText(voteAverage);
        textViewStatus.setVisibility(View.VISIBLE);
        textViewStatus.setText(getStatus());
        textViewDescription.setText(movie.getOverview());
    }
    private void fillViewsForTV() {
        Picasso.get().load(tv.getFullSmallPosterPath()).into(imageViewPoster);
        textViewTitle.setText(tv.getName());
        textViewOriginalTitle.setText(tv.getOriginalName());
        textViewReleaseDate.setText(tv.getFirstAirDate());
        String voteAverage = Double.toString(tv.getVoteAverage());
        textViewRating.setText(voteAverage);
        textViewDescription.setText(tv.getOverview());
    }



}
