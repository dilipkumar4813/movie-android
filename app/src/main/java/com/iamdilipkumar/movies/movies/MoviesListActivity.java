package com.iamdilipkumar.movies.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.iamdilipkumar.movies.movies.utilities.MoviesJsonUtils;
import com.iamdilipkumar.movies.movies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * Launcher activity that shows movies in grid layout
 * Created on 20/03/2017
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesListActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {

    @BindView(R.id.pb_loading_data)
    ProgressBar mLoading;

    @BindView(R.id.tv_loading_message)
    TextView mErrorText;

    @BindView(R.id.rv_movies_list)
    RecyclerView mMoviesList;

    @BindView(R.id.s_sort_options)
    Spinner mSortSpinner;

    @OnItemSelected(R.id.s_sort_options)
    void spinnerItemSelected(Spinner spinner, int position) {
        String sort = spinner.getItemAtPosition(position).toString();

        if (sort.equals(spinner.getItemAtPosition(0))) {
            setTitle(getString(R.string.spinner_popular));
            getMoviesList(getString(R.string.sort_popular));
        } else if (sort.equals(spinner.getItemAtPosition(1))) {
            setTitle(getString(R.string.spinner_top_rated));
            getMoviesList(getString(R.string.sort_top_rated));
        } else {
            Toast.makeText(this, sort, Toast.LENGTH_SHORT).show();
        }
    }

    MoviesAdapter mAdapter;
    ArrayList<Movie> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        ButterKnife.bind(this);

        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(mGridLayoutManager);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortSpinner.setAdapter(adapter);

        RecyclerView.OnScrollListener recyclerViewScroll = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mGridLayoutManager.getItemCount();
                int lastVisisbleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount - 1 == lastVisisbleItemPosition) {
                    Log.d("dilip", " Total" + totalItemCount + " Last visible position" + lastVisisbleItemPosition);
                }
            }
        };

        mMoviesList.addOnScrollListener(recyclerViewScroll);

        //mMoviesList.setItemAnimator(new SlideIn);
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
                shareText();
                break;
        }

        return true;
    }

    /**
     * Method to share the data over various app installed on the device
     */
    private void shareText() {
        String mimeType = getString(R.string.mime_type);
        String title = getString(R.string.share_title);
        String textToShare = getString(R.string.share_content);
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }

    /**
     * Method to hide the error message and show the movies list
     */
    private void showList() {
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
     * Method to build the URL and call the API
     * Constructs the URL (using {@link NetworkUtils})
     *
     * @param sortOrder the order in which movies have to be sorted
     */
    private void getMoviesList(String sortOrder) {
        try {
            URL url = NetworkUtils.buildUrl(sortOrder);
            new MoviesQueryTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        movieIntent.putExtra("title", movie.getMovieTitle());
        movieIntent.putExtra("plot", movie.getMoviePlot());
        movieIntent.putExtra("banner", movie.getMovieBanner());
        movieIntent.putExtra("poster", movie.getMoviePoster());
        movieIntent.putExtra("release", movie.getMovieRelease());
        movieIntent.putExtra("language", movie.getMovieLanguage());
        movieIntent.putExtra("votes", movie.getMovieVoteCount());
        movieIntent.putExtra("average", movie.getMovieVoteAverage());
        startActivity(movieIntent);
    }

    /**
     * Async task for network connection
     * TMDB api call {@link NetworkUtils} and parse the json object
     */
    private class MoviesQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        MoviesAdapter.MovieItemClickListener mMovieItemClickListener;

        MoviesQueryTask(MoviesAdapter.MovieItemClickListener movieItemClickListener) {
            mMovieItemClickListener = movieItemClickListener;
        }

        @Override
        protected void onPreExecute() {
            mErrorText.setVisibility(View.INVISIBLE);
            mLoading.setVisibility(View.VISIBLE);
            doNotShowList();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL searchUrl = params[0];
            try {
                String moviesResults = NetworkUtils.getMoviesListFromHttpUrl(searchUrl);
                try {
                    mMovies = MoviesJsonUtils.getMoviesFromJson(moviesResults);
                    return mMovies;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoading.setVisibility(View.INVISIBLE);

            if (movies != null) {
                mAdapter = new MoviesAdapter(movies, mMovieItemClickListener);
                mMoviesList.setAdapter(mAdapter);
                showList();
            } else {
                mErrorText.setVisibility(View.VISIBLE);
            }
        }
    }
}
