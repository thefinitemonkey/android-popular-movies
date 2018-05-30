package com.example.a0603614.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a0603614.popularmovies.adapters.VideoListAdapter;

public class VideoList extends AppCompatActivity implements VideoListAdapter.ListItemClickListener{

    private RecyclerView mVideosRecycler;
    private VideoListAdapter mVideosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        // Create the adapter and bind it to the recycler view for the video list
        mVideosRecycler = findViewById(R.id.rv_videos_list);
        mVideosAdapter = new VideoListAdapter(this, this);
        mVideosRecycler.setAdapter(mVideosAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
    }

    @Override
    public void onListItemClick(int id) {
        //TODO: Create intent for opening YouTube video
    }
}
