package com.example.a0603614.popularmovies.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.example.a0603614.popularmovies.movieobjects.VideoItemData;
import com.example.a0603614.popularmovies.utilities.NetworkUtility;
import com.example.a0603614.popularmovies.utilities.TMDBDataTransform;

public class VideoListLoader extends AsyncTaskLoader<VideoItemData[]> {

    private Uri mVideoUri;
    private Context mContext;

    public VideoListLoader(Context context, String url) {
        super(context);

        mContext = context;
        mVideoUri = Uri.parse(url);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public VideoItemData[] loadInBackground() {
        // Check that there is a uri to work with
        if (mVideoUri == null) return null;

        try {
            // Get the videos array from  the JSON return
            String jsonVideoResponse = NetworkUtility.getResponseFromHTTPUrl(mVideoUri);
            return TMDBDataTransform.getVideosFromJson(mContext, jsonVideoResponse);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
