package com.example.a0603614.popularmovies;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a0603614.popularmovies.adapters.VideoListAdapter;
import com.example.a0603614.popularmovies.loaders.VideoListLoader;
import com.example.a0603614.popularmovies.movieobjects.VideoItemData;

public class VideosListFragment extends Fragment implements VideoListAdapter.ListItemClickListener {

    private static int VIDEO_LOADER_ID = 1995;
    private RecyclerView mVideosRecycler;
    private VideoListAdapter mVideosAdapter;
    private LoaderManager.LoaderCallbacks<VideoItemData[]> mVideoLoaderCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize the view
        final View view = inflater.inflate(R.layout.activity_videos_list, container, false);

        // Create the adapter and bind it to the recycler view for the video list
        mVideosAdapter = new VideoListAdapter(view.getContext(), this);
        mVideosRecycler = view.findViewById(R.id.rv_videos_list);
        mVideosRecycler.setAdapter(mVideosAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        mVideosRecycler.setLayoutManager(linearLayoutManager);
        mVideosRecycler.setHasFixedSize(true);

        // Create the loader callback
        mVideoLoaderCallback =
                new LoaderManager.LoaderCallbacks<VideoItemData[]>() {
                    @Override
                    public Loader<VideoItemData[]> onCreateLoader(int i, Bundle bundle) {
                        return new VideoListLoader(view.getContext(), bundle.getString("queryUrl"));
                    }

                    @Override
                    public void onLoadFinished(Loader<VideoItemData[]> loader, VideoItemData[] videoItemData) {
                        // Check that there are videos
                        if (videoItemData == null) return;

                        mVideosAdapter.setVideoData(videoItemData);
                    }

                    @Override
                    public void onLoaderReset(Loader<VideoItemData[]> loader) {

                    }
                };

        // Check if there's already an active loader for the video list data
        if (getLoaderManager().getLoader(VIDEO_LOADER_ID) != null) {
            getLoaderManager().initLoader(VIDEO_LOADER_ID, null, mVideoLoaderCallback);
        }

        // Start the data load
        loadVideoListData();

        // Return the view now that everything is stood up
        return view;
    }

    @Override
    public void onListItemClick(int id) {
        //TODO: Create intent for opening YouTube video
    }

    public void loadVideoListData() {
        // Build the bundle with the queryString parameter
        Bundle videoBundle = new Bundle();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getResources().getString(R.string.tmdb_extras_scheme));
        builder.authority(getResources().getString(R.string.tmdb_extras_host));
        builder.appendEncodedPath(getResources().getString(
                R.string.tmdb_extras_path) + "/" + MovieDetails.getMovieId() + getResources().getString(
                R.string.tmdb_extras_videos_path));
        builder.appendQueryParameter(
                getResources().getString(R.string.tmdb_key_name),
                getResources().getString(R.string.tmdb_key_value)
        );
        Uri uri = builder.build();
        videoBundle.putString("queryUrl", uri.toString());

        // Restart the loader using the new bundle
        getLoaderManager().restartLoader(VIDEO_LOADER_ID, videoBundle, mVideoLoaderCallback);
    }
}
