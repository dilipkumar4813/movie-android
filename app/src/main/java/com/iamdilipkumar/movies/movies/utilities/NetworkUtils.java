package com.iamdilipkumar.movies.movies.utilities;

import android.content.Context;

import com.iamdilipkumar.movies.movies.BuildConfig;
import com.iamdilipkumar.movies.movies.R;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    protected final static String MOVIES_PAGE = "page";

    /**
     *
     * @param context - used to access the strings, to fetch API key
     * @return Retrofit - used for building API calls
     */
    public static Retrofit buildRetrofit(final Context context){
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(PARAM_API, context.getString(R.string.movies_api_key))
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        if(BuildConfig.DEBUG) {
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
