package com.antonkokoryshkin.flickrrecent.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photo implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("farm")
    @Expose
    private Integer farm;
    @SerializedName("url_s")
    @Expose
    private String urlS;

    public String getUrlS() {
        return urlS;
    }

    public Uri getPhotoUri(){
        return Uri.parse("https://farm" + farm + ".staticflickr.com/")
                .buildUpon()
                .appendPath(server)
                .appendPath(id + "_" + secret + ".jpg")
                .build();
    }

    public Uri getPhotoPageUri() {
        return Uri.parse("https://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(owner)
                .appendPath(id)
                .build();
    }

}
