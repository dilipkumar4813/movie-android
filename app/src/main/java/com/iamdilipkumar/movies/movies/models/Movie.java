package com.iamdilipkumar.movies.movies.models;

/**
 * Created on 21/03/17.
 */

public class Movie {

    private String movieTitle;
    private String moviePlot;
    private String moviePoster;
    private String movieVoteCount;
    private String movieRelease;
    private String movieVoteAverage;
    private String movieLanguage;
    private String movieBanner;

    public Movie(String movieTitle, String moviePlot, String moviePoster, String movieVoteCount, String movieRelease, String movieVoteAverage, String movieLanguage, String movieBanner) {
        this.movieTitle = movieTitle;
        this.moviePlot = moviePlot;
        this.moviePoster = "http://image.tmdb.org/t/p/w342" + moviePoster;
        this.movieVoteCount = movieVoteCount;
        this.movieRelease = movieRelease;
        this.movieVoteAverage = movieVoteAverage;
        this.movieLanguage = movieLanguage;
        this.movieBanner = "http://image.tmdb.org/t/p/w500" + movieBanner;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getMovieVoteCount() {
        return movieVoteCount;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public String getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public String getMovieLanguage() {
        return movieLanguage;
    }

    public String getMovieBanner() {
        return movieBanner;
    }
}
