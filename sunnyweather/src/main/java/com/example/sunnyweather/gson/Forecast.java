package com.example.sunnyweather.gson;

public class Forecast {
    public String date;
    public Temperature temperature;
    public More more;

    public static class Temperature {
        public String max;
        public String min;
    }

    public static class More {
        public String info;
    }
}
