package com.antonkokoryshkin.flickrrecent.service;

import com.antonkokoryshkin.flickrrecent.model.FAnswersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApi {

    @GET("/services/rest")
    Call<FAnswersResponse> getData(@Query("method") String methodName, @Query("api_key") String apiKey, @Query("page") int page, @Query("format") String format, @Query("nojsoncallback") String nojsoncallback, @Query("extras") String extras);
}
