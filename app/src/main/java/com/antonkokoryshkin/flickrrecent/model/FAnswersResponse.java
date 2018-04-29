package com.antonkokoryshkin.flickrrecent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FAnswersResponse {

    @SerializedName("photos")
    @Expose
    private Photos photos;

    public Photos getPhotos() {
        return photos;
    }

}
