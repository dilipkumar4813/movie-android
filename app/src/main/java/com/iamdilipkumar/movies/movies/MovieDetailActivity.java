package com.iamdilipkumar.movies.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.iamdilipkumar.movies.movies.adapters.ReviewsAdapter;
import com.iamdilipkumar.movies.movies.adapters.TrailersAdapter;
import com.iamdilipkumar.movies.movies.databinding.ActivityMovieDetailBinding;
import com.iamdilipkumar.movies.movies.models.Movie;
import com.iamdilipkumar.movies.movies.models.Review;
import com.iamdilipkumar.movies.movies.models.ReviewsResult;
import com.iamdilipkumar.movies.movies.models.Trailer;
import com.iamdilipkumar.movies.movies.models.TrailersResult;
import com.iamdilipkumar.movies.movies.utilities.MoviesInterface;
import com.iamdilipkumar.movies.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Class to show the details of the movie
 * By using the data that was passed from {@link MoviesListActivity#onMovieItemClick(int)}
 * <p>
 * Created on 21/03/2017
 *
 * @author dilipkumar4813
 * @version 2.0
 */
public class MovieDetailActivity extends AppCompatActivity implements TrailersAdapter.TrailerItemClickListener {

    @BindView(R.id.iv_movie_poster)
    ImageView mPosterImage;

    @BindView(R.id.iv_movie_banner)
    ImageView mBannerImage;

    @BindView(R.id.rv_trailers)
    RecyclerView mTrailersList;

    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsList;


    private CompositeDisposable mCompositeDisposable;

    protected static final String PARAMS_MOVIE = "movie";

    private List<Trailer> mTrailers;
    TrailersAdapter mTrailersAdapter;

    List<Review> mReviews;
    ReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ActivityMovieDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        LinearLayoutManager layoutManagerTrailers = new LinearLayoutManager(getApplicationContext());
        layoutManagerTrailers.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecorationTrailers = new DividerItemDecoration(this,
                layoutManagerTrailers.getOrientation());
        mTrailersList.setLayoutManager(layoutManagerTrailers);
        mTrailersList.addItemDecoration(dividerItemDecorationTrailers);

        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(getApplicationContext());
        layoutManagerReviews.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecorationReviews = new DividerItemDecoration(this,
                layoutManagerReviews.getOrientation());
        mReviewsList.setLayoutManager(layoutManagerReviews);
        mReviewsList.addItemDecoration(dividerItemDecorationReviews);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie != null) {
            Picasso.with(this).load(movie.getBackdropPath()).into(mBannerImage);
            Picasso.with(this).load(movie.getPosterPath()).into(mPosterImage);
            binding.setMovie(movie);
            loadTrailersAndReviews(String.valueOf(movie.getId()));
        }
    }

    private void loadTrailersAndReviews(String movieId) {

        MoviesInterface moviesInterface = NetworkUtils.buildRetrofit().create(MoviesInterface.class);

        mCompositeDisposable.add(moviesInterface.getTrailers(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::responseTrailers, this::responseError));

        mCompositeDisposable.add(moviesInterface.getReviews(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::responseReviews, this::responseError));
    }

    private void responseTrailers(TrailersResult result) {
        mTrailers = result.getResults();
        mTrailersAdapter = new TrailersAdapter(mTrailers, this);
        mTrailersList.setAdapter(mTrailersAdapter);
        mTrailersAdapter.notifyDataSetChanged();
    }

    private void responseReviews(ReviewsResult result) {
        mReviews = result.getResults();

        mReviewsAdapter = new ReviewsAdapter(mReviews);
        mReviewsList.setAdapter(mReviewsAdapter);
        mReviewsAdapter.notifyDataSetChanged();
    }

    private void responseError(Throwable error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    public void onTrailerClick(int position) {
        Trailer trailer = mTrailers.get(position);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(NetworkUtils.YOUTUBE_BASE_URL + trailer.getKey()));
        startActivity(i);
    }
}
