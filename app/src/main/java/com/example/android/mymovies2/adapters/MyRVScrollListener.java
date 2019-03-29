package com.example.android.mymovies2.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class MyRVScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // Общее кол-во фильмов после последней загрузки
    private boolean loading = true; // True если мы ждем загрузки данных
    private int visibleThreshold = 4; // Минимальное количество элементов, которое должно быть ниже текущей позиции прокрутки в RV, прежде чем загружать дальше.
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int currentPage = 1;

    private GridLayoutManager gridLayoutManager;

    public MyRVScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.i("Scroll", "Page: " + currentPage +
                        ", visibleItems: " + visibleItemCount +
                        ", totalItems: " + totalItemCount +
                        ", firstVisibleItem: " + firstVisibleItem +
                        ", prevTotal: " + previousTotal);
        visibleItemCount = recyclerView.getChildCount(); // Число видимых элементов RecyclerView
        totalItemCount = gridLayoutManager.getItemCount(); // Количество элементов, привязанных к родительскому RecyclerView.
        firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition(); // Возвращает позицию адаптера на первом видимом View.
        if (dy > 0) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount <= (firstVisibleItem + visibleThreshold))) {
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }
        }
    }

    public abstract void onLoadMore(int currentPage);
}
