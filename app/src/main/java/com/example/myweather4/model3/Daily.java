package com.example.myweather4.model3;

public class Daily {
    private String fxDate;//预报时间
    private String tempMax;//最高温度
    private String tempMin;//最低温度
    private String iconDay;//白天天气状况图片，
    private String textDay;//白天天气状况文字描述
    private String iconNight;//夜晚天气状况图片
    private String textNight;//夜晚天气状况图片

    @Override
    public String toString() {
        return "预报时间：" + fxDate +
                "\n最高温度：" + tempMax +
                "\n最低气温：" + tempMin +
                "\n白天天气状况：" + textDay +
                "\n夜晚天气状况：" + textNight;
    }
}
