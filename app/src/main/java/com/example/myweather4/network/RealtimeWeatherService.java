package com.example.myweather4.network;

import com.example.myweather4.model2.RealtimeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RealtimeWeatherService {
    @GET("v7/weather/now?key=4de1c3e969234b9b8eb0e5bd772ee1ed")
    Call<RealtimeResponse> getRealtimeWeatherInfo(@Query("location")String location);
}
