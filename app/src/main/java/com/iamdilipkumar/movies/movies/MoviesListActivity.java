package com.iamdilipkumar.movies.movies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iamdilipkumar.movies.movies.utilities.MoviesJsonUtils;
import com.iamdilipkumar.movies.movies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Launcher activity that shows movies in grid layout
 * Created on 20/03/2017
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesListActivity extends AppCompatActivity {

    ProgressBar mLoading;
    TextView mDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading_data);
        mDisplayTextView = (TextView) findViewById(R.id.tv_display_data);

        getMoviesList(getString(R.string.sort_popular));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_movies_popular:
                getMoviesList(getString(R.string.sort_popular));
                break;
            case R.id.action_movies_top:
                getMoviesList(getString(R.string.sort_top_rated));
                break;
            case R.id.action_share:
                break;
        }

        return true;
    }

    private void showList() {
        mDisplayTextView.setVisibility(View.VISIBLE);
    }

    private void doNotShowList() {
        mDisplayTextView.setVisibility(View.INVISIBLE);
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
            new MoviesQueryTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Async task for network connection
     * TMDB api call {@link NetworkUtils} and parse the json object
     */
    private class MoviesQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            mLoading.setVisibility(View.VISIBLE);
            doNotShowList();
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(URL... params) {
            URL searchUrl = params[0];
            try {
                String moviesResults = NetworkUtils.getMoviesListFromHttpUrl(searchUrl);
                try {
                    return MoviesJsonUtils.getMoviesFromJson(moviesResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] movies) {
            mLoading.setVisibility(View.INVISIBLE);

            if (movies != null) {
                for (String movie : movies) {
                    mDisplayTextView.append(movie + "\n\n\n");
                }

                showList();
            }
        }
    }
}
