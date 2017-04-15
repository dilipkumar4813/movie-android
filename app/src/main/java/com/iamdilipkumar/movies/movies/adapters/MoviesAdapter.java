package com.iamdilipkumar.movies.movies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context context;

    private ArrayList<Movie> movies;
    private MovieItemClickListener mOnClickListener;

    public MoviesAdapter(ArrayList<Movie> moviesResult, MovieItemClickListener onClickListener) {
        this.movies = moviesResult;
        this.mOnClickListener = onClickListener;
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(int position);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Imageview to display the poster
        @Nullable
        @BindView(R.id.iv_movie_poster)
        ImageView mPoster;

        MovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        /**
         * Method to load the poster using picasso
         *
         * @param url        the poster that has to be loaded
         * @param movieTitle setting the title as content description
         */
        void bind(String url, String movieTitle) {
            assert mPoster != null;
            mPoster.setContentDescription(movieTitle);

            Picasso.with(context).load(url).into(mPoster);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieItemClick(clickedPosition);
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
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);

        return new MovieViewHolder(view);
    }

    /**
     * Method to get reference to the list and load the same
     *
     * @param holder   the MovieViewHolder that has to be updated
     * @param position the position of the item
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie.getPosterPath(), movie.getTitle());
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
}
