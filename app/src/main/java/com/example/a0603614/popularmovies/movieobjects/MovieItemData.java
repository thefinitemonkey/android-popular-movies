package com.example.a0603614.popularmovies.movieobjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by A0603614 on 5/18/18.
 */


// Class for creating movie data object

public class MovieItemData implements Parcelable {
    public static final Parcelable.Creator<MovieItemData> CREATOR =
            new Parcelable.Creator<MovieItemData>() {
                public MovieItemData createFromParcel(Parcel in) {
                    return new MovieItemData(in);
                }

                public MovieItemData[] newArray(int size) {
                    return new MovieItemData[size];
                }
            };
    public final int voteCount;
    public final int id;
    public final Boolean video;
    public final float voteAverage;
    public final String title;
    public final float popularity;
    public final String posterPath;
    public final String originalLanguage;
    public final String originalTitle;
    public final int[] genreIDs;
    public final String backdropPath;
    public final Boolean adult;
    public final String overview;
    public Date releaseDate;

    public MovieItemData(int movieID, String movieTitle, int votes, float average,
                         float popularityScore, String poster, String origLanguage,
                         String origTitle, int[] genres, String backdrop,
                         Boolean adultFilm, String overviewText, Date release,
                         Boolean hasVideo) {
        id = movieID;
        title = movieTitle;
        voteCount = votes;
        voteAverage = average;
        popularity = popularityScore;
        posterPath = poster;
        originalLanguage = origLanguage;
        originalTitle = origTitle;
        genreIDs = genres;
        backdropPath = backdrop;
        adult = adultFilm;
        overview = overviewText;
        releaseDate = release;
        video = hasVideo;
    }

    public MovieItemData(Parcel in) {
        id = in.readInt();
        title = in.readString();
        voteCount = in.readInt();
        int v = in.readInt();
        if (v == 0) {
            video = false;
        } else {
            video = true;
        }
        voteAverage = in.readFloat();
        popularity = in.readFloat();
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        genreIDs = in.createIntArray();
        backdropPath = in.readString();
        int a = in.readInt();
        if (a == 0) {
            adult = false;
        } else {
            adult = true;
        }
        overview = in.readString();
        String date = in.readString();
        try {
            releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            releaseDate = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(voteCount);
        if (video) {
            parcel.writeInt(1);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeFloat(voteAverage);
        parcel.writeFloat(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeIntArray(genreIDs);
        parcel.writeString(backdropPath);
        if (adult) {
            parcel.writeInt(1);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeString(overview);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        parcel.writeString(dateFormat.format(releaseDate));
    }
}
