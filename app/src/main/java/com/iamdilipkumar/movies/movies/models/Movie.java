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

    /**
     * Constructor to initialize the Movie class for referencing in adapter and activity
     *
     * @param movieTitle store the title of the movie
     * @param moviePlot store the plot of the movie
     * @param moviePoster store the url for the poster
     * @param movieVoteCount store the vote count of the movie
     * @param movieRelease store the release date of the movie
     * @param movieVoteAverage store the average voting for the movie
     * @param movieLanguage store the original lanugage of them movie
     * @param movieBanner store the banner url of the movie
     */
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

    /**
     *
     * @return the title of the movie in string format
     */
    public String getMovieTitle() {
        return movieTitle;
    }

    /**
     *
     * @return the movie plot in string format
     */
    public String getMoviePlot() {
        return moviePlot;
    }

    /**
     *
     * @return return the poster url in string format
     */
    public String getMoviePoster() {
        return moviePoster;
    }

    /**
     *
     * @return the movie vote count in string format
     */
    public String getMovieVoteCount() {
        return movieVoteCount;
    }

    /**
     *
     * @return the movie release date in string format
     */
    public String getMovieRelease() {
        return movieRelease;
    }

    /**
     *
     * @return the average vote rate of the movie in string format
     */
    public String getMovieVoteAverage() {
        return movieVoteAverage;
    }

    /**
     *
     * @return the original lanugage of the movie in string format
     */
    public String getMovieLanguage() {
        return movieLanguage;
    }

    /**
     *
     * @return the movie banner url in string format
     */
    public String getMovieBanner() {
        return movieBanner;
    }
}
