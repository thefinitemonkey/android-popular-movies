package com.example.a0603614.popularmovies;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.res.Resources;
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
    static private final String TAG = MovieListAdapter.class.getSimpleName();

    private final ListItemClickListener mOnClickListener;
    private Context mContext;
    private int mListItemCount = 0;
    private String mImageSize;
    private MovieItemData[] mMoviesData;

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
            case DisplayMetrics.DENSITY_MEDIUM:
            case DisplayMetrics.DENSITY_TV: {
                mImageSize = "w154";
                break;
            }
            case DisplayMetrics.DENSITY_HIGH:
            case DisplayMetrics.DENSITY_XHIGH: {
                mImageSize = "w342";
                break;
            }
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH: {
                mImageSize = "w500";
                break;
            }
        }
    }

    public MovieItemData getMovieData(int index) {
        if (index >= 0 && mMoviesData.length > 0) {
            return mMoviesData[index];
        }
        return null;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the view
        Context context = parent.getContext();
        int listItemID = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(listItemID, parent, false);

        // Return the MovieViewHolder
        return new MovieViewHolder(view);
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

        private ImageView mImagePoster;
        private Context mHolderContext;

        public MovieViewHolder(View view) {
            super(view);
            mHolderContext = view.getContext();

            // Cache the reference to the movie poster image display
            mImagePoster = view.findViewById(R.id.iv_movie_poster);
            // Set the view to listen for clicks
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemIndex = getAdapterPosition();
            mOnClickListener.onListItemClick(itemIndex);
        }

        public void bind(int position) {
            // Get the poster path from the movie data
            String posterPath = mMoviesData[position].getPosterPath();
            // Build the URL string from the information
            String posterUrl = mHolderContext.getResources().getString(R.string.tmdb_poster_base_url) +
                    mImageSize + posterPath;

            // Use Picasso to set the image into the image view on this MovieViewHolder
            Picasso.with(mHolderContext).load(posterUrl).into(mImagePoster);
            // Get screen pixels
            int screenPixels = mHolderContext.getResources().getDisplayMetrics().widthPixels;
            mImagePoster.setMinimumWidth(screenPixels / 2);
        }
    }

    public void setMovieData(MovieItemData[] movies) {
        mMoviesData = movies;
        mListItemCount = mMoviesData.length;
        notifyDataSetChanged();
    }
}
