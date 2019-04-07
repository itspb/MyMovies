package com.example.android.mymovies2.api;

import com.example.android.mymovies2.BuildConfig;
import com.example.android.mymovies2.Constants;
import com.example.android.mymovies2.pojo.MovieResponse;
import com.example.android.mymovies2.pojo.SearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie?")
    // В скобках этого метода можно передавать параметры для запроса к API. @Query("api_key") int apiKey, ... и т.д., их может быть сколько угодно.
    Observable<MovieResponse> getMovies(@Query("api_key") String apiKey,
                                        @Query("language") String language,
                                        @Query("sort_by") String sortBy,
                                        @Query("page") int page);

    @GET("multi?")
    Observable<SearchResponse> getSearchResults(@Query("api_key") String apiKey,
                                                @Query("language") String language,
                                                @Query("query") String searchQuery);
}
