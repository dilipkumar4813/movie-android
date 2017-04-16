package com.iamdilipkumar.movies.movies.utilities;

import com.iamdilipkumar.movies.movies.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This utility class will be used to connect with the movies api
 * Fetch the results based on sort order
 * <p>
 * Created on 20/03/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class NetworkUtils {

    private final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public final static String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    public final static String MOVIE_POSTER_SIZE = "w185";

    public final static String MOVIE_BANNER_SIZE = "w500";

    public final static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    final static String MOVIES_PAGE = "page";

    /**
     * Method to create a new Retrofit instance
     * Add the API key {@link MoviesApiInterceptor#intercept(Interceptor.Chain)}
     * Show debug information only in DEBUG build
     *
     * @return Retrofit - used for building API calls
     */
    public static Retrofit buildRetrofit() {
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new MoviesApiInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(MOVIEDB_BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
