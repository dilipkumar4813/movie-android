package com.iamdilipkumar.movies.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView mPosterImage, mBannerImage;
    TextView mTitleText, mPlotText, mReleaseText, mVoteAverageText,mMovieLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mBannerImage = (ImageView) findViewById(R.id.iv_movie_banner);
        mPosterImage = (ImageView) findViewById(R.id.iv_movie_poster);

        mTitleText = (TextView) findViewById(R.id.tv_movie_title);
        mPlotText = (TextView) findViewById(R.id.tv_movie_plot);
        mReleaseText = (TextView) findViewById(R.id.tv_movie_release_date);
        mVoteAverageText = (TextView) findViewById(R.id.tv_movie_average_vote);
        mMovieLanguage = (TextView) findViewById(R.id.tv_movie_language);

        Intent savedIntent = getIntent();

        Picasso.with(this).load(savedIntent.getStringExtra("banner")).into(mBannerImage);
        Picasso.with(this).load(savedIntent.getStringExtra("poster")).into(mPosterImage);

        mTitleText.setText(savedIntent.getStringExtra("title"));
        mPlotText.setText(savedIntent.getStringExtra("plot"));
        mReleaseText.setText(savedIntent.getStringExtra("release"));
        mVoteAverageText.setText(getString(R.string.movie_average_vote) + savedIntent.getStringExtra("average"));
        mMovieLanguage.setText("Language: "+savedIntent.getStringExtra("language"));
    }
}
