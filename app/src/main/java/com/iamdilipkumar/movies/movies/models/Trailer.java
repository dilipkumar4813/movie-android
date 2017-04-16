package com.iamdilipkumar.movies.movies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model to hold the data received from the API call
 * for individual trailer item
 *
 * Created on 16/04/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class Trailer {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("site")
    @Expose
    private String site;

    @SerializedName("size")
    @Expose
    private Integer size;

    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
