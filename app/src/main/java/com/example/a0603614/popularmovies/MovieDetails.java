package com.example.a0603614.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a0603614.popularmovies.database.TMDBMoviesContract;
import com.example.a0603614.popularmovies.loaders.FavoriteCheckLoader;
import com.example.a0603614.popularmovies.movieobjects.MovieItemData;
import com.example.a0603614.popularmovies.utilities.TMDBDataTransform;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetails extends AppCompatActivity {

    private static final int mFavoriteCheckLoaderId = 2002;
    private static int mMovieId;
    private String mSourceSort;
    private ImageView mivFavorite;
    private MovieItemData movieData;
    private String mImageSize;
    private Boolean mIsFavorite = false;
    private LoaderManager.LoaderCallbacks<Boolean> mFavoriteCheckCallback =
            new LoaderManager.LoaderCallbacks<Boolean>() {
                @Override
                public Loader<Boolean> onCreateLoader(int i, Bundle bundle) {
                    return new FavoriteCheckLoader(MovieDetails.this, movieData.id);
                }

                @Override
                public void onLoadFinished(Loader<Boolean> loader, Boolean movieItemData) {
                    // Check that we have movies to display
                    if (movieItemData == null) return;
                    if (movieItemData) {
                        mIsFavorite = true;
                    }
                    displayFavoriteIcon();
                }

                @Override
                public void onLoaderReset(Loader<Boolean> loader) {

                }
            };

    public static int getMovieId() {
        return mMovieId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW: {
                mImageSize = "w92";
                break;
            }
            case DisplayMetrics.DENSITY_MEDIUM:
            case DisplayMetrics.DENSITY_TV: {
                mImageSize = "w154";
                break;
            }
            case DisplayMetrics.DENSITY_HIGH:
            case DisplayMetrics.DENSITY_XHIGH: {
                mImageSize = "w342";
                break;
            }
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH: {
                mImageSize = "w500";
                break;
            }
        }

        // Hold on to the sort view setting we came from
        mSourceSort = getIntent().getStringExtra("selectedSort");

        // Get the movie object from the intent
        movieData = getIntent().getParcelableExtra("movieData");

        // Get the movieID
        mMovieId = movieData.id;

        // Get screen pixels
        int screenPixels = getResources().getDisplayMetrics().widthPixels;
        // Set the display from the movie data

        // Backdrop image
        String backdropUrl = getResources().getString(R.string.tmdb_poster_base_url) +
                mImageSize + movieData.backdropPath;
        ImageView backdropImage = findViewById(R.id.iv_details_backdrop);
        // Use Picasso to set the image into the image view on this display
        Picasso.with(this).load(backdropUrl).into(backdropImage);
        backdropImage.setMinimumWidth(screenPixels / 3);
        backdropImage.setMaxWidth(screenPixels / 3);

        // Poster image
        String posterUrl = getResources().getString(R.string.tmdb_poster_base_url) +
                mImageSize + movieData.posterPath;
        ImageView posterImage = findViewById(R.id.iv_details_poster);
        // Use Picasso to set the image into the image view on this display
        Picasso.with(this).load(posterUrl).into(posterImage);
        posterImage.setMinimumWidth(screenPixels / 3);
        posterImage.setMaxWidth(screenPixels / 3);

        // Release date
        TextView tvReleaseDate = findViewById(R.id.tv_release_date);
        Date releaseDate = movieData.releaseDate;
        DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        String strReleaseDate = formatter.format(releaseDate);
        tvReleaseDate.setText(strReleaseDate);

        // Average vote
        TextView tvAvgVote = findViewById(R.id.tv_vote_average);
        String avgVote = movieData.voteAverage + "";
        tvAvgVote.setText(avgVote);

        // Movie title
        TextView tvTitle = findViewById(R.id.tv_movie_title);
        tvTitle.setText(movieData.title);

        // Synopsis
        TextView tvSynopsis = findViewById(R.id.tv_synopsis);
        tvSynopsis.setText(movieData.overview);

        // Make the request to check whether this is a favorite
        getSupportLoaderManager().restartLoader(
                mFavoriteCheckLoaderId, null, mFavoriteCheckCallback);
        mivFavorite = findViewById(R.id.iv_favorite);

    }

    private void displayFavoriteIcon() {
        if (mIsFavorite) {
            mivFavorite.setImageResource(R.drawable.like_selected);
        } else {
            mivFavorite.setImageResource(R.drawable.like_unselected);
        }
    }

    public void toggleFavorite(View view) {
        mIsFavorite = !mIsFavorite;
        if (mIsFavorite) {
            mivFavorite.setImageResource(R.drawable.like_selected);
            addToFavoritesDb();
        } else {
            mivFavorite.setImageResource(R.drawable.like_unselected);
            removeFromFavoritesDb();
        }
        displayFavoriteIcon();
    }

    private void addToFavoritesDb() {
        // Initialize placeholder values for things that don't store directly
        String genres = TMDBDataTransform.convertIntArrayToString(movieData.genreIDs);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(movieData.releaseDate);
        int hasVideo = 0;
        if (movieData.video) hasVideo = 1;
        int isAdult = 0;
        if (movieData.adult) isAdult = 1;

        // Initialize the contentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieData.id);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, movieData.title);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, movieData.voteCount);
        contentValues.put(
                TMDBMoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movieData.voteAverage);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_VIDEO, hasVideo);
        contentValues.put(
                TMDBMoviesContract.MoviesEntry.COLUMN_POPULARITY_SCORE, movieData.popularity);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movieData.posterPath);
        contentValues.put(
                TMDBMoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE,
                movieData.originalLanguage
        );
        contentValues.put(
                TMDBMoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, movieData.originalTitle);
        contentValues.put(
                TMDBMoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, movieData.backdropPath);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_ADULT_FILM, isAdult);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_OVERVIEW, movieData.overview);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, date);
        contentValues.put(TMDBMoviesContract.MoviesEntry.COLUMN_GENRE_IDS, genres);

        // Call the content provider to add the favorite
        getContentResolver().insert(TMDBMoviesContract.MoviesEntry.CONTENT_URI, contentValues);
    }

    private void removeFromFavoritesDb() {
        // Make the call to the content provider to remove the favorite
        getContentResolver().delete(
                TMDBMoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendEncodedPath(
                        "/" + movieData.id).build(), null, null);
    }

    @Override
    public void onBackPressed() {
        prepareReturnToParent();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    private void prepareReturnToParent() {
        Intent intent = new Intent();
        intent.putExtra("selectedSort", mSourceSort);
        setResult(1, intent);
    }
}
