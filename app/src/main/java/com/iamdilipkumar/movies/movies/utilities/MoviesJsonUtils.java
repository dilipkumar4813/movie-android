package com.iamdilipkumar.movies.movies.utilities;

import com.iamdilipkumar.movies.movies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created on 21/03/17.
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesJsonUtils {

    public static ArrayList<Movie> getMoviesFromJson(String moviesList) throws JSONException {

        final String MOVIE_RESULTS = "results";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_TITLE = "title";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_VOTE_COUNT = "vote_count";
        final String MOVIE_AVERAGE_VOTE = "vote_average";
        final String MOVIE_LANGUAGE = "original_language";
        final String MOVIE_BACK_DROP = "backdrop_path";

        JSONObject movieList = new JSONObject(moviesList);
        JSONArray movies = movieList.getJSONArray(MOVIE_RESULTS);

        ArrayList<Movie> moviesArray = new ArrayList<>();

        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = movies.getJSONObject(i);
            moviesArray.add(new Movie(movie.getString(MOVIE_TITLE),movie.getString(MOVIE_OVERVIEW),
                    movie.getString(MOVIE_POSTER),movie.getString(MOVIE_VOTE_COUNT),movie.getString(MOVIE_RELEASE_DATE)
            ,movie.getString(MOVIE_AVERAGE_VOTE),movie.getString(MOVIE_LANGUAGE),movie.getString(MOVIE_BACK_DROP)));
        }

        return moviesArray;
    }
}
