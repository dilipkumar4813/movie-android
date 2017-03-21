package com.iamdilipkumar.movies.movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 21/03/17.
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesJsonUtils {

    public static String[] getMoviesFromJson(String moviesList) throws JSONException {

        final String MOVIE_RESULTS = "results";

        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_TITLE = "title";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_VOTE_COUNT = "vote_count";
        final String MOVIE_AVERAGE_VOTE = "vote_average";
        final String MOVIE_LANGUAGE = "original_language";

        JSONObject movieList = new JSONObject(moviesList);
        JSONArray movies = movieList.getJSONArray(MOVIE_RESULTS);

        String[] parsedMovies = new String[movies.length()];

        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = movies.getJSONObject(i);
            parsedMovies[i] = movie.getString(MOVIE_TITLE) + "\n" + movie.getString(MOVIE_OVERVIEW)
                    + "\n" + movie.getString(MOVIE_RELEASE_DATE) + "\n" + movie.getString(MOVIE_VOTE_COUNT)
                    + "\n" + movie.getString(MOVIE_AVERAGE_VOTE) + "\n" + movie.getString(MOVIE_POSTER)
                    + "\n" + movie.getString(MOVIE_LANGUAGE);
        }

        return parsedMovies;
    }
}
