package com.example.myweather4.model4;

import java.util.List;

public class HourlyWeatherResponse {
    private String code;
    private String updateTime;//观测时间
    private String fxLink;
    private List<Hourly> hourly;

    @Override
    public String toString() {
        return "HourlyWeatherResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", fxLink='" + fxLink + '\'' +
                ", hourly=" + hourly.toString() +
                '}';
    }

    public String getCode() {
        return code;
    }

    public String getFxLink() {
        return fxLink;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }
}
