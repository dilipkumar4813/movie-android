package com.iamdilipkumar.movies.movies.utilities.network;

import com.iamdilipkumar.movies.movies.models.MoviesResult;
import com.iamdilipkumar.movies.movies.models.ReviewsResult;
import com.iamdilipkumar.movies.movies.models.TrailersResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created on 15/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public interface MoviesInterface {

    @GET("{directory}")
    Observable<MoviesResult> getMovies(@Path("directory") String directory, @Query(NetworkUtils.MOVIES_PAGE) String pageNo);

    @GET("{movie_id}/reviews")
    Observable<ReviewsResult> getReviews(@Path("movie_id") String movieId);

    @GET("{movie_id}/videos")
    Observable<TrailersResult> getTrailers(@Path("movie_id") String movieId);
}
