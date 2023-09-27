package com.example.myweather4.model4;

public class Hourly {
    private String fxTime;//预报时间
    private String temp;//温度
    private String icon;//天气状况图标
    private String text;//天气状况文字
    private String windScale;//风力等级

    @Override
    public String toString() {
        return "\n预报时间：" + fxTime +
                "\n温度：" + temp +
                "\n天气状况：" + text +
                "\n风力等级：" + windScale + "级";
    }
}
