package com.iamdilipkumar.movies.movies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iamdilipkumar.movies.movies.R;
import com.iamdilipkumar.movies.movies.models.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private List<Review> mReviews;

    public ReviewsAdapter(List<Review> reviews) {
        this.mReviews = reviews;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.onBind(review.getAuthor(), review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_reviewer)
        TextView mAuthor;

        @Nullable
        @BindView(R.id.tv_review_content)
        TextView mReviewContent;

        private ReviewsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        private void onBind(String author, String content) {
            assert mAuthor != null;
            mAuthor.setText(author);

            assert mReviewContent != null;
            mReviewContent.setText(content);
        }
    }
}
