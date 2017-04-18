package com.iamdilipkumar.movies.movies;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iamdilipkumar.movies.movies.adapters.MoviesAdapter;
import com.iamdilipkumar.movies.movies.models.Movie;
import com.iamdilipkumar.movies.movies.models.MoviesResult;
import com.iamdilipkumar.movies.movies.utilities.network.MoviesInterface;
import com.iamdilipkumar.movies.movies.utilities.network.NetworkUtils;
import com.iamdilipkumar.movies.movies.utilities.ShareUtils;
import com.iamdilipkumar.movies.movies.views.listeners.OnInfiniteScrollListener;
import com.iamdilipkumar.movies.movies.views.listeners.InfiniteScrollListener;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Launcher activity that shows movies in grid layout
 * Created on 20/03/2017
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesListActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener, InfiniteScrollListener {

    @BindView(R.id.pb_loading_data)
    ProgressBar mLoading;

    @BindView(R.id.tv_loading_message)
    TextView mErrorText;

    @BindView(R.id.rv_movies_list)
    RecyclerView mMoviesList;

    @BindView(R.id.s_sort_options)
    Spinner mSortSpinner;

    @BindString(R.string.sort_popular)
    String sortPopular;

    @BindString(R.string.sort_top_rated)
    String sortTopRated;

    @OnItemSelected(R.id.s_sort_options)
    void spinnerItemSelected(Spinner spinner, int position) {
        String sort = spinner.getItemAtPosition(position).toString();
        sCurrentPage = 1;
        mMovies.clear();

        switch (position) {
            case 0:
                setTitle(sortPopular);
                mSortOrder = sortPopular;
                getMoviesList(sortPopular);
                break;
            case 1:
                setTitle(sortTopRated);
                mSortOrder = sortTopRated;
                getMoviesList(sortTopRated);
                break;
            case 2:
                Toast.makeText(this, sort, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    CompositeDisposable mCompositeDisposable;

    private MoviesAdapter mAdapter;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private boolean mLoadMore = false;
    GridLayoutManager mGridLayoutManager;
    private String mSortOrder;

    private static int sCurrentPage = 1;
    private static int sTotalPages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == MoviesAdapter.ITEM_TYPE_LOADING) {
                    return 2;
                }
                return 1;
            }
        });
        mMoviesList.setLayoutManager(mGridLayoutManager);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortSpinner.setAdapter(adapter);

        mAdapter = new MoviesAdapter(mMovies, this);
        mMoviesList.setAdapter(mAdapter);
        RecyclerView.OnScrollListener recyclerViewScroll = new OnInfiniteScrollListener(this, mGridLayoutManager);
        mMoviesList.addOnScrollListener(recyclerViewScroll);
    }

    /**
     * Method to inflate the menu created for the activity
     *
     * @param menu used for creating the menu
     * @return will always return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Method to handle the clicks on the menu
     *
     * @param item the item in the menu that was clicked
     * @return will always return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ShareUtils.shareApp(this);
                break;
        }

        return true;
    }

    /**
     * Method to hide the error message and show the movies list
     */
    private void showList() {
        mLoading.setVisibility(View.GONE);
        mErrorText.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
    }

    /**
     * Method to hide the list
     */
    private void doNotShowList() {
        mMoviesList.setVisibility(View.INVISIBLE);
    }

    /**
     * Method to Connect and retrieve JSON response
     * Constructs retrofit (using {@link NetworkUtils})
     *
     * @param sortOrder the order in which movies have to be sorted
     */
    private void getMoviesList(String sortOrder) {
        if (sCurrentPage == 1) {
            doNotShowList();
            mLoading.setVisibility(View.VISIBLE);
        }

        MoviesInterface moviesInterface = NetworkUtils.buildRetrofit().create(MoviesInterface.class);

        mCompositeDisposable.add(moviesInterface.getMovies(sortOrder, String.valueOf(sCurrentPage))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::apiResponse, this::apiError));
    }

    /**
     * Method to read json response and parse the same
     * by passing the objects to the adapter
     *
     * @param movies - Main GSON Object class
     */
    private void apiResponse(MoviesResult movies) {
        showList();
        hideLoadingItem();

        sTotalPages = movies.getTotalPages();
        sCurrentPage++;
        mMovies.addAll(movies.getResults());
        mAdapter.notifyDataSetChanged();
        mLoadMore = true;
    }

    private void apiError(Throwable error) {
        hideLoadingItem();
        Toast.makeText(this, "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void hideLoadingItem() {
        if (mMovies.size() > 0) {
            if (mAdapter.getItemViewType(mMovies.size() - 1) == MoviesAdapter.ITEM_TYPE_LOADING) {
                mMovies.remove(mMovies.size() - 1);
                mAdapter.notifyItemRemoved(mMovies.size() - 1);
            }
        }
    }

    /**
     * Implemeting the interface created in the MoviesAdapter {@link MoviesAdapter}
     *
     * @param position based on poisting passing data to detail activity
     */
    @Override
    public void onMovieItemClick(int position) {
        Movie movie = mMovies.get(position);
        Intent movieIntent = new Intent(this, MovieDetailActivity.class);
        movieIntent.putExtra(MovieDetailActivity.PARAMS_MOVIE, movie);
        startActivity(movieIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    public void loadMoreData() {
        if (mLoadMore && (sTotalPages >= sCurrentPage)) {
            mLoadMore = false;
            mMovies.add(null);
            mAdapter.notifyItemInserted(mMovies.size() - 1);
            getMoviesList(mSortOrder);
        } else {
            mLoadMore = false;
        }
    }
}
