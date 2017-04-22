package com.iamdilipkumar.movies.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created on 21/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.iamdilipkumar.movies.movies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TASKS = "favourites";

    public static final class MoviesTable implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_AVERAGE_VOTE = "average";
        public static final String COLUMN_RELEASE_DATE = "release";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_MOVIE_ID = "movieid";

    }
}
