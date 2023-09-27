package com.example.myweather4.model;

public class Place {
    private String name;
    private String id;
    private String lat;//纬度
    private String lon;//经度
    private String adm1;//该地区或城市所属的一级行政区域
    private String adm2;//该地区或城市的上级行政区划名称
    private String country;//该地区所属国家
    private String tz;//时区
    private String type;//地区类型
    private String fxLink;//该地区的天气预报网页链接

    @Override
    public String toString() {
        return "地区/城市名：" + name + "\n地区id：" + id
                + "\n地区经、纬度：" + lon + lat
                + "\n地区的上级行政区域：" + adm1
                + "\n地区所属国家：" + country
                + "\n地区所在的时区：" + tz;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAdm1() {
        return adm1;
    }

    public String getCountry() {
        return country;
    }

    public String getTz() {
        return tz;
    }

    public String getFxLink() {
        return fxLink;
    }

    public String getAdm2() {
        return adm2;
    }
}
