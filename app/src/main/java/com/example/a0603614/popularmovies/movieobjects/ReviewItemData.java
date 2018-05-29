package com.example.a0603614.popularmovies.movieobjects;

public class ReviewItemData {
    public final int id;
    public final String author;
    public final String content;
    public final String url;

    public ReviewItemData(int movieId, String reviewAuthor, String reviewContent, String reviewUrl) {
        id = movieId;
        author = reviewAuthor;
        content = reviewContent;
        url = reviewUrl;
    }
}
