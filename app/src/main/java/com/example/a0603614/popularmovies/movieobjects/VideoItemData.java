package com.example.a0603614.popularmovies.movieobjects;

public class VideoItemData {
    public final int id;
    public final String key;
    public final String name;
    public final String site;
    public final String type;
    public final String thumbUrl;

    public VideoItemData(int movieId, String videoKey, String videoName, String videoSite, String videoType, String thumb) {
        id = movieId;
        name = videoName;
        key = videoKey;
        site = videoSite;
        type = videoType;
        thumbUrl = thumb;
    }
}
