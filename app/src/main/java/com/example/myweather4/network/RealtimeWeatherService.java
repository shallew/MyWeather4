package com.example.myweather4.network;

import com.example.myweather4.model2.RealtimeResponse;
import com.example.myweather4.model3.DailyWeatherResponse;
import com.example.myweather4.model4.HourlyWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RealtimeWeatherService {
    @GET("v7/weather/now?key=4de1c3e969234b9b8eb0e5bd772ee1ed")
    Call<RealtimeResponse> getRealtimeWeatherInfo(@Query("location")String location);
    //获取实时天气状况

    @GET("v7/weather/7d?key=621bfd0b3c1b49529df49076df657700")
    Call<DailyWeatherResponse> getDailyWeatherInfo(@Query("location")String location);
    //获取未来七天天气状况（包含今天）

    @GET("v7/weather/24h?key=621bfd0b3c1b49529df49076df657700")
    Call<HourlyWeatherResponse> getHourlyWeatherInfo(@Query("location")String location);
}
