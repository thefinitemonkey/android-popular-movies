package com.example.a0603614.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class TMDBMoviesContract {
    // Define the base authority and content uri
    public static final String AUTHORITY = "com.example.a0603614.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths (tables)
    public static final String PATH_FAVORITES = "movies";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_REVIEWS = "reviews";

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        public static final String TABLE_NAME = "movies";

        // Define all the columns for the table
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VIDEO = "has_video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_POPULARITY_SCORE = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_GENRE_IDS = "genre_ids";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_ADULT_FILM = "adult";
        public static final String COLUMN_OVERVIEW = "overiew";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }

    public static final class VideosEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();
        public static final String TABLE_NAME = "videos";

        // Define all the columns for the table
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_KEY = "video_key";
        public static final String COLUMN_VIDEO_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_VIDEO_TYPE = "type";
    }

    public static final class ReviewsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String TABLE_NAME = "reviews";

        // Define all the columns for the table
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
    }
}
