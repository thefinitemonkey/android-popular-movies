package com.example.a0603614.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;

import com.example.a0603614.popularmovies.utilities.Api;
import com.example.a0603614.popularmovies.utilities.NetworkUtility;
import com.example.a0603614.popularmovies.utilities.TMDBDataTransform;

import java.io.IOException;

public class MoviesList extends AppCompatActivity implements MovieListAdapter.ListItemClickListener {

    // Need to hold onto the adapter and recycler
    private MovieListAdapter mMoviesAdapter;
    private RecyclerView mMoviesRecycler;

    private Uri mTopRatedUri;
    private Uri mPopularUri;

    private static final String POPULAR = "popular";
    private static final String RATED = "rated";

    private String mSelectedSort = POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        // Create and hold the URIs for popular and top rated requests
        // Popularity URI
        mPopularUri = Api.getPopularMoviesUri(this);
        // Top-rated URI
        mTopRatedUri = Api.getTopRatedMoviesUri(this);

        // Create the adapter instance
        mMoviesAdapter = new MovieListAdapter(this, this);
        // Create the recycler instance and set the adapter
        mMoviesRecycler = findViewById(R.id.rv_movie_list);
        mMoviesRecycler.setAdapter(mMoviesAdapter);

        // Create a grid layout manager for use in the display of the recycler
        // and set display properties for the recycler
        GridLayoutManager gridManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);
        mMoviesRecycler.setLayoutManager(gridManager);
        mMoviesRecycler.setHasFixedSize(true);
        mMoviesRecycler.addItemDecoration(new RecyclerViewItemDecorator(0));

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMoviesTask().execute(mSelectedSort);
    }

    public class RecyclerViewItemDecorator extends RecyclerView.ItemDecoration {
        private int spaceInPixels;

        public RecyclerViewItemDecorator(int spaceInPixels) {
            this.spaceInPixels = spaceInPixels;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = spaceInPixels;
            outRect.right = spaceInPixels;
            outRect.bottom = spaceInPixels;

            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = spaceInPixels;
            } else {
                outRect.top = 0;
            }
        }
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
            if (sort.equals(POPULAR)) {
                sortUri = mPopularUri;
            } else if (sort.equals(RATED)) {
                sortUri = mTopRatedUri;
            } else {
                return null;
            }

            try {
                // Get the JSON data
                String jsonMoviesResponse = NetworkUtility.getResponseFromHTTPUrl(sortUri);

                return TMDBDataTransform.getMoviesFromJSON(
                        MoviesList.this, jsonMoviesResponse);
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
        Context context = this;
        Class detinationClass = MovieDetails.class;
        Intent showMovieDetails = new Intent(context, detinationClass);
        showMovieDetails.putExtra("movieData", mMoviesAdapter.getMovieData(itemIndex));
        startActivity(showMovieDetails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Set up the menu items
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String screenTitle = "";

        switch (id) {
            case R.id.menu_popular: {
                mSelectedSort = POPULAR;
                screenTitle = "Popular Movies";
                break;
            }
            case R.id.menu_highest_rated: {
                mSelectedSort = RATED;
                screenTitle = "Highest Rated Movies";
                break;
            }
        }

        // Change the screen title to reflect the sort selection and load the data
        try {
            getSupportActionBar().setTitle(screenTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadMovieData();

        return super.onOptionsItemSelected(item);
    }
}
