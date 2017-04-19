package com.iamdilipkumar.movies.movies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.iamdilipkumar.movies.movies.R;
import com.iamdilipkumar.movies.movies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 21/03/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private ArrayList<Movie> movies;
    private MovieItemClickListener mOnClickListener;
    private LikeItemClickListener mLikeItemClickListener;

    private final static int ITEM_TYPE_MOVIE = 1;
    public final static int ITEM_TYPE_LOADING = 0;

    private int lastPosition = 0;

    public MoviesAdapter(ArrayList<Movie> moviesResult, MovieItemClickListener onClickListener, LikeItemClickListener likeItemClickListener) {
        this.movies = moviesResult;
        this.mOnClickListener = onClickListener;
        this.mLikeItemClickListener = likeItemClickListener;
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(int position);
    }

    public interface LikeItemClickListener {
        void onLikeItemClick(int position);
    }

    /**
     * View holder class for movies type
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Imageview to display the poster
        @Nullable
        @BindView(R.id.iv_movie_poster)
        ImageView mPoster;

        @Nullable
        @BindView(R.id.iv_like)
        ImageView mLike;

        MovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mPoster.setOnClickListener(this);
            mLike.setOnClickListener(this);

        }

        /**
         * Method to load the poster using picasso
         *
         * @param url        the poster that has to be loaded
         * @param movieTitle setting the title as content description
         */
        void onBind(String url, String movieTitle) {
            assert mPoster != null;
            mPoster.setContentDescription(movieTitle);

            Picasso.with(context).load(url).into(mPoster);
        }

        public void onLike() {
            mLike.setImageResource(R.drawable.ic_heart);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieItemClick(clickedPosition);
            mLikeItemClickListener.onLikeItemClick(clickedPosition);
        }
    }

    /**
     * Loading view holder class
     */
    class LoadingViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.pb_loading_movies)
        ProgressBar mProgressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
        }

        void onBind() {
            //assert mProgressBar != null;
            //mProgressBar.setIndeterminate(true);
        }
    }

    /**
     * Method to inflate the layout for the item
     * View holders are created as per the number of the count of items
     *
     * @param parent   ViewHolders are contained within this ViewGroup.
     * @param viewType if recyclerview has different types
     * @return the new view holder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        switch (viewType) {
            case ITEM_TYPE_MOVIE:
                View movieView = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
                return new MovieViewHolder(movieView);
            case ITEM_TYPE_LOADING:
                View loadingView = LayoutInflater.from(context).inflate(R.layout.load_more_list_item, parent, false);
                return new LoadingViewHolder(loadingView);
        }

        return null;
    }

    /**
     * Method to get reference to the list and load the same
     *
     * @param holder   the MovieViewHolder that has to be updated
     * @param position the position of the item
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MovieViewHolder) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Movie movie = movies.get(position);
            movieViewHolder.onBind(movie.getPosterPath(), movie.getTitle());


            if ((position == movies.size() - 1) || (position == movies.size() - 2)) {
                Animation animation;
                if (position % 2 == 0) {
                    animation = AnimationUtils.loadAnimation(context,
                            R.anim.recycler_load_from_below_even);
                } else {
                    animation = AnimationUtils.loadAnimation(context,
                            R.anim.recycler_load_from_below_odd);
                }

                movieViewHolder.itemView.startAnimation(animation);
                Log.d("test", "" + lastPosition);
            }
            lastPosition++;
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.onBind();
        }

    }

    /**
     * This method simply returns the number of items to be display.
     *
     * @return number of movie items
     */
    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position) == null ? ITEM_TYPE_LOADING : ITEM_TYPE_MOVIE;
    }
}
