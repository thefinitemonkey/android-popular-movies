package com.example.a0603614.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesContentProvider extends ContentProvider {
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    public static final int VIDEOS = 200;
    public static final int VIDEOS_WITH_ID = 201;
    public static final int REVIEWS = 300;
    public static final int REVIEWS_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TMDBMoviesDbHelper mMoviesDbHelper;

    public static UriMatcher buildUriMatcher() {
        // Initialize empty matcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TMDBMoviesContract.AUTHORITY, TMDBMoviesContract.PATH_FAVORITES, MOVIES);
        uriMatcher.addURI(
                TMDBMoviesContract.AUTHORITY, TMDBMoviesContract.PATH_FAVORITES + "/#",
                MOVIES_WITH_ID
        );
        uriMatcher.addURI(TMDBMoviesContract.AUTHORITY, TMDBMoviesContract.PATH_VIDEOS, VIDEOS);
        uriMatcher.addURI(
                TMDBMoviesContract.AUTHORITY, TMDBMoviesContract.PATH_VIDEOS + "/#",
                VIDEOS_WITH_ID
        );
        uriMatcher.addURI(TMDBMoviesContract.AUTHORITY, TMDBMoviesContract.PATH_REVIEWS, REVIEWS);
        uriMatcher.addURI(
                TMDBMoviesContract.AUTHORITY, TMDBMoviesContract.PATH_REVIEWS + "/#",
                REVIEWS_WITH_ID
        );

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new TMDBMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get a readable database to retrieve records
        SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        // Determine the match pattern
        int match = sUriMatcher.match(uri);
        Cursor records;

        switch (match) {
            case MOVIES: {
                records = db.query(
                        TMDBMoviesContract.MoviesEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder
                );
                break;
            }
            case MOVIES_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                records = db.query(
                        TMDBMoviesContract.MoviesEntry.TABLE_NAME, projection,
                        TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{id},
                        null, null, sortOrder
                );
                break;
            }
            case VIDEOS: {
                records = db.query(
                        TMDBMoviesContract.VideosEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder
                );
                break;
            }
            case VIDEOS_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                records = db.query(
                        TMDBMoviesContract.VideosEntry.TABLE_NAME, projection,
                        TMDBMoviesContract.VideosEntry.COLUMN_MOVIE_ID + "=?", new String[]{id},
                        null, null, sortOrder
                );
                break;
            }
            case REVIEWS: {
                records = db.query(
                        TMDBMoviesContract.ReviewsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder
                );
                break;
            }
            case REVIEWS_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                records = db.query(
                        TMDBMoviesContract.ReviewsEntry.TABLE_NAME, projection,
                        TMDBMoviesContract.ReviewsEntry.COLUMN_MOVIE_ID + "=?", new String[]{id},
                        null, null, sortOrder
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unrecognized URI for record retrieval: " + uri);
            }
        }

        // Set the context update notification on the cursor and return it
        records.setNotificationUri(getContext().getContentResolver(), uri);
        return records;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // Get a writable db instance for the insert
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        // Determine the match pattern
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case MOVIES: {
                // Insert values into the table of favorites
                long id = db.insert(TMDBMoviesContract.MoviesEntry.TABLE_NAME, null, contentValues);
                if (id > 0) returnUri = ContentUris.withAppendedId(
                        TMDBMoviesContract.MoviesEntry.CONTENT_URI, id);
                break;
            }
            case VIDEOS: {
                // Insert values into the videos table
                long id = db.insert(TMDBMoviesContract.VideosEntry.TABLE_NAME, null, contentValues);
                if (id > 0) returnUri = ContentUris.withAppendedId(
                        TMDBMoviesContract.VideosEntry.CONTENT_URI, id);
                break;
            }
            case REVIEWS: {
                // Insert values into the reviews table
                long id = db.insert(
                        TMDBMoviesContract.ReviewsEntry.TABLE_NAME, null, contentValues);
                if (id > 0) returnUri = ContentUris.withAppendedId(
                        TMDBMoviesContract.ReviewsEntry.CONTENT_URI, id);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown URI for insert: " + uri);
            }
        }

        if (returnUri == null) throw new SQLException("Failed to insert row into " + uri);

        // Notify that content has changed
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Get a writable db instance for the delete
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        // Determine the match pattern
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        int recordsDeleted = 0;

        switch (match) {
            case MOVIES_WITH_ID: {
                // Get the id from the URI path
                String id = uri.getPathSegments().get(1);
                // Delete the selected movie from the favorites
                recordsDeleted = db.delete(
                        TMDBMoviesContract.MoviesEntry.TABLE_NAME,
                        TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{id}
                );
                break;
            }
            case VIDEOS_WITH_ID: {
                // Get the id from the URI path
                String id = uri.getPathSegments().get(1);
                // Delete the videos for the selected movie
                recordsDeleted = db.delete(
                        TMDBMoviesContract.VideosEntry.TABLE_NAME,
                        TMDBMoviesContract.VideosEntry.COLUMN_MOVIE_ID + "=?", new String[]{id}
                );
                break;
            }
            case REVIEWS_WITH_ID: {
                // Get the id from the URI path
                String id = uri.getPathSegments().get(1);
                // Delete the reviews for the selected movie
                recordsDeleted = db.delete(
                        TMDBMoviesContract.ReviewsEntry.TABLE_NAME,
                        TMDBMoviesContract.ReviewsEntry.COLUMN_MOVIE_ID + "=?", new String[]{id}
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown URI for delete: " + uri);
            }
        }

        if (recordsDeleted > 0) getContext().getContentResolver().notifyChange(uri, null);

        return recordsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
