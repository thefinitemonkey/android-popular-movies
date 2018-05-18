package com.example.a0603614.popularmovies;

import java.util.Date;

/**
 * Created by A0603614 on 5/18/18.
 */


// Class for creating movie data object

public class MovieItemData {
    private int mVoteCount;
    private int mID;
    private Boolean mVideo;
    private float mVoteAverage;
    private String mTitle;
    private float mPopularity;
    private String mPosterPath;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private int[] mGenreIDs;
    private String mBackdropPath;
    private Boolean mAdult;
    private String mOverview;
    private Date mReleaseDate;

    public MovieItemData(int movieID, String movieTitle) {
        mID = movieID;
        mTitle = movieTitle;
    }

    public int getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setVoteCount(int voteCount) {
        mVoteCount = voteCount;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVideoAvailable(Boolean hasVideo) {
        mVideo = hasVideo;
    }

    public Boolean getVideoAvailable() {
        return mVideo;
    }

    public void setVoteAverage(float voteAverage) {
        mVoteAverage = voteAverage;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setOriginalLanguage(String originalLanguage) {
        mOriginalLanguage = originalLanguage;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void addGenreID(int pos, int genreID) {
        mGenreIDs[pos] = genreID;
    }

    public void setGenreIDs(int[] genreIDs) {
        mGenreIDs = genreIDs;
    }

    public int[] getGenreIDs() {
        return mGenreIDs;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setAdult(Boolean adult) {
        mAdult = adult;
    }

    public Boolean getAdult() {
        return mAdult;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }
}
