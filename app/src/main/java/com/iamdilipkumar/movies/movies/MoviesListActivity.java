package com.iamdilipkumar.movies.movies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.iamdilipkumar.movies.movies.utilities.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Launcher activity that shows movies in grid layout
 * Created on 20/03/2017
 *
 * @author dilipkumar4813
 * @version 1.0
 *
 */

public class MoviesListActivity extends AppCompatActivity {

    ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading_data);

        getMoviesList(getString(R.string.sort_popular));
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

    private class MoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
