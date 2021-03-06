package com.iamdilipkumar.movies.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iamdilipkumar.movies.movies.adapters.ReviewsAdapter;
import com.iamdilipkumar.movies.movies.adapters.TrailersAdapter;
import com.iamdilipkumar.movies.movies.databinding.ActivityMovieDetailBinding;
import com.iamdilipkumar.movies.movies.models.Movie;
import com.iamdilipkumar.movies.movies.models.Review;
import com.iamdilipkumar.movies.movies.models.ReviewsResult;
import com.iamdilipkumar.movies.movies.models.Trailer;
import com.iamdilipkumar.movies.movies.models.TrailersResult;
import com.iamdilipkumar.movies.movies.utilities.DatabaseUtils;
import com.iamdilipkumar.movies.movies.utilities.network.MoviesInterface;
import com.iamdilipkumar.movies.movies.utilities.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.tv_trailers_network)
    TextView mTrailersNetworkText;

    @BindView(R.id.rv_trailers)
    RecyclerView mTrailersList;

    @BindView(R.id.tv_reviews_network)
    TextView mReviewsNetworkText;

    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsList;

    @BindString(R.string.trailers_list_empty)
    String mEmptyTrailers;

    @BindString(R.string.reviews_list_empty)
    String mEmptyReviews;

    @BindString(R.string.description_empty)
    String mEmptyDescription;

    @BindView(R.id.sv_details)
    ScrollView mMainScroll;

    @BindView(R.id.iv_heart)
    ImageView mHeart;

    @BindView(R.id.ll_like)
    LinearLayout mLike;

    @BindView(R.id.tv_favourite)
    TextView mFavouriteText;

    @BindString(R.string.added_to_favourite)
    String mAddedToFavourite;

    @BindString(R.string.add_to_favourite)
    String mAddToFavourite;

    @BindView(R.id.iv_banner)
    ImageView mBanner;

    private CompositeDisposable mCompositeDisposable;

    protected static final String PARAMS_MOVIE = "movie";

    private List<Trailer> mTrailers = new ArrayList<>();
    private TrailersAdapter mTrailersAdapter;

    private List<Review> mReviews = new ArrayList<>();
    private ReviewsAdapter mReviewsAdapter;

    private Movie movie;
    private boolean liked = false;

    private final static String STATE_TRAILERS = "trailers_state";
    private final static String STATE_REVIEWS = "reviews_state";
    private final static String STATE_TRAILERS_LOADED = "trailers_load_state";
    private final static String STATE_REVIEWS_LOADED = "reviews_load_state";
    private final static String STATE_SCROLL = "movie_scroll_state";

    private boolean mIsLoadedTrailers = false;
    private boolean mIsLoadedReviews = false;
    private ActivityMovieDetailBinding mBinding;

    @OnClick(R.id.ll_like)
    void addOrRemoveFavourite() {
        if (!liked) {
            liked = true;
            mHeart.setImageResource(R.drawable.ic_heart);
            mHeart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
            mFavouriteText.setText(mAddedToFavourite);
            DatabaseUtils.addMovieToFavourites(this, movie);
        } else {
            liked = false;
            mHeart.setImageResource(R.drawable.ic_heart_stroke);
            mHeart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
            mFavouriteText.setText(mAddToFavourite);
            DatabaseUtils.removeMovieFromFavourites(this, movie.getId());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

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

        loadData(savedInstanceState);
    }

    /**
     * Method to detect the state of the activity
     * If the activity is loaded for the first time then the intent content is read
     * Else data is read from the saved instance state
     *
     * @param savedInstanceState - Accessing the arraylist and movie object for reload
     */
    private void loadData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(PARAMS_MOVIE);
        } else {
            movie = getIntent().getParcelableExtra(PARAMS_MOVIE);
        }

        if (movie != null) {
            Picasso.with(this).load(movie.getPosterPath()).into(mPosterImage);
            Picasso.with(this).load(movie.getBackdropPath()).into(mBanner);

            if (movie.getOverview().isEmpty()) {
                movie.setOverview(mEmptyDescription);
            }
            mBinding.setMovie(movie);

            if (savedInstanceState != null) {

                mIsLoadedTrailers = savedInstanceState.getBoolean(STATE_TRAILERS_LOADED);
                mIsLoadedReviews = savedInstanceState.getBoolean(STATE_REVIEWS_LOADED);

                if (mIsLoadedTrailers && mIsLoadedReviews) {
                    mTrailers = savedInstanceState.getParcelableArrayList(STATE_TRAILERS);
                    mReviews = savedInstanceState.getParcelableArrayList(STATE_REVIEWS);

                    if (mTrailers.size() > 0) {
                        mTrailersNetworkText.setVisibility(View.GONE);

                        mTrailersAdapter = new TrailersAdapter(mTrailers, this);
                        mTrailersList.setAdapter(mTrailersAdapter);
                    } else {
                        mTrailersNetworkText.setText(mEmptyTrailers);
                    }

                    if (mReviews.size() > 0) {
                        mReviewsNetworkText.setVisibility(View.GONE);

                        mReviewsAdapter = new ReviewsAdapter(mReviews);
                        mReviewsList.setAdapter(mReviewsAdapter);
                    } else {
                        mReviewsNetworkText.setText(mEmptyReviews);
                    }
                } else {
                    loadTrailersAndReviews(String.valueOf(movie.getId()));
                }

                int[] scrollPositions = savedInstanceState.getIntArray(STATE_SCROLL);
                assert scrollPositions != null;
                mMainScroll.scrollTo(scrollPositions[0], scrollPositions[1]);

            } else {
                loadTrailersAndReviews(String.valueOf(movie.getId()));
            }
        }

        liked = DatabaseUtils.checkIfMovieFavourite(getBaseContext(), movie.getId());

        if (liked) {
            mFavouriteText.setText(mAddedToFavourite);
            mHeart.setImageResource(R.drawable.ic_heart);
        } else {
            mFavouriteText.setText(mAddToFavourite);
            mHeart.setImageResource(R.drawable.ic_heart_stroke);
        }
    }

    /**
     * Method to call the API by passing the ID of the movie
     * Fetch trailers and reviews using retrofit 2 and rxjava 2
     * {@link NetworkUtils#buildRetrofit()} used to build the retrofit object
     * {@link MoviesInterface#getTrailers(String)} Interface for trailers
     * {@link MoviesInterface#getReviews(String)} Interface for reviews
     *
     * @param movieId - Used to uniquely identify the trailers and reviews
     */
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

    /**
     * Method to parse the returned JSON file and set the
     * adapter for the recycler view and populate the same
     *
     * @param result - Fetch the trailers
     */
    private void responseTrailers(TrailersResult result) {

        ArrayList<Trailer> trailersArrayList = new ArrayList<>();
        mTrailers = result.getResults();

        if (mTrailers.size() > 0) {
            mTrailersNetworkText.setVisibility(View.GONE);

            for (Trailer trailer : result.getResults()) {
                if (trailer.getType().equalsIgnoreCase("Trailer")) {
                    trailersArrayList.add(trailer);
                }
            }

            mTrailers = trailersArrayList;

            mTrailersAdapter = new TrailersAdapter(mTrailers, this);
            mTrailersList.setAdapter(mTrailersAdapter);
            mTrailersAdapter.notifyDataSetChanged();
        } else {
            mTrailersNetworkText.setText(mEmptyTrailers);
        }

        mIsLoadedTrailers = true;
    }

    /**
     * Method to parse the returned JSON file and set the
     * adapter for the recycler view and populate the same
     *
     * @param result - pass for populating the recyclerview
     */
    private void responseReviews(ReviewsResult result) {
        mReviews = result.getResults();

        if (mReviews.size() > 0) {
            mReviewsNetworkText.setVisibility(View.GONE);

            mReviewsAdapter = new ReviewsAdapter(mReviews);
            mReviewsList.setAdapter(mReviewsAdapter);
            mReviewsAdapter.notifyDataSetChanged();
        } else {
            mReviewsNetworkText.setText(mEmptyReviews);
        }

        mIsLoadedReviews = true;
    }

    /**
     * Method to handle the response error response
     * during API calls
     *
     * @param error - Used to fetch and handle the error
     */
    private void responseError(Throwable error) {
        mTrailersNetworkText.setText(error.getLocalizedMessage());
        mReviewsNetworkText.setText(error.getLocalizedMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    /**
     * Method to fetch the position of the trailer item
     * Using which the trailer information is extracted
     * and used for calling the intent for loading the video
     *
     * @param position - integer to get position of Trailers list
     */
    @Override
    public void onTrailerClick(int position) {
        Trailer trailer = mTrailers.get(position);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(NetworkUtils.YOUTUBE_BASE_URL + trailer.getKey()));
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("test", "test");

        ArrayList<Trailer> trailersList = new ArrayList<>();
        trailersList.addAll(mTrailers);

        ArrayList<Review> reviewsList = new ArrayList<>();
        reviewsList.addAll(mReviews);

        outState.putParcelable(PARAMS_MOVIE, movie);
        outState.putParcelableArrayList(STATE_TRAILERS, trailersList);
        outState.putParcelableArrayList(STATE_REVIEWS, reviewsList);
        outState.putBoolean(STATE_TRAILERS_LOADED, mIsLoadedTrailers);
        outState.putBoolean(STATE_REVIEWS_LOADED, mIsLoadedReviews);
        outState.putIntArray(STATE_SCROLL, new int[]{mMainScroll.getScrollX(), mMainScroll.getScrollY()});

        super.onSaveInstanceState(outState);
    }
}
