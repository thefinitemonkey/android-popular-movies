package com.example.a0603614.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TMDBMoviesDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "TMDBMovies.db";
    private static final int DATABASE_VERSION = 1;

    public TMDBMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIES_TABLE =
                "CREATE TABLE " + TMDBMoviesContract.MoviesEntry.TABLE_NAME + " ( " +
                        TMDBMoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " FLOAT NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_POPULARITY_SCORE + " FLOAT NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_ADULT_FILM + " BOOLEAN NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_GENRE_IDS + " TEXT NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_VIDEO + " BOOLEAN NOT NULL, " +
                        TMDBMoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL);";

        final String CREATE_VIDEOS_TABLE =
                "CREATE TABLE " + TMDBMoviesContract.VideosEntry.TABLE_NAME + " (" +
                        TMDBMoviesContract.VideosEntry._ID + " INTEGER PRIMARY KEY, " +
                        TMDBMoviesContract.VideosEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        TMDBMoviesContract.VideosEntry.COLUMN_VIDEO_ID + " TEXT NOT NULL, " +
                        TMDBMoviesContract.VideosEntry.COLUMN_VIDEO_NAME + " TEXT NOT NULL, " +
                        TMDBMoviesContract.VideosEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                        TMDBMoviesContract.VideosEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                        TMDBMoviesContract.VideosEntry.COLUMN_VIDEO_TYPE + " TEXT NOT NULL);";

        final String CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + TMDBMoviesContract.ReviewsEntry.TABLE_NAME + " (" +
                        TMDBMoviesContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY, " +
                        TMDBMoviesContract.ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        TMDBMoviesContract.ReviewsEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                        TMDBMoviesContract.ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                        TMDBMoviesContract.ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                        TMDBMoviesContract.ReviewsEntry.COLUMN_URL + " TEXT NOT NULL);";

        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_VIDEOS_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TMDBMoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TMDBMoviesContract.VideosEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TMDBMoviesContract.ReviewsEntry.TABLE_NAME);

        onCreate(db);
    }
}
