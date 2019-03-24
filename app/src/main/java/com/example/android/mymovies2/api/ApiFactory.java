package com.example.android.mymovies2.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static ApiFactory apiFactory;
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";

    private ApiFactory() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())         // конвертер json
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // используем rxjava для отслеживания процесса получения данных
                .baseUrl(BASE_URL)                                          // добавляем базовый URL, он должен заканчиваться на слеш
                .build();

    };

    public static ApiFactory getInstance() {
        if (apiFactory == null) {
            apiFactory = new ApiFactory();
        }
        return apiFactory;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
