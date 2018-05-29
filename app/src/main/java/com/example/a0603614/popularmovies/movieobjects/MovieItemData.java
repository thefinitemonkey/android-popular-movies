package com.example.a0603614.popularmovies.movieobjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by A0603614 on 5/18/18.
 */


// Class for creating movie data object

public class MovieItemData implements Parcelable {
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


    public MovieItemData(Parcel in) {
        mID = in.readInt();
        mTitle = in.readString();
        mVoteCount = in.readInt();
        int video = in.readInt();
        if (video == 0) {
            mVideo = false;
        } else {
            mVideo = true;
        }
        mVoteAverage = in.readFloat();
        mPopularity = in.readFloat();
        mPosterPath = in.readString();
        mOriginalLanguage = in.readString();
        mOriginalTitle = in.readString();
        mGenreIDs = in.createIntArray();
        mBackdropPath = in.readString();
        int adult = in.readInt();
        if (adult == 0) {
            mAdult = false;
        } else {
            mAdult = true;
        }
        mOverview = in.readString();
        String date = in.readString();
        try {
            mReleaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            mReleaseDate = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeString(mTitle);
        parcel.writeInt(mVoteCount);
        if(mVideo) {
            parcel.writeInt(1);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeFloat(mVoteAverage);
        parcel.writeFloat(mPopularity);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOriginalLanguage);
        parcel.writeString(mOriginalTitle);
        parcel.writeIntArray(mGenreIDs);
        parcel.writeString(mBackdropPath);
        if (mAdult) {
            parcel.writeInt(1);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeString(mOverview);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        parcel.writeString(dateFormat.format(mReleaseDate));
    }

    public static final Parcelable.Creator<MovieItemData> CREATOR =
            new Parcelable.Creator<MovieItemData> () {
        public MovieItemData createFromParcel(Parcel in) {
            return new MovieItemData(in);
        }

        public MovieItemData[] newArray(int size) {
            return new MovieItemData[size];
        }
    };
}
