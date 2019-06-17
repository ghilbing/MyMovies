package com.hilbing.mymovies.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    RecyclerView.LayoutManager mLayoutManager;

    private int minVisibleItems = 5;
    private int currentPage = 1;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

        GridLayoutManager gridLayoutManager;

    public PaginationScrollListener(GridLayoutManager gridLayoutManager){
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = gridLayoutManager.getChildCount();
        int totalItemCount = gridLayoutManager.getItemCount();
        int firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()){
            if ((visibleItemCount + firstVisiblePosition) >= totalItemCount
                    && firstVisiblePosition >= 0
                    && totalItemCount >= getTotalPageCount()){
                loadMoreItems();
            }
        }

    }

    protected abstract void loadMoreItems();
    public abstract int getTotalPageCount();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}
