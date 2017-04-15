package com.iamdilipkumar.movies.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class to show the details of the movie
 * By using the data that was passed from {@link MoviesListActivity#onMovieItemClick(int)}
 * <p>
 * Created on 21/03/2017
 *
 * @author dilipkumar4813
 * @version 1.0
 */
public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_poster)
    ImageView mPosterImage;
    @BindView(R.id.iv_movie_banner)
    ImageView mBannerImage;

    @BindView(R.id.tv_movie_title)
    TextView mTitleText;
    @BindView(R.id.tv_movie_plot)
    TextView mPlotText;
    @BindView(R.id.tv_movie_release_date)
    TextView mReleaseText;
    @BindView(R.id.tv_movie_average_vote)
    TextView mVoteAverageText;
    @BindView(R.id.tv_movie_language)
    TextView mMovieLanguage;

    String movieVoteAverage, movieLanugage;

    public static final String MOVIE_POSTER = "poster";
    public static final String MOVIE_BANNER = "banner";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_PLOT = "plot";
    public static final String MOVIE_RELEASE = "release";
    public static final String MOVIE_LANGUAGE = "language";
    public static final String MOVIE_AVERAGE_RATING = "average";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        Intent savedIntent = getIntent();
        if (savedIntent != null && savedIntent.getExtras() != null) {

            if (savedIntent.hasExtra(MOVIE_BANNER)) {
                Picasso.with(this).load(savedIntent.getStringExtra(MOVIE_BANNER)).into(mBannerImage);
            }

            if (savedIntent.hasExtra(MOVIE_POSTER)) {
                Picasso.with(this).load(savedIntent.getStringExtra(MOVIE_POSTER)).into(mPosterImage);
            }

            if (savedIntent.hasExtra(MOVIE_TITLE)) {
                mTitleText.setText(savedIntent.getStringExtra(MOVIE_TITLE));
            }

            if (savedIntent.hasExtra(MOVIE_RELEASE)) {
                mReleaseText.setText(savedIntent.getStringExtra(MOVIE_RELEASE));
            }

            if (savedIntent.hasExtra(MOVIE_AVERAGE_RATING)) {
                movieVoteAverage = getString(R.string.movie_average_vote) + savedIntent.getDoubleExtra(MOVIE_AVERAGE_RATING, 0);
                mVoteAverageText.setText(movieVoteAverage);
            }

            if (savedIntent.hasExtra(MOVIE_LANGUAGE)) {
                movieLanugage = getString(R.string.movie_language) + savedIntent.getStringExtra(MOVIE_LANGUAGE);
                mMovieLanguage.setText(movieLanugage);
            }

            if (savedIntent.hasExtra(MOVIE_PLOT)) {
                mPlotText.setText(savedIntent.getStringExtra(MOVIE_PLOT));
            }
        }
    }
}
