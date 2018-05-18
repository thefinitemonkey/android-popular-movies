package com.example.a0603614.popularmovies.utilities;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.example.a0603614.popularmovies.MovieItemData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by A0603614 on 5/18/18.
 */

public final class TMDBDataTransform {
    private static final String TAG = TMDBDataTransform.class.getSimpleName();

    private static final String JSON_MESSAGE_CODE = "cod";
    private static final String JSON_RESULTS_ARRAY = "results";
    private static final String JSON_VOTE_COUNT = "vote_count";
    private static final String JSON_ID = "id";
    private static final String JSON_VIDEO = "video";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_TITLE = "title";
    private static final String JSON_POPULARITY = "popularity";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_ORIGINAL_LANGUAGE = "original_language";
    private static final String JSON_ORIGINAL_TITLE = "original_title";
    private static final String JSON_GENRE_IDS = "genre_ids";
    private static final String JSON_BACKDROP_PATH = "backdrop_path";
    private static final String JSON_ADULT = "adult";
    private static final String JSON_OVERVIEW = "overview";
    private static final String JSON_RELEASE_DATE = "release_date";

    private static final MovieItemData[] EMPTY_MOVIE_LIST = new MovieItemData[0];



    public static MovieItemData[] getMoviesFromJSON(Context context, String jsonData) {
        // Initialize values for use in transforming the JSON data
        JSONObject movieJSON;
        try {
            movieJSON = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
            return EMPTY_MOVIE_LIST;
        }

        // Check that there isn't an unexpected error code
        if (movieJSON.has(JSON_MESSAGE_CODE)) {
            try {
                int errorCode = movieJSON.getInt(JSON_MESSAGE_CODE);
                if (errorCode != HttpURLConnection.HTTP_OK) return EMPTY_MOVIE_LIST;
            } catch (Exception e) {
                e.printStackTrace();
                return EMPTY_MOVIE_LIST;
            }
        }

        // Get the JSON array of movies from the JSON object
        JSONArray movieArray;
        try {
            movieArray = movieJSON.getJSONArray(JSON_RESULTS_ARRAY);
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY_MOVIE_LIST;
        }

        // Create a MovieItemData array of the size of our JSON array
        MovieItemData[] movies = new MovieItemData[movieArray.length()];

        // Iterate over the JSON array to create the movie objects
        int arrayLength = movieArray.length();
        for (int i = 0; i < arrayLength; i++) {
            JSONObject movieJSONObj = null;
            try {
                movieJSONObj = movieArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create a MovieItemData object and set its information from the JSON
            try {
                // ID and Title are required to create object
                int id = movieJSONObj.getInt(JSON_ID);
                String title = movieJSONObj.getString(JSON_TITLE);
                MovieItemData movieItem = new MovieItemData(id, title);

                // Put the simple properties
                movieItem.setAdult(movieJSONObj.getBoolean(JSON_ADULT));
                movieItem.setBackdropPath(movieJSONObj.getString(JSON_BACKDROP_PATH));
                movieItem.setOriginalLanguage(movieJSONObj.getString(JSON_ORIGINAL_LANGUAGE));
                movieItem.setOriginalTitle(movieJSONObj.getString(JSON_ORIGINAL_TITLE));
                movieItem.setOverview(movieJSONObj.getString(JSON_OVERVIEW));
                movieItem.setPopularity(movieJSONObj.getLong(JSON_POPULARITY));
                movieItem.setPosterPath(movieJSONObj.getString(JSON_POSTER_PATH));
                movieItem.setVideoAvailable(movieJSONObj.getBoolean(JSON_VIDEO));
                movieItem.setVoteAverage(movieJSONObj.getLong(JSON_VOTE_AVERAGE));
                movieItem.setVoteCount(movieJSONObj.getInt(JSON_VOTE_COUNT));

                // Date is returned as a string in JSON and will be stored as a Date
                String jsonDate = movieJSONObj.getString(JSON_RELEASE_DATE);
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonDate);
                movieItem.setReleaseDate(releaseDate);

                // Genres are an array and need to be resolved from the JSON
                JSONArray jsonIDs = movieJSONObj.getJSONArray(JSON_GENRE_IDS);
                int[] intIDs = new int[jsonIDs.length()];
                for (int pos = 0; pos < jsonIDs.length(); pos++) {
                    intIDs[pos] = jsonIDs.getInt(pos);
                }
                movieItem.setGenreIDs(intIDs);

                // Store the movie object in the array
                movies[i] = movieItem;
            } catch (Exception e) {
                Log.e(TAG, "getMoviesFromJSON: Error creating MovieItemData" + e.getMessage(), null);
            }
        }


        return movies;

    }
}
