package com.antonkokoryshkin.flickrrecent.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrService {

    private static final String BASE_URL = "https://api.flickr.com/";

    private static Retrofit retrofit = null;

    public static FlickrApi getFlickrApi() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit.create(FlickrApi.class);

    }
}
