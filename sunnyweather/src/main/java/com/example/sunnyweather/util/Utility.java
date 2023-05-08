package com.example.sunnyweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.sunnyweather.dao.CityDao;
import com.example.sunnyweather.db.City;
import com.example.sunnyweather.db.County;
import com.example.sunnyweather.db.Province;
import com.example.sunnyweather.gson.Aqi;
import com.example.sunnyweather.gson.Basic;
import com.example.sunnyweather.gson.Forecast;
import com.example.sunnyweather.gson.Now;
import com.example.sunnyweather.gson.Suggestion;
import com.example.sunnyweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utility {


    //省
    public static boolean handleProvinceResponse(String response, Context context){
        if (!TextUtils.isEmpty(response)){

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    CityDao cityDao = CityDao.getInstance(context);
                    Province province = new Province();
                    province.setProvinceName(name);
                    province.setProvinceCode(id);
                    cityDao.provinceadd(province);
                }
                return true;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    //市
    public static boolean handleCityResponse(String response,int provinceId,Context context){
        if (!TextUtils.isEmpty(response)){
            JSONArray jsonArray = null;
            try {
                CityDao cityDao = CityDao.getInstance(context);
                City city = new City();
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    city.setCityName(name);
                    city.setCityCode(id);
                    city.setProvinceId(provinceId);
                    cityDao.cityadd(city);
                }
                return true;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    //县
    public static boolean handleCountyResponse(String response,int cityId,Context context){
        if (!TextUtils.isEmpty(response)){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String id = jsonArray.getJSONObject(i).getString("weather_id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    CityDao cityDao = CityDao.getInstance(context);
                    County county = new County();
                    county.setCountyName(name);
                    county.setWeatherId(id);
                    county.setCityId(cityId);
                    cityDao.countyadd(county);

                    /*SharedPreferences weather = context.getSharedPreferences("weather", 0);
                    SharedPreferences.Editor edit = weather.edit();
                    SharedPreferences.Editor weather1 = edit.putString("weather_id", id);
                    edit.commit();*/

                }
                return true;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response,Context context){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather");
            for (int i = 0; i < heWeather.length(); i++) {

                Forecast forecastone = new Forecast();
                Basic basicone = new Basic();
                Suggestion suggest = new Suggestion();
                Aqi aqitest = new Aqi();
                Now nowone = new Now();
                Weather weather = new Weather();


                JSONObject basic = heWeather.getJSONObject(i).getJSONObject("basic");
                String city = basic.getString("city");
                String id = basic.getString("id");
                JSONObject update = basic.getJSONObject("update");
                String data = update.getString("loc");

                Log.d("ninger",city+"/"+id+"/"+data);

                basicone.cityName = city;
                basicone.weatherId = id;
                Basic.Update update1 = new Basic.Update();
                update1.updateTime = data;
                basicone.update = update1;
                Log.d("ninger",city+"/"+id+"/"+data);

                JSONObject now = heWeather.getJSONObject(i).getJSONObject("now");
                String wind_dir = now.getString("wind_dir");
                JSONObject cond = now.getJSONObject("cond");
                String txt = cond.getString("txt");
                nowone.temperature =wind_dir;
                Log.d("ninger",wind_dir+"/"+txt);
                Now.More more = new Now.More();
                more.info = txt;
                nowone.more = more;
                Log.d("ninger",wind_dir+"/"+txt);

                JSONArray forecast = heWeather.getJSONObject(i).getJSONArray("daily_forecast");
                for (int j = 0; j < forecast.length(); j++) {
                    JSONObject jsonObject1 = forecast.getJSONObject(i);
                    String date = jsonObject1.getString("date");
                    String max = jsonObject1.getJSONObject("tmp").getString("max");
                    String min = jsonObject1.getJSONObject("tmp").getString("min");
                    String txt_d = jsonObject1.getJSONObject("cond").getString("txt_d");
                    forecastone.date = date;

                    Forecast.Temperature temperature = new Forecast.Temperature();
                    temperature.max = max;
                    temperature.min = min;
                    forecastone.temperature = temperature;

                    Forecast.More more1 = new Forecast.More();
                    more1.info = txt_d;
                    forecastone.more=more1;

                    List<Forecast> forecastList = new ArrayList<>();
                    forecastList.add(forecastone);
                    weather.forecastList=forecastList;

                    Log.d("ninger",date+"/"+max+"/"+min+"/"+txt_d);
                }

                JSONObject aqi = heWeather.getJSONObject(i).getJSONObject("aqi");
                JSONObject city_api = aqi.getJSONObject("city");
                String aqi_zs = city_api.getString("aqi");
                String pm25 = city_api.getString("pm25");

                Aqi.AQICity aqiCity = new Aqi.AQICity();
                aqiCity.api = aqi_zs;
                aqiCity.pm25 = pm25;
                aqitest.aqiCity = aqiCity;
                Log.d("ninger",aqi_zs+"/"+pm25);

                JSONObject suggestion = heWeather.getJSONObject(i).getJSONObject("suggestion");
                String comf_txt = suggestion.getJSONObject("comf").getString("txt");
                String sport_txt = suggestion.getJSONObject("sport").getString("txt");
                String cw_txt = suggestion.getJSONObject("cw").getString("txt");

                Suggestion.Comfort comfort = new Suggestion.Comfort();
                Suggestion.Sport sport = new Suggestion.Sport();
                Suggestion.CarWash carWash = new Suggestion.CarWash();

                comfort.info = comf_txt;
                sport.info = sport_txt;
                carWash.info = cw_txt;

                suggest.comfort=comfort;
                suggest.sport = sport;
                suggest.carWash = carWash;
                Log.d("ninger",comf_txt+"/"+sport_txt+"/"+cw_txt);

                String status = heWeather.getJSONObject(i).getString("status");

                weather.status =  status;
                weather.aqi = aqitest;
                weather.basic = basicone;
                weather.now = nowone;
                weather.suggestion = suggest;

                return weather;
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
