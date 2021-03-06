package com.example.a0603614.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a0603614.popularmovies.R;
import com.example.a0603614.popularmovies.movieobjects.MovieItemData;
import com.squareup.picasso.Picasso;

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
        if (metrics.densityDpi <= DisplayMetrics.DENSITY_LOW) {
            mImageSize = "w92";
        } else if (metrics.densityDpi > DisplayMetrics.DENSITY_LOW &&
                metrics.densityDpi < DisplayMetrics.DENSITY_TV) {
            mImageSize = "w154";
        } else if (metrics.densityDpi >= DisplayMetrics.DENSITY_TV &&
                metrics.densityDpi < DisplayMetrics.DENSITY_XHIGH) {
            mImageSize = "w342";
        } else {
            mImageSize = "w500";
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
            String posterPath = mMoviesData[position].posterPath;
            // Build the URL string from the information
            String posterUrl = mHolderContext.getResources().getString(R.string.tmdb_poster_base_url) +
                    mImageSize + posterPath;

            // Use Picasso to set the image into the image view on this MovieViewHolder
            Picasso.with(mHolderContext).load(posterUrl).into(mImagePoster);
        }
    }

    public void setMovieData(MovieItemData[] movies) {
        mMoviesData = movies;
        mListItemCount = mMoviesData.length;
        notifyDataSetChanged();
    }
}
