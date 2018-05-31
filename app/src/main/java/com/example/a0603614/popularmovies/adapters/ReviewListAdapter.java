package com.example.a0603614.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a0603614.popularmovies.R;
import com.example.a0603614.popularmovies.movieobjects.ReviewItemData;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private Context mContext;
    private int mListItemCount = 0;
    private ListItemClickListener mOnClickListener;
    private ReviewItemData[] mReviewList;

    public ReviewListAdapter(@NonNull Context current, ListItemClickListener listener) {
        mContext = current;
        mOnClickListener = listener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);
        view.setFocusable(true);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListItemCount;
    }

    public void setReviewData(ReviewItemData[] reviews) {
        mReviewList = reviews;
        mListItemCount = mReviewList.length;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int id);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        final TextView mReviewAuthor;
        final TextView mReviewContent;
        final Context mHolderContext;

        public ReviewViewHolder(View view) {
            super(view);

            mHolderContext = view.getContext();
            mReviewAuthor = view.findViewById(R.id.tv_review_author);
            mReviewContent = view.findViewById(R.id.tv_review_content);

            view.setOnClickListener(this);
        }

        public void bind(int position) {
            // Get the information for the specified review item and display it
            ReviewItemData review = mReviewList[position];

            if (review != null) {
                mReviewAuthor.setText(review.author);
                mReviewContent.setText(review.content);
            }
        }

        @Override
        public void onClick(View view) {
            // Emit the adapter item that was clicked
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(adapterPosition);
        }
    }
}
