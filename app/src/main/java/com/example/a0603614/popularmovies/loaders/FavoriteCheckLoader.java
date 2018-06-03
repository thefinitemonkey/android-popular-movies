package com.example.a0603614.popularmovies.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.a0603614.popularmovies.database.TMDBMoviesContract;

public class FavoriteCheckLoader extends AsyncTaskLoader<Boolean> {
    private static int mID;


    public FavoriteCheckLoader(Context context, int id) {
        super(context);
        mID = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Boolean loadInBackground() {
        Cursor cursor;
        try {
            cursor = getContext().getContentResolver().query(
                    TMDBMoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendEncodedPath(
                            "/" + mID).build(),
                    new String[]{TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_ID}, null, null,
                    null
            );
            if (cursor != null && cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
