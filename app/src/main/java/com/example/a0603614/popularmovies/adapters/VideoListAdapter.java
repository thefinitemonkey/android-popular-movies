package com.example.a0603614.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a0603614.popularmovies.R;
import com.example.a0603614.popularmovies.movieobjects.VideoItemData;
import com.squareup.picasso.Picasso;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private Context mContext;
    private int mListItemCount = 0;
    private ListItemClickListener mOnClickListener;
    private VideoItemData[] mVideoList;

    public VideoListAdapter(@NonNull Context current, ListItemClickListener listener) {
        mContext = current;
        mOnClickListener = listener;
    }

    public VideoItemData getVideoData(int index) {
        if (index > 0 && mVideoList.length >= 0) {
            return mVideoList[index];
        }
        return null;
    }

    public void swapVideoList(VideoItemData[] videos) {
        mVideoList = videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.video_list_item, parent, false);
        view.setFocusable(true);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.VideoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListItemCount;
    }

    public void setVideoData(VideoItemData[] videos) {
        mVideoList = videos;
        mListItemCount = videos.length;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int id);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder
            implements RecyclerView.OnClickListener {
        final ImageView mVideoThumb;
        final TextView mVideoTitle;
        final TextView mVideoType;
        final Context mHolderContext;

        public VideoViewHolder(View view) {
            super(view);

            mHolderContext = view.getContext();
            mVideoThumb = view.findViewById(R.id.iv_video_thumbnail);
            mVideoTitle = view.findViewById(R.id.tv_thumb_name);
            mVideoType = view.findViewById(R.id.tv_thumb_type);

            view.setOnClickListener(this);
        }

        public void bind(int position) {
            VideoItemData videoItem = mVideoList[position];

            // Use Picasso to set the thumbnail image into the image view
            Picasso.with(mHolderContext).load(videoItem.thumbUrl).into(mVideoThumb);
            // Load the name and type into the text fields
            mVideoTitle.setText(videoItem.name);
            mVideoType.setText(videoItem.type);
        }

        @Override
        public void onClick(View view) {
            // Get the id for the clicked movie item and use that in firing the click event
            int adapterPosition = getAdapterPosition();
            //VideoItemData videoItem = mVideoList[adapterPosition];
            mOnClickListener.onListItemClick(adapterPosition);
        }
    }
}
