package com.example.myweather4.model2;

public class Now {
    private String obsTime;//数据观测时间
    private String temp;//温度
    private String feelsLike;//体感温度
    private String icon;//天气状况的图标
    private String text;//天气状况的文字描述
    private String windDir;//风向文字描述
    private String windScale;//风力等级
    private String humidity;//相对湿度
    private String pressure;//大气压强(百帕)
    private String vis;//能见度（公里km）
    private String cloud;//云量(%)

    public String getRoughInfo() {
        return "温度：" + temp + "\n" + "体感温度：" + feelsLike
                + "\n天气状况：" + text + "\n风向：" + windDir
                + "\n风力等级：" + windScale;
    }
    public String getMoreInfo() {
        return "温度：" + temp + "\n" + "体感温度：" + feelsLike
                + "\n天气状况：" + text + "\n风向：" + windDir
                + "\n风力等级：" + windScale + "\n相对湿度：" + humidity + "%\n" +
                "大气压强：" + pressure + "百帕\n" +
                "能见度：" + vis + "km\n云量：" + cloud + "%";
    }

    public String getObsTime() {
        return obsTime;
    }

    public String getTemp() {
        return temp;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public String getWindDir() {
        return windDir;
    }

    public String getWindScale() {
        return windScale;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVis() {
        return vis;
    }

    public String getCloud() {
        return cloud;
    }
}
