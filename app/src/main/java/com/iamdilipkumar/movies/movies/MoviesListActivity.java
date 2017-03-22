package com.iamdilipkumar.movies.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iamdilipkumar.movies.movies.adapters.MoviesAdapter;
import com.iamdilipkumar.movies.movies.models.Movie;
import com.iamdilipkumar.movies.movies.utilities.MoviesJsonUtils;
import com.iamdilipkumar.movies.movies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Launcher activity that shows movies in grid layout
 * Created on 20/03/2017
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesListActivity extends AppCompatActivity implements MoviesAdapter.MovieItemClickListener {

    ProgressBar mLoading;
    TextView mErrorText;

    RecyclerView mMoviesList;

    MoviesAdapter mAdapter;
    ArrayList<Movie> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading_data);
        mErrorText = (TextView) findViewById(R.id.tv_loading_message);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies_list);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(mGridLayoutManager);

        setTitle(getString(R.string.action_popular));
        getMoviesList(getString(R.string.sort_popular));
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
            case R.id.action_movies_popular:
                setTitle(getString(R.string.action_popular));
                getMoviesList(getString(R.string.sort_popular));
                break;
            case R.id.action_movies_top:
                setTitle(getString(R.string.action_top_rated));
                getMoviesList(getString(R.string.sort_top_rated));
                break;
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
        String mimeType = "text/plain";
        String title = "Share movie app";
        String textToShare = "Check out the coolest app to view the most popular and top rated apps";
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
