package com.iamdilipkumar.movies.movies.views;

import android.support.v7.widget.RecyclerView;

/**
 * Created by dilipkumar4813 on 17/04/17.
 */

public class OnInfiniteScrollListener extends RecyclerView.OnScrollListener {

    InfiniteScrollListener mInfiniteScrollListener;

    public OnInfiniteScrollListener(InfiniteScrollListener infiniteScrollListener){
        this.mInfiniteScrollListener = infiniteScrollListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        mInfiniteScrollListener.loadMoreData();
    }

    public interface InfiniteScrollListener{
        void loadMoreData();
    }
}
