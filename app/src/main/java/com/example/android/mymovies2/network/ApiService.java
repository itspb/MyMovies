package com.example.android.mymovies2.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
//    @GET("movie?")
//    // В скобках этого метода можно передавать параметры для запроса к API. @Query("api_key") int apiKey, ... и т.д., их может быть сколько угодно.
//    Observable<MovieResponse> getMovies(@Query("api_key") String apiKey,
//                                        @Query("language") String language,
//                                        @Query("sort_by") String sortBy,
//                                        @Query("page") int page);
        @GET("movie?")
        Call<ResponseBody> getMovies(@Query("api_key") String apiKey,
                                     @Query("language") String language,
                                     @Query("sort_by") String sortBy,
                                     @Query("page") int page);
}
