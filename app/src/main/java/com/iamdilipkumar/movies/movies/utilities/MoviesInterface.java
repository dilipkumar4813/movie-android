package com.iamdilipkumar.movies.movies.utilities;

import com.iamdilipkumar.movies.movies.models.MoviesResult;

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
}
