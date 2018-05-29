package com.example.a0603614.popularmovies.utilities;


import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.a0603614.popularmovies.R;

// Static api class for getting the correct end points for networking calls
public class Api {
    private static Uri mPopularUri;
    private static Uri mTopRatedUri;
    private static Uri mVideosUri;
    private static Uri mReviewsUri;
    private static Uri mYouTubeUri;


    public static Uri getPopularMoviesUri(Context context) {
        // If the popular api uri has already been set then return that
        if (mPopularUri != null) return mPopularUri;

        // Build the popular uri to continue using
        mPopularUri = new Uri.Builder()
                .scheme(context.getResources().getString(R.string.tmdb_popularity_sort_scheme))
                .authority(context.getResources().getString(R.string.tmdb_popularity_sort_host))
                .encodedPath(context.getResources().getString(R.string.tmdb_popularity_sort_path))
                .appendQueryParameter(
                        context.getResources().getString(R.string.tmdb_key_name),
                        context.getResources().getString(R.string.tmdb_key_value)
                ).build();

        return mPopularUri;
    }

    public static Uri getTopRatedMoviesUri(Context context) {
        // If the top rated api uri has already been set then return that
        if (mTopRatedUri != null) return mTopRatedUri;

        // Build the top rated uri to continue using
        mTopRatedUri = new Uri.Builder()
                .scheme(context.getResources().getString(R.string.tmdb_top_rated_sort_scheme))
                .authority(context.getResources().getString(R.string.tmdb_top_rated_sort_host))
                .encodedPath(context.getResources().getString(R.string.tmdb_top_rated_sort_path))
                .appendQueryParameter(
                        context.getResources().getString(R.string.tmdb_key_name),
                        context.getResources().getString(R.string.tmdb_key_value)
                ).build();

        return mTopRatedUri;
    }

    public static Uri getVideosUri(Context context, String movieId) {
        // Build the video uri property for continued use and return it
        Resources resources = context.getResources();
        mVideosUri = new Uri.Builder().scheme(resources.getString(R.string.tmdb_extras_scheme))
                .authority(resources.getString(R.string.tmdb_extras_host))
                .encodedPath(resources.getString(
                        R.string.tmdb_extras_path) + "/" + movieId + "/" + resources.getString(
                        R.string.tmdb_extras_videos_path))
                .build();

        return mVideosUri;
    }

    public static Uri getReviewsUri(Context context, String movieId) {
        // Build the reviews uri property for continued use and return it
        Resources resources = context.getResources();
        mReviewsUri = new Uri.Builder().scheme(resources.getString(R.string.tmdb_extras_scheme))
                .authority(resources.getString(R.string.tmdb_extras_host))
                .encodedPath(resources.getString(
                        R.string.tmdb_extras_path) + "/" + movieId + "/" + resources.getString(
                        R.string.tmdb_extras_reviews_path))
                .build();

        return mReviewsUri;
    }

    public static Uri getYouTubeVideoUri(Context context, String key) {
        Resources resources = context.getResources();
        // Build out the YouTube base uri for continued use if necessary
        if (mYouTubeUri == null) {
            mYouTubeUri = new Uri.Builder().scheme(
                    resources.getString(R.string.tmdb_youtube_scheme))
                    .authority(resources.getString(R.string.tmdb_youtube_host))
                    .encodedPath(resources.getString(R.string.tmdb_youtube_path))
                    .build();
        }

        // Build the final uri with the parameter for the video
        Uri finalVideoUri = mYouTubeUri.buildUpon().appendQueryParameter(
                resources.getString(R.string.tmdb_youtube_view_param), key).build();

        return finalVideoUri;
    }
}
