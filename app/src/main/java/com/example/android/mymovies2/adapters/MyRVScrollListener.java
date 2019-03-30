package com.example.android.mymovies2.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class MyRVScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4; // Количество элементов, которое должно быть ниже текущей позиции прокрутки в RV, прежде чем загружать дальше.
    private int totalItemCount, lastVisibleItem;
    private int currentPage = 1;

    private GridLayoutManager gridLayoutManager;

    public MyRVScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.i("Scroll", "Page: " + currentPage +
                        ", totalItems: " + totalItemCount +
                        ", lastVisibleItem: " + lastVisibleItem +
                        ", loading: " + loading +
                        ", prevTotal: " + previousTotal);
        totalItemCount = gridLayoutManager.getItemCount(); // Количество элементов, привязанных к родительскому RecyclerView.
        lastVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition(); // Позиция последнего видимого(целиком) элемента
        //currentPage = totalItemCount / 20;
        //if (dy > 0) { }
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount <= (lastVisibleItem + visibleThreshold))) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);

}
