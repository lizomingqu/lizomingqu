package com.example.sunnyweather.gson;

public class Suggestion {

    public Comfort comfort;
    public CarWash carWash;
    public Sport sport;

    public static class Comfort {
        public String info;
    }

    public static class CarWash {
        public String info;
    }

    public static class Sport {
        public String info;
    }
}
