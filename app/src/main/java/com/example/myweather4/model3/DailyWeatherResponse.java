package com.example.myweather4.model3;

import java.util.List;

public class DailyWeatherResponse {
    private String code;
    private String updateTime;
    private String fxLink;
    private List<Daily> daily;

    @Override
    public String toString() {
        return "DailyWeatherResponse{" +
                "code='" + code + '\'' +
                ", \nupdateTime='" + updateTime + '\'' +
                ", \nfxLink='" + fxLink + '\'' +
                ", \ndaily=" + daily.toString() +
                '}';
    }

    public String getCode() {
        return code;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public String getFxLink() {
        return fxLink;
    }
}
