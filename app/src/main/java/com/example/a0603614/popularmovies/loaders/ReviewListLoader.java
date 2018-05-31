package com.example.a0603614.popularmovies.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.example.a0603614.popularmovies.movieobjects.ReviewItemData;
import com.example.a0603614.popularmovies.utilities.NetworkUtility;
import com.example.a0603614.popularmovies.utilities.TMDBDataTransform;

public class ReviewListLoader extends AsyncTaskLoader<ReviewItemData[]> {

    private Uri mReviewUri;
    private Context mContext;


    public ReviewListLoader(Context context, String url) {
        super(context);

        mContext = context;
        mReviewUri = Uri.parse(url);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ReviewItemData[] loadInBackground() {
        // Check that there is a uri to work with
        if (mReviewUri == null) return null;

        try {
            // Get the videos array from  the JSON return
            String jsonReviewsResponse = NetworkUtility.getResponseFromHTTPUrl(mReviewUri);
            return TMDBDataTransform.getReviewsFromJson(mContext, jsonReviewsResponse);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
