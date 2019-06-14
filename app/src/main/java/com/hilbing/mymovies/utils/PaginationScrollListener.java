package com.hilbing.mymovies.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;



public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager linearLayoutManager;

    public PaginationScrollListener(LinearLayoutManager linearLayoutManager){
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && isLastPage()){
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
