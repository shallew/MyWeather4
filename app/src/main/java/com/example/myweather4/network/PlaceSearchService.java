package com.example.myweather4.network;

import com.example.myweather4.model.SearchCityResponse;
import com.example.myweather4.model2.RealtimeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceSearchService {

    @GET("v2/city/lookup?key=4de1c3e969234b9b8eb0e5bd772ee1ed")
    Call<SearchCityResponse> searchCity(@Query("location")String location);
}
