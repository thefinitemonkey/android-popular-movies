package com.example.a0603614.popularmovies;

import android.content.res.Resources;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a0603614.popularmovies.adapters.ReviewListAdapter;
import com.example.a0603614.popularmovies.loaders.ReviewListLoader;
import com.example.a0603614.popularmovies.movieobjects.ReviewItemData;


public class ReviewsListFragment extends Fragment implements ReviewListAdapter.ListItemClickListener {
    private static final int REVIEW_LOADER_ID = 1997;

    private RecyclerView mReviewsRecycler;
    private String mParam2;

    private ReviewListAdapter mReviewsAdapter;
    private LoaderManager.LoaderCallbacks<ReviewItemData[]> mReviewLoaderCallback;
    private ReviewItemData[] mReviewsData;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reviews_list, container, false);
        // Create the reviews adapter
        mReviewsAdapter = new ReviewListAdapter(view.getContext(), this);
        mReviewsRecycler = view.findViewById(R.id.rv_reviews_list);
        mReviewsRecycler.setAdapter(mReviewsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);

        mReviewsRecycler.setLayoutManager(linearLayoutManager);
        mReviewsRecycler.setHasFixedSize(false);

        mReviewLoaderCallback = new LoaderManager.LoaderCallbacks<ReviewItemData[]>() {
            @Override
            public Loader<ReviewItemData[]> onCreateLoader(int i, Bundle bundle) {
                return new ReviewListLoader(view.getContext(), bundle.getString("queryUrl"));
            }

            @Override
            public void onLoadFinished(Loader<ReviewItemData[]> loader, ReviewItemData[] reviewItemData) {
                // Check that there are reviews
                if (reviewItemData == null) return;

                mReviewsData = reviewItemData;
                mReviewsAdapter.setReviewData(mReviewsData);
            }

            @Override
            public void onLoaderReset(Loader<ReviewItemData[]> loader) {

            }
        };

        // Check if there's already an active loader for the reviews
        if (getLoaderManager().getLoader(REVIEW_LOADER_ID) != null) {
            getLoaderManager().initLoader(REVIEW_LOADER_ID, null, mReviewLoaderCallback);
        }

        // Return the view now that everything is stood up
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Load the review data
        loadReviewListData();
    }

    @Override
    public void onListItemClick(int id) {
        return;
    }

    public void loadReviewListData() {
        // Create the uri for the reviews data request
        Resources resources = getResources();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(resources.getString(R.string.tmdb_extras_scheme));
        builder.authority(resources.getString(R.string.tmdb_extras_host));
        builder.appendEncodedPath(getResources().getString(
                R.string.tmdb_extras_path) + "/" + MovieDetails.getMovieId() + getResources().getString(
                R.string.tmdb_extras_reviews_path));
        builder.appendQueryParameter(
                resources.getString(R.string.tmdb_key_name),
                resources.getString(R.string.tmdb_key_value)
        );
        Uri uri = builder.build();

        Bundle reviewBundle = new Bundle();
        reviewBundle.putString("queryUrl", uri.toString());

        // Restart the loader with the new bundle
        getLoaderManager().restartLoader(REVIEW_LOADER_ID, reviewBundle, mReviewLoaderCallback);
    }
}
