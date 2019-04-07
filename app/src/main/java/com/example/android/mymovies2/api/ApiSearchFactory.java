package com.example.android.mymovies2.api;

import com.example.android.mymovies2.Constants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiSearchFactory {
    private static ApiSearchFactory apiSearchFactory;
    private static Retrofit retrofit;


    private ApiSearchFactory() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())         // конвертер json
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // используем rxjava для отслеживания процесса получения данных
                .baseUrl(Constants.BASE_URL_SEARCH)                         // добавляем базовый URL, он должен заканчиваться на слеш
                .build();

    };

    public static ApiSearchFactory getInstance() {
        if (apiSearchFactory == null) {
            apiSearchFactory = new ApiSearchFactory();
        }
        return apiSearchFactory;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
