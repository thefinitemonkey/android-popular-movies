package com.example.a0603614.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a0603614.popularmovies.movieobjects.MovieItemData;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetails extends AppCompatActivity {

    private MovieItemData movieData;
    private String mImageSize;

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

        // Get the movie object from the intent
        movieData = getIntent().getParcelableExtra("movieData");

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
    }
}
