package com.example.myweather4.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceServiceCreator {
    private static final OkHttpClient okHttpClient= new OkHttpClient.Builder()
            .addInterceptor(new GzipRequestInterceptor())
            .build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://geoapi.qweather.com/")//根路径
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();
    public static Object create(Class serviceClass){
        return retrofit.create(serviceClass);
    }
}

