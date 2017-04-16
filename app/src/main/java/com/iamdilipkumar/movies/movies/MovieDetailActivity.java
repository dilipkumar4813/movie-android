package com.iamdilipkumar.movies.movies;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.iamdilipkumar.movies.movies.databinding.ActivityMovieDetailBinding;
import com.iamdilipkumar.movies.movies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Class to show the details of the movie
 * By using the data that was passed from {@link MoviesListActivity#onMovieItemClick(int)}
 * <p>
 * Created on 21/03/2017
 *
 * @author dilipkumar4813
 * @version 2.0
 */
public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_poster)
    ImageView mPosterImage;
    @BindView(R.id.iv_movie_banner)
    ImageView mBannerImage;

    CompositeDisposable mCompositeDisposable;

    protected static final String PARAMS_MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ActivityMovieDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie != null) {
            Picasso.with(this).load(movie.getBackdropPath()).into(mBannerImage);
            Picasso.with(this).load(movie.getPosterPath()).into(mPosterImage);
            binding.setMovie(movie);
            loadTrailersAndReviews();
        }
    }

    private void loadTrailersAndReviews() {

    }

    private void responseTrailers() {

    }

    private void responseReviews() {

    }

    private void responesError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
