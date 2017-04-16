package com.iamdilipkumar.movies.movies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 16/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class TrailersResult {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = null;

    public Integer getId() {
        return id;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
