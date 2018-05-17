package com.example.a0603614.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;

public class MoviesList extends AppCompatActivity {

    // Need to hold onto the adapter and recycler
    private Adapter mMoviesAdapter;
    private RecyclerView mMoviesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        mMoviesRecycler = (RecyclerView) findViewById(R.id.rv_movie_list);

        // Create a grid layout manager for use in the display of the recycler
        // and set display properties for the recycler
        GridLayoutManager gridManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);
        mMoviesRecycler.setLayoutManager(gridManager);
        mMoviesRecycler.setHasFixedSize(true);

    }


}
