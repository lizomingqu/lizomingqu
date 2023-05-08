package com.example.sunnyweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunnyweather.gson.Forecast;
import com.example.sunnyweather.gson.Weather;
import com.example.sunnyweather.util.HttpUtil;
import com.example.sunnyweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView titleUpdateTime;
    private TextView wetherInfoText;
    private LinearLayout forecastLayout;
    private TextView apiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        bingPicImg = findViewById(R.id.bing_pic_img);

        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        degreeText = findViewById(R.id.degree_text);
        titleUpdateTime = findViewById(R.id.title_update_time);
        wetherInfoText = findViewById(R.id.wether_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        apiText = findViewById(R.id.api_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);

        SharedPreferences weather = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = weather.edit();
        String weatherString = weather.getString("weather", null);
        if (weatherString!=null){
            Weather weather1 = Utility.handleWeatherResponse(weatherString, getApplication());
            showWeatherInfo(weather1);
            SharedPreferences.Editor weather2 = edit.putString("weather", null);
            weather2.commit();
        }else {
            String weatherId = getIntent().getStringExtra("weather_id");
            Log.d("ninger",weatherId);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
            SharedPreferences.Editor weather2 = edit.putString("weather", null);
            weather2.commit();
        }

        String bing_pic = weather.getString("bing_pic", null);
        if (bing_pic!=null){

        }else {
            loadBingPic();
        }
    }

    private void loadBingPic() {

    }

    private void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=QMGUg1vq2FK7wyBi";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText, getApplication());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            edit.putString("weather",responseText);
                            edit.apply();
                            showWeatherInfo(weather);
                        }
                    }
                });
            }
        });

    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        wetherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView datetext = view.findViewById(R.id.datetext);
            TextView info_text = view.findViewById(R.id.info_text);
            TextView max_text = view.findViewById(R.id.max_text);
            TextView min_text = view.findViewById(R.id.min_text);

            datetext.setText(forecast.date);
            info_text.setText(forecast.more.info);
            max_text.setText(forecast.temperature.max);
            min_text.setText(forecast.temperature.min);

            Log.d("ninger",forecast.toString());

            forecastLayout.addView(view);
        }

        String api = weather.aqi.aqiCity.api;
        String pm25 = weather.aqi.aqiCity.pm25;

        apiText.setText(api);
        pm25Text.setText(pm25);

        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数："+ weather.suggestion.carWash.info;
        String sport = "运动指数："+weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

    }
}



















