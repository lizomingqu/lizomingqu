package com.example.sunnyweather.gson;

import javax.net.ssl.SSLContext;

public class Basic {

    public String cityName;
    public String weatherId;
    public Update update;

    public static class Update {

        public String updateTime;

    }
}
