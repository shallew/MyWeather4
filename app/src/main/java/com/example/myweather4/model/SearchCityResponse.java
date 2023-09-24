package com.example.myweather4.model;


import java.util.List;

public class SearchCityResponse {
    private String code;
    private List<Place> location;

    public List<Place> getLocation() {
        return location;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SearchCityResponse{" +
                "location=" + location.toString() +
                '}';
    }
}
