package com.example.android.mymovies2.screens.movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.mymovies2.Constants;
import com.example.android.mymovies2.api.ApiSearchFactory;
import com.example.android.mymovies2.api.ApiService;
import com.example.android.mymovies2.pojo.SearchResponse;
import com.example.android.mymovies2.pojo.SearchResult;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends AndroidViewModel {

    private MutableLiveData<List<SearchResult>> searchResults;
    private MutableLiveData<Throwable> errors;
    private CompositeDisposable compositeDisposable;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchResults = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<List<SearchResult>> getSearchResults() {
        return searchResults;
    }

    public void doSearch(String searchQuery) {
        ApiSearchFactory apiSearchFactory = ApiSearchFactory.getInstance();
        ApiService apiService = apiSearchFactory.getApiService();
        Disposable disposable = apiService.getSearchResults(Constants.API_KEY, Constants.LANGUAGE, searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResponse>() {
                    @Override
                    public void accept(SearchResponse searchResponse) throws Exception {
                        searchResults.setValue(searchResponse.getResults());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
