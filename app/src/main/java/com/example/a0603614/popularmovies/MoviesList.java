package com.example.a0603614.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;

import com.example.a0603614.popularmovies.utilities.NetworkUtility;
import com.example.a0603614.popularmovies.utilities.TMDBDataTransform;

import java.io.IOException;

public class MoviesList extends AppCompatActivity implements MovieListAdapter.ListItemClickListener {

    // Need to hold onto the adapter and recycler
    private MovieListAdapter mMoviesAdapter;
    private RecyclerView mMoviesRecycler;

    private Uri mTopRatedUri;
    private Uri mPopularUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        // Create and hold the URIs for popular and top rated requests
        // Popularity URI
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(getResources().getString(R.string.tmdb_popularity_sort_scheme));
        uriBuilder.authority(getResources().getString(R.string.tmdb_popularity_sort_host));
        uriBuilder.encodedPath(getResources().getString(R.string.tmdb_popularity_sort_path));
        uriBuilder.appendQueryParameter(getResources().getString(R.string.tmdb_key_name),
                getResources().getString(R.string.tmdb_key_value));
        mPopularUri = uriBuilder.build();

        // Top-rated URI
        uriBuilder = new Uri.Builder();
        uriBuilder.scheme(getResources().getString(R.string.tmdb_top_rated_sort_scheme));
        uriBuilder.authority(getResources().getString(R.string.tmdb_top_rated_sort_host));
        uriBuilder.encodedPath(getResources().getString(R.string.tmdb_top_rated_sort_path));
        uriBuilder.appendQueryParameter(getResources().getString(R.string.tmdb_key_name),
                getResources().getString(R.string.tmdb_key_value));
        mTopRatedUri = uriBuilder.build();

        // Create the adapter instance
        mMoviesAdapter = new MovieListAdapter(this, this);
        // Create the recycler instance and set the adapter
        mMoviesRecycler = (RecyclerView) findViewById(R.id.rv_movie_list);
        mMoviesRecycler.setAdapter(mMoviesAdapter);

        // Create a grid layout manager for use in the display of the recycler
        // and set display properties for the recycler
        GridLayoutManager gridManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);
        mMoviesRecycler.setLayoutManager(gridManager);
        mMoviesRecycler.setHasFixedSize(true);

        loadMovieData();
    }

    private void loadMovieData() {
        // TODO: Get the sort type to pass in

        new FetchMoviesTask().execute("popular");
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, MovieItemData[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: Set up a loading indicator
        }

        @Override
        protected MovieItemData[] doInBackground(String... params) {
            // Check that there is a parameter work with
            if (params.length == 0) return null;

            // Get the URI to use for the selected sort
            Uri sortUri;
            String sort = params[0];
            if (sort == "popular") {
                sortUri = mPopularUri;
            } else if (sort == "toprated") {
                sortUri = mTopRatedUri;
            } else {
                return null;
            }

            try {
                // Get the JSON data
                String jsonMoviesResponse = NetworkUtility.getResponseFromHTTPUrl(sortUri);

                MovieItemData[] movieItemData = TMDBDataTransform.getMoviesFromJSON(
                        MoviesList.this, jsonMoviesResponse);

                return movieItemData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(MovieItemData[] movies) {
            //TODO: Hide loading indicator

            // Check that we have movies to display
            if (movies == null) return;

            mMoviesAdapter.setMovieData(movies);
        }
    }

    @Override
    public void onListItemClick(int itemIndex) {
        // TODO: Navigate to details screen on click
    }
}
