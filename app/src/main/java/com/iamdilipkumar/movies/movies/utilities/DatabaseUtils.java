package com.iamdilipkumar.movies.movies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.iamdilipkumar.movies.movies.data.MoviesContract;
import com.iamdilipkumar.movies.movies.models.Movie;
import com.iamdilipkumar.movies.movies.utilities.network.NetworkUtils;

import java.util.ArrayList;

/**
 * Created on 22/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class DatabaseUtils {

    /**
     * Method to read from the favourites database
     * Create an array list of movie and return for the adapter
     *
     * @param context - used for accessing content resolver
     * @return - returns an array list of movie class
     */
    public static ArrayList<Movie> getMoviesFromFavourites(Context context) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            Cursor cursor = context.getContentResolver().query(MoviesContract.MoviesTable.CONTENT_URI,
                    null,
                    null,
                    null,
                    MoviesContract.MoviesTable._ID);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    movies.add(new Movie(
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_POSTER)),
                            cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_MOVIE_ID)),
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_POSTER)),
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_LANGUAGE)),
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_BACKDROP)),
                            cursor.getDouble(cursor.getColumnIndex(MoviesContract.MoviesTable.COLUMN_AVERAGE_VOTE))
                    ));

                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return movies;
    }

    /**
     * Method to check if the movie is found in favourites
     *
     * @param context - used to access content resolver
     * @param movieId - used for selection in database
     * @return - returns true if movie found else false
     */
    public static boolean checkIfMovieFavourite(Context context, int movieId) {
        boolean liked = false;

        try {
            String stringId = Integer.toString(movieId);
            Uri uri = MoviesContract.MoviesTable.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            Cursor cursor = context.getContentResolver().query(uri,
                    null,
                    null,
                    null,
                    null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    liked = true;
                }
                cursor.close();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return liked;
    }

    /**
     * Method to add a movie to the favourite database table
     *
     * @param context - used to access the content resolver
     * @param movie   - Object that should used to adding content to the database table
     */
    public static void addMovieToFavourites(Context context, Movie movie) {
        ContentValues cv = new ContentValues();

        String poster = movie.getPosterPath();
        String cutUrl = NetworkUtils.MOVIE_IMAGE_BASE_URL + NetworkUtils.MOVIE_POSTER_SIZE;
        poster = poster.substring(cutUrl.length());

        cv.put(MoviesContract.MoviesTable.COLUMN_TITLE, movie.getTitle());
        cv.put(MoviesContract.MoviesTable.COLUMN_PLOT, movie.getOverview());
        cv.put(MoviesContract.MoviesTable.COLUMN_AVERAGE_VOTE, movie.getVoteAverage());
        cv.put(MoviesContract.MoviesTable.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MoviesContract.MoviesTable.COLUMN_POSTER, poster);
        cv.put(MoviesContract.MoviesTable.COLUMN_LANGUAGE, movie.getOriginalLanguage());
        cv.put(MoviesContract.MoviesTable.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MoviesContract.MoviesTable.COLUMN_BACKDROP, movie.getBackdropPath());

        Uri uri = context.getContentResolver().insert(MoviesContract.MoviesTable.CONTENT_URI, cv);

        if (uri == null) {
            Toast.makeText(context, "Could not insert data", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method remove the movie object from the favourite list
     *
     * @param context - used to access content resolver
     * @param id      - the movie id that is used for deletion
     */
    public static void removeMovieFromFavourites(Context context, int id) {
        String stringId = Integer.toString(id);
        Uri uri = MoviesContract.MoviesTable.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        context.getContentResolver().delete(uri, null, null);
    }
}
