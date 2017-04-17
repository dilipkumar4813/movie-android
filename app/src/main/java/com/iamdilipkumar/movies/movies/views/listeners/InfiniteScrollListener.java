package com.iamdilipkumar.movies.movies.views.listeners;

/**
 * Created on 17/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public interface InfiniteScrollListener {
    void loadMoreData(int initialItem, int totalItems, int visibleItem);
}
