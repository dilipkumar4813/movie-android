package com.iamdilipkumar.movies.movies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iamdilipkumar.movies.movies.R;
import com.iamdilipkumar.movies.movies.models.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class to create an adapter for trailers
 * <p>
 * Created on 16/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private List<Trailer> mTrailers;
    private TrailerItemClickListener mTrailerItemClickListener;

    public TrailersAdapter(List<Trailer> trailers, TrailerItemClickListener trailerItemClickListener) {
        this.mTrailers = trailers;
        this.mTrailerItemClickListener = trailerItemClickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        holder.onBind(trailer.getName(), trailer.getType());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public interface TrailerItemClickListener {
        void onTrailerClick(int position);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.tv_trailer_name)
        TextView mTrailerName;

        @Nullable
        @BindView(R.id.tv_trailer_type)
        TextView mTrailerType;

        private TrailerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void onBind(String name, String type) {
            Log.d("test", name + " " + type);

            assert mTrailerName != null;
            mTrailerName.setText(name);

            assert mTrailerType != null;
            mTrailerType.setText(type);
        }

        @Override
        public void onClick(View v) {
            int trailerPosition = getAdapterPosition();
            mTrailerItemClickListener.onTrailerClick(trailerPosition);
        }
    }
}
