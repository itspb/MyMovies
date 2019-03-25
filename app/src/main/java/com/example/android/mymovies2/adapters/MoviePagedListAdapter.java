package com.example.android.mymovies2.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mymovies2.R;
import com.example.android.mymovies2.network.NetworkState;
import com.example.android.mymovies2.pojo.Movie;
import com.squareup.picasso.Picasso;

public class MoviePagedListAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    public static final int MOVIE_ITEM_VIEW_TYPE = 1;
    public static final int LOAD_ITEM_VIEW_TYPE  = 0;
    private Context mContext;
    private NetworkState mNetworkState;

    public MoviePagedListAdapter(Context context) {
        super(Movie.DIFF_CALL);
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return ( isLoadingData() && position == getItemCount()-1 ) ? LOAD_ITEM_VIEW_TYPE : MOVIE_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == MOVIE_ITEM_VIEW_TYPE) {
            view = inflater.inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.progress_bar, parent, false);
            return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Image image;
        if (holder instanceof MovieViewHolder) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Movie movie = getItem(position);
            movieViewHolder.bind(movie);
        }
    }

    public void setNetworkState(NetworkState networkState) {
        NetworkState prevState = networkState;
        boolean wasLoading = isLoadingData();
        mNetworkState = networkState;
        boolean willLoad = isLoadingData();
        if (wasLoading != willLoad) {
            if (wasLoading) notifyItemRemoved(getItemCount());
                else notifyItemInserted(getItemCount());
        }
    }

    public boolean isLoadingData(){
        return  ( mNetworkState != null && mNetworkState != NetworkState.LOADED);
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewSmallPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
        }
        public void bind(Movie movie) {
            Picasso.get().load(movie.getFullSmallPosterPath()).into(imageViewSmallPoster);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder{

        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
