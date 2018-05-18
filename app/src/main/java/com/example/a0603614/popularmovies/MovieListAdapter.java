package com.example.a0603614.popularmovies;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.Inflater;

/**
 * Created by A0603614 on 5/17/18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final ListItemClickListener mOnClickListener;
    private Context mContext;
    private int mListItemCount = 0;
    private String mImageSize;

    public MovieListAdapter(Context current, ListItemClickListener listener) {
        // Set the mouse click listener
        mOnClickListener = listener;

        // Determine the image size parameter to use
        mContext = current;
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW: {
                mImageSize = "w92";
                break;
            }
            case DisplayMetrics.DENSITY_MEDIUM: {
                mImageSize = "w154";
                break;
            }
            case DisplayMetrics.DENSITY_TV: {
                mImageSize = "w185";
                break;
            }
            case DisplayMetrics.DENSITY_HIGH: {
                mImageSize = "w342";
                break;
            }
            case DisplayMetrics.DENSITY_XHIGH: {
                mImageSize = "w500";
                break;
            }
            case DisplayMetrics.DENSITY_XXHIGH: {
                mImageSize = "w780";
                break;
            }
            case DisplayMetrics.DENSITY_XXXHIGH: {
                mImageSize = "original";
                break;
            }
        }
    }

    public void setMovieData() {

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the view
        Context context = parent.getContext();
        int listItemID = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(listItemID, parent, false);

        // Create the MovieViewHolder with the inflated view
        MovieViewHolder movieView = new MovieViewHolder(view);

        // Return the MovieViewHolder
        return movieView;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListItemCount;
    }

    public interface ListItemClickListener {
        void onListItemClick(int itemIndex);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements RecyclerView.OnClickListener {

        private ImageView imagePoster;
        private Context mHolderContext;

        public MovieViewHolder(View view) {
            super(view);
            mHolderContext = view.getContext();

            // Cache the reference to the movie poster image display
            imagePoster = view.findViewById(R.id.iv_movie_poster);
            // Set the view to listen for clicks
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemIndex = getAdapterPosition();
            mOnClickListener.onListItemClick(itemIndex);
        }

        public void bind(int position) {
            // TODO: Get the poster file name from the data

            // Temporary image file name for the moment
            String imgFile = "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg";
            // Build the URL string from the information
            String posterUrl = R.string.tmdb_image_base_url + mImageSize + imgFile;

            // Use Picasso to set the image into the image view on this MovieViewHolder
            Picasso.with(mHolderContext).load(posterUrl).into(imagePoster);
        }
    }
}
