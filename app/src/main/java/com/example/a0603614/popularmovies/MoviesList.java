package com.example.a0603614.popularmovies;

import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.a0603614.popularmovies.adapters.MovieListAdapter;
import com.example.a0603614.popularmovies.loaders.MovieListCursorLoader;
import com.example.a0603614.popularmovies.loaders.MovieListLoader;
import com.example.a0603614.popularmovies.movieobjects.MovieItemData;
import com.example.a0603614.popularmovies.utilities.Api;

public class MoviesList extends AppCompatActivity implements
        MovieListAdapter.ListItemClickListener {

    private static final String POPULAR = "popular";
    private static final String RATED = "rated";
    private static final String FAVORITE = "favorite";
    // Need to hold onto the adapter and recycler
    private MovieListAdapter mMoviesAdapter;
    private RecyclerView mMoviesRecycler;
    private Uri mTopRatedUri;
    private Uri mPopularUri;
    private String mSelectedSort = POPULAR;
    private int mMovieListLoaderId = 1993;
    private static final int REQUEST_CODE = 1970;


    /****** Begin loader methods for retrieving movie lists from TMDB API ******/
    private LoaderManager.LoaderCallbacks<MovieItemData[]> mMovieItemLoaderCallback =
            new LoaderManager.LoaderCallbacks<MovieItemData[]>() {
                @Override
                public Loader<MovieItemData[]> onCreateLoader(int i, Bundle bundle) {
                    mMovieListLoaderId = i;
                    return new MovieListLoader(MoviesList.this, bundle.getString("queryUrl"));
                }

                @Override
                public void onLoadFinished(Loader<MovieItemData[]> loader, MovieItemData[] movieItemData) {
                    // Check that we have movies to display
                    if (movieItemData == null) return;

                    mMoviesAdapter.setMovieData(movieItemData);

                    //TODO: End loading indicator
                }

                @Override
                public void onLoaderReset(Loader<MovieItemData[]> loader) {

                }
            };
    /****** End loader methods for retrieving movie lists from TMDB API ******/


    /****** Begin loader methods for retrieving movie lists from SQLite ******/
    private LoaderManager.LoaderCallbacks<MovieItemData[]> mMovieCursorLoaderCallback =
            new LoaderManager.LoaderCallbacks<MovieItemData[]>() {
                @Override
                public Loader<MovieItemData[]> onCreateLoader(int i, Bundle bundle) {
                    mMovieListLoaderId = i;
                    return new MovieListCursorLoader(MoviesList.this);
                }

                @Override
                public void onLoadFinished(Loader<MovieItemData[]> loader, MovieItemData[] movieItemData) {
                    // Check that we have movies to display
                    if (movieItemData == null) return;

                    mMoviesAdapter.setMovieData(movieItemData);

                    //TODO: End loading indicator
                }

                @Override
                public void onLoaderReset(Loader<MovieItemData[]> loader) {

                }
            };

    /****** End loader methods for retrieving movie lists from SQLite ******/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        if (savedInstanceState != null) {
            mSelectedSort = savedInstanceState.getString("selectedSort");
        }

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

        // Determine whether we're portrait or landscape and set the number of spans accordingly
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            spanCount = 3;

        // Create a grid layout manager for use in the display of the recycler
        // and set display properties for the recycler
        GridLayoutManager gridManager = new GridLayoutManager(this, spanCount,
                                                              GridLayoutManager.VERTICAL, false
        );
        mMoviesRecycler.setLayoutManager(gridManager);
        mMoviesRecycler.setHasFixedSize(true);
        mMoviesRecycler.addItemDecoration(new RecyclerViewItemDecorator(0));

        // Check if there are already existing movies list loaders
        if (getSupportLoaderManager().getLoader(mMovieListLoaderId) != null) {
            getSupportLoaderManager().initLoader(
                    mMovieListLoaderId, null, mMovieItemLoaderCallback);
        }

        loadMovieData();
    }



    /****** Begin interaction handling
     // Set up all the click interactions for the activity. This includes
     // the click listener for the grid view as well as the menu
     // creation and click handling.
     ******/
    @Override
    public void onListItemClick(int itemIndex) {
        Context context = this;
        Class detinationClass = MovieDetails.class;
        Intent showMovieDetails = new Intent(context, detinationClass);
        showMovieDetails.putExtra("movieData", mMoviesAdapter.getMovieData(itemIndex));
        showMovieDetails.putExtra("selectedSort", mSelectedSort);
        startActivityForResult(showMovieDetails, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) return;

        mSelectedSort = data.getStringExtra("selectedSort");
        super.onActivityResult(requestCode, resultCode, data);
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
            case R.id.menu_favorite: {
                mSelectedSort = FAVORITE;
                screenTitle = "Favorite Movies";
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
    /****** End interaction handling ******/


    /****** Begin method for restarting loaders based on menu selection ******/
    private void loadMovieData() {
        // TODO: Set up a loading indicator

        // Start up the loader with the appropriate bundle and callback, depending
        // on which menu item was selected
        Bundle sortBundle = new Bundle();
        if (mSelectedSort.equals(POPULAR)) {
            sortBundle.putString("queryUrl", mPopularUri.toString());
            getSupportLoaderManager().restartLoader(
                    mMovieListLoaderId, sortBundle, mMovieItemLoaderCallback);
        } else if (mSelectedSort.equals(RATED)) {
            sortBundle.putString("queryUrl", mTopRatedUri.toString());
            getSupportLoaderManager().restartLoader(
                    mMovieListLoaderId, sortBundle, mMovieItemLoaderCallback);
        } else if (mSelectedSort.equals(FAVORITE)) {
            getSupportLoaderManager().restartLoader(
                    mMovieListLoaderId, null, mMovieCursorLoaderCallback);
        }
    }

    /****** End method for restarting loaders based on menu selection ******/

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


    //TODO: Loader methods for SQLite Cursor holding favorite movies

}
