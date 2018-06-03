package com.example.a0603614.popularmovies.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.a0603614.popularmovies.database.TMDBMoviesContract;
import com.example.a0603614.popularmovies.movieobjects.MovieItemData;
import com.example.a0603614.popularmovies.utilities.TMDBDataTransform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieListCursorLoader extends AsyncTaskLoader<MovieItemData[]> {
    private static final String TAG = MovieListCursorLoader.class.getSimpleName();
    private static final String[] mMoviesProjection = new String[]{
            TMDBMoviesContract.MoviesEntry._ID,
            TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE,
            TMDBMoviesContract.MoviesEntry.COLUMN_VOTE_COUNT,
            TMDBMoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            TMDBMoviesContract.MoviesEntry.COLUMN_POPULARITY_SCORE,
            TMDBMoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
            TMDBMoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE,
            TMDBMoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
            TMDBMoviesContract.MoviesEntry.COLUMN_GENRE_IDS,
            TMDBMoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,
            TMDBMoviesContract.MoviesEntry.COLUMN_ADULT_FILM,
            TMDBMoviesContract.MoviesEntry.COLUMN_OVERVIEW,
            TMDBMoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
            TMDBMoviesContract.MoviesEntry.COLUMN_VIDEO
    };
    private static final int COLUMN_MOVIE_ID = 1;
    private static final int COLUMN_MOVIE_TITLE = 2;
    private static final int COLUMN_VOTE_COUNT = 3;
    private static final int COLUMN_VOTE_AVERAGE = 4;
    private static final int COLUMN_POPULARITY_SCORE = 5;
    private static final int COLUMN_POSTER_PATH = 6;
    private static final int COLUMN_ORIGINAL_LANGUAGE = 7;
    private static final int COLUMN_ORIGINAL_TITLE = 8;
    private static final int COLUMN_GENRE_IDS = 9;
    private static final int COLUMN_BACKDROP_PATH = 10;
    private static final int COLUMN_ADULT_FILM = 11;
    private static final int COLUMN_OVERVIEW = 12;
    private static final int COLUMN_RELEASE_DATE = 13;
    private static final int COLUMN_VIDEO = 14;
    Cursor mMovieCursor = null;
    MovieItemData[] mMovieData = null;


    public MovieListCursorLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public MovieItemData[] loadInBackground() {
        Cursor cursor;
        try {
            cursor = getContext().getContentResolver().query(
                    TMDBMoviesContract.MoviesEntry.CONTENT_URI, mMoviesProjection, null, null,
                    null
            );
            if (cursor != null) {
                return moviesCursorToArray(cursor);
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "loadInBackground: Failed to load movies list -- " + e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(MovieItemData[] data) {
        mMovieData = data;
        super.deliverResult(data);
    }

    private MovieItemData[] moviesCursorToArray(Cursor cursor) {
        // Get the number of records in the cursor and construct the array
        int numRecords = cursor.getCount();
        // Validate we have good records
        if (numRecords < 1 || !cursor.moveToFirst()) return new MovieItemData[0];

        // Iterate over all the records in the cursor to create an array of
        // MovieItemData objects to drive the display
        MovieItemData[] movies = new MovieItemData[numRecords];
        for (int i = 0; i < movies.length; i++) {
            // Move the cursor to the appropriate record
            cursor.moveToPosition(i);

            // Gather all the data
            int movieID = cursor.getInt(COLUMN_MOVIE_ID);
            String movieTitle = cursor.getString(COLUMN_MOVIE_TITLE);
            int votes = cursor.getInt(COLUMN_VOTE_COUNT);
            float average = cursor.getLong(COLUMN_VOTE_AVERAGE);
            float popularityScore = cursor.getLong(COLUMN_POPULARITY_SCORE);
            String poster = cursor.getString(COLUMN_POSTER_PATH);
            String origLanguage = cursor.getString(COLUMN_ORIGINAL_LANGUAGE);
            String origTitle = cursor.getString(COLUMN_ORIGINAL_TITLE);
            String backdrop = cursor.getString(COLUMN_BACKDROP_PATH);
            String overviewText = cursor.getString(COLUMN_OVERVIEW);

            // Genres needs to be converted from a String to an int[]
            String genresString = cursor.getString(COLUMN_GENRE_IDS);
            int[] genres = TMDBDataTransform.convertStringToIntArray(genresString);

            // adultFilm needs to be converted from an int to a boolean
            int adult = cursor.getInt(COLUMN_ADULT_FILM);
            Boolean adultFilm = false;
            if (adult != 0) adultFilm = true;

            // hasVideo needs to be converted from an int to a boolean
            int video = cursor.getInt(COLUMN_VIDEO);
            Boolean hasVideo = false;
            if (video != 0) hasVideo = true;

            // release needs to be converted from an int to a Date
            String releaseDate = cursor.getString(COLUMN_RELEASE_DATE);
            Date release;
            try {
                release = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
            } catch (ParseException e) {
                Log.e(TAG, "moviesCursorToArray: Could not parse release date " + e);
                e.printStackTrace();
                release = new Date();
            }

            // Create the object
            MovieItemData movie = new MovieItemData(movieID, movieTitle, votes, average,
                                                    popularityScore, poster, origLanguage,
                                                    origTitle, genres, backdrop, adultFilm,
                                                    overviewText, release, hasVideo, true
            );

            // Add the movie to the list
            movies[i] = movie;
        }

        return movies;
    }
}
