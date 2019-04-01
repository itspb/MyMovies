package com.example.android.mymovies2.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class MyRVScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 6; // Количество элементов, которое должно быть ниже текущей позиции прокрутки в RV, прежде чем загружать дальше.
    private int totalItemCount, lastVisibleItem;

    private GridLayoutManager gridLayoutManager;

    public MyRVScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.i("Scroll", "totalItems: " + totalItemCount +
                ", lastVisibleItem: " + lastVisibleItem);
        totalItemCount = gridLayoutManager.getItemCount(); // Количество элементов, привязанных к родительскому RecyclerView.
        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition(); // Позиция последнего видимого элемента
        if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();

}
