package com.example.a0603614.popularmovies.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.example.a0603614.popularmovies.movieobjects.MovieItemData;
import com.example.a0603614.popularmovies.utilities.NetworkUtility;
import com.example.a0603614.popularmovies.utilities.TMDBDataTransform;

public class MovieListLoader extends AsyncTaskLoader<MovieItemData[]>{

    private Uri mSortUri;
    private Context mContext;

    public MovieListLoader(Context context, String url) {
        super(context);
        mSortUri = Uri.parse(url);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public MovieItemData[] loadInBackground() {
        // Check that there is a parameter work with
        if (mSortUri == null) return null;

        try {
            // Get the JSON data
            String jsonMoviesResponse = NetworkUtility.getResponseFromHTTPUrl(mSortUri);

            return TMDBDataTransform.getMoviesFromJSON(
                    mContext, jsonMoviesResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
