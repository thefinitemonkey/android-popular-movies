package com.example.a0603614.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.a0603614.popularmovies.R;
import com.example.a0603614.popularmovies.movieobjects.MovieItemData;
import com.example.a0603614.popularmovies.movieobjects.ReviewItemData;
import com.example.a0603614.popularmovies.movieobjects.VideoItemData;

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
    private static final VideoItemData[] EMPTY_VIDEO_LIST = new VideoItemData[0];
    private static final ReviewItemData[] EMPTY_REVIEW_LIST = new ReviewItemData[0];


    private static String strSeparator = "__,__";
    public static String convertIntArrayToString(int[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static int[] convertStringToIntArray(String str){
        String[] arr = str.split(strSeparator);
        int[] intArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            intArr[i] = Integer.parseInt(arr[i]);
        }
        return intArr;
    }

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

                // Put the simple properties
                Boolean adult = (movieJSONObj.getBoolean(JSON_ADULT));
                String backdropPath = (movieJSONObj.getString(JSON_BACKDROP_PATH));
                String originalLanguage = (movieJSONObj.getString(JSON_ORIGINAL_LANGUAGE));
                String originalTitle = (movieJSONObj.getString(JSON_ORIGINAL_TITLE));
                String overview = (movieJSONObj.getString(JSON_OVERVIEW));
                Long popularity = (movieJSONObj.getLong(JSON_POPULARITY));
                String posterPath = (movieJSONObj.getString(JSON_POSTER_PATH));
                Boolean video = (movieJSONObj.getBoolean(JSON_VIDEO));
                Long voteAverage = (movieJSONObj.getLong(JSON_VOTE_AVERAGE));
                int voteCount = (movieJSONObj.getInt(JSON_VOTE_COUNT));

                // Date is returned as a string in JSON and will be stored as a Date
                String jsonDate = movieJSONObj.getString(JSON_RELEASE_DATE);
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonDate);

                // Genres are an array and need to be resolved from the JSON
                JSONArray jsonIDs = movieJSONObj.getJSONArray(JSON_GENRE_IDS);
                int[] intIDs = new int[jsonIDs.length()];
                for (int pos = 0; pos < jsonIDs.length(); pos++) {
                    intIDs[pos] = jsonIDs.getInt(pos);
                }

                // Store the movie object in the array
                MovieItemData movieItem =
                        new MovieItemData(id, title, voteCount, voteAverage, popularity,
                                          posterPath, originalLanguage, originalTitle,
                                          intIDs, backdropPath, adult, overview, releaseDate,
                                          video);

                movies[i] = movieItem;
            } catch (Exception e) {
                Log.e(
                        TAG, "getMoviesFromJSON: Error creating MovieItemData" + e.getMessage(),
                        null
                );
            }
        }


        return movies;

    }

    public static VideoItemData[] getVideosFromJson(Context context, String jsonData) {
        JSONObject videosJson;
        try {
           videosJson = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
            return EMPTY_VIDEO_LIST;
        }

        // Check for unexpected return code
        if (videosJson.has(JSON_MESSAGE_CODE)) {
            try {
                int messageCode = videosJson.getInt(JSON_MESSAGE_CODE);
                if (messageCode != HttpURLConnection.HTTP_OK) return EMPTY_VIDEO_LIST;
            } catch (JSONException e) {
                e.printStackTrace();
                return EMPTY_VIDEO_LIST;
            }
        }

        // Get the JSON array of videos
        JSONArray videosJsonArray;
        try {
            videosJsonArray = videosJson.getJSONArray(JSON_RESULTS_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
            return EMPTY_VIDEO_LIST;
        }

        // Create a new VideoItemData array the size of the JSON array
        VideoItemData[] videosArray = new VideoItemData[videosJsonArray.length()];
        // Iterate over the JSON array to make VideoItemData objects
        for (int i = 0; i < videosArray.length; i++) {
            JSONObject videoJsonObj = null;
            try {
                videoJsonObj = videosJsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Create a VideoItemData object and set its info from the JSON
            try {
                String id = videoJsonObj.getString("id");
                String key = videoJsonObj.getString("key");
                String name = videoJsonObj.getString("name");
                String site = videoJsonObj.getString("site");
                String type = videoJsonObj.getString("type");
                String thumb = context.getResources().getString(R.string.tmdb_youtube_thumb_url);
                String placeholder = context.getResources().getString(R.string.tmdb_youtube_thumb_key_replacement);
                thumb = thumb.replace(placeholder, key);

                VideoItemData video = new VideoItemData(id, key, name, site, type, thumb);
                videosArray[i] = video;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return videosArray;
    }

    public static ReviewItemData[] getReviewsFromJson(Context context, String jsonData) {
        JSONObject reviewJson;
        try {
            reviewJson = new JSONObject(jsonData);
        } catch(JSONException e) {
            e.printStackTrace();
            return EMPTY_REVIEW_LIST;
        }

        // Check for unexpected return message
        if (reviewJson.has(JSON_MESSAGE_CODE)) {
            try {
                int code = reviewJson.getInt(JSON_MESSAGE_CODE);
                if (code != HttpURLConnection.HTTP_OK) return EMPTY_REVIEW_LIST;
            } catch(JSONException e) {
                e.printStackTrace();
                return EMPTY_REVIEW_LIST;
            }
        }

        // Get the JSON array of reviews
        JSONArray reviewsJsonArray;
        try {
            reviewsJsonArray = reviewJson.getJSONArray(JSON_RESULTS_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
            return EMPTY_REVIEW_LIST;
        }

        // Create a new reviewsItemData array the size of the JSON array
        ReviewItemData[] reviewsArray = new ReviewItemData[reviewsJsonArray.length()];
        // Iterate over the array to make the review item data objects
        for (int i = 0; i < reviewsArray.length; i++) {
            try {
                JSONObject reviewJsonObj = reviewsJsonArray.getJSONObject(i);
                String id = reviewJsonObj.getString("id");
                String author = reviewJsonObj.getString("author");
                String content = reviewJsonObj.getString("content");
                String url = reviewJsonObj.getString("url");

                ReviewItemData review = new ReviewItemData(id, author, content, url);
                reviewsArray[i] = review;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return reviewsArray;
    }

}
