package com.example.android.mymovies2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.pojo.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> {

    private List<Movie> movies;
    private View.OnClickListener onClickListener;

    public MovieAdapter() {
        movies = Collections.emptyList();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void updateMoviesListItems(List<Movie> movies) {
        final MoviesDiffCallback diffCallback = new MoviesDiffCallback(this.movies, movies);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.movies = movies;
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Picasso.get()
                .load(movie.getFullSmallPosterPath())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.imageViewSmallPoster, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("Picasso", "Image from cache");
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(movie.getFullSmallPosterPath()).into(holder.imageViewSmallPoster);
                        Log.i("Picasso", "Image from internet");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewSmallPoster;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            // For onClickListener
            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }
    }
}
