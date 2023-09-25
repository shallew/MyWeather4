package com.example.myweather4.model2;

public class RealtimeResponse {
    private String code;//状态码
    private String updateTime;//当前API的最近更新时间
    private Now now;

    @Override
    public String toString() {
        return "RealtimeResponse{" +
                "code='" + code + '\'' +
                ", \nupdateTime='" + updateTime + '\'' +
                ", \nnow=" + now.getRoughInfo()+
                '}';
    }

    public String getCode() {
        return code;
    }

    public Now getNow() {
        return now;
    }
}
