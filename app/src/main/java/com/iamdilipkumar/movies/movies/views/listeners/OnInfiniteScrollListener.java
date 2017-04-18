package com.iamdilipkumar.movies.movies.views.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created on 17/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class OnInfiniteScrollListener extends RecyclerView.OnScrollListener {

    private InfiniteScrollListener mInfiniteScrollListener;
    private GridLayoutManager mGridLayoutManager;

    public OnInfiniteScrollListener(InfiniteScrollListener infiniteScrollListener, GridLayoutManager gridLayoutManager) {
        this.mInfiniteScrollListener = infiniteScrollListener;
        this.mGridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        int initialItemCount = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
        int totalItemCount = mGridLayoutManager.getItemCount();
        int lastVisisbleItemPosition = mGridLayoutManager.findLastCompletelyVisibleItemPosition();

        if((totalItemCount - 1 == lastVisisbleItemPosition) && (initialItemCount > 0)){
            mInfiniteScrollListener.loadMoreData();
        }
    }
}
