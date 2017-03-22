package com.iamdilipkumar.movies.movies.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created on 20/03/17.
 * @author dilipkumar4813
 * @version 1.0
 *
 * This utility class will be used to connect with the movies api
 * Fetch the results based on sort order
 */

public class NetworkUtils {

    private final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public final static String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    public final static String MOVIE_POSTER_SIZE = "w185";

    public final static String MOVIE_BANNER_SIZE = "w500";

    private final static String PARAM_API = "api_key";

    // Add the movie API key for TMDB
    private final static String API_KEY = "";

    /**
     *  Builds the url with the sort order and api key
     *
     * @param sortingOrder string that specifies the sort order
     * @return URL returns the built URL
     * @throws MalformedURLException Related to malformed URL
     */
    public static URL buildUrl(String sortingOrder) throws MalformedURLException {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL+sortingOrder).buildUpon()
                .appendQueryParameter(PARAM_API, API_KEY)
                .build();

        return new URL(builtUri.toString());
    }

    /**
     * Fetch data from the url provided
     *
     * @param url The url from which the data has to be
     * @return String response from the url
     * @throws IOException Related to network and stream reading
     */
    public static String getMoviesListFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasResults = scanner.hasNext();

            if (hasResults) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

}
