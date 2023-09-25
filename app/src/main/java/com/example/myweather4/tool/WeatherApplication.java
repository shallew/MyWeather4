package com.example.myweather4.tool;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.example.myweather4.model.Place;
import com.example.myweather4.model2.Now;

public class WeatherApplication extends Application {
    public static Place chosenPlace;//该字段保存选择的地区
    public static boolean showMore = false;//该字段标志是否显示详细天气信息
    public static Now nowWeather;//该字段记录实况天气

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}