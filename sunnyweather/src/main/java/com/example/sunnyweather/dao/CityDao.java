package com.example.sunnyweather.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.sunnyweather.db.City;
import com.example.sunnyweather.db.Citybasedate;
import com.example.sunnyweather.db.County;
import com.example.sunnyweather.db.Province;

import java.util.ArrayList;
import java.util.List;

public class CityDao {

    private static CityDao cityDao;
    private Citybasedate citybasedate;

    private CityDao(Context context) {
        if (citybasedate==null){
            citybasedate = Citybasedate.getInstance(context);
        }
    }

    public static CityDao getInstance(Context context){
        if (cityDao == null){
            cityDao = new CityDao(context);
        }
        return cityDao;
    }

    public void provinceadd(Province province){
        ContentValues values = new ContentValues();
        values.put("provinceName",province.getProvinceName());
        values.put("provinceCode",province.getProvinceCode());
        citybasedate.getWritableDatabase().insert(citybasedate.province_name,null,values);
    }

    public List<Province> provincequery(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = citybasedate.getReadableDatabase().query(citybasedate.province_name, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Province province = new Province();
            province.setId(cursor.getInt(0));
            province.setProvinceName(cursor.getString(1));
            province.setProvinceCode(cursor.getInt(2));
            list.add(province);
        }
        return list;
    }

    public List<City> cityquery(){
        List<City> list = new ArrayList<>();
        Cursor cursor = citybasedate.getReadableDatabase().query(citybasedate.city_name, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setId(cursor.getInt(0));
            city.setCityName(cursor.getString(1));
            city.setCityCode(cursor.getInt(2));
            city.setProvinceId(cursor.getInt(3));
            list.add(city);
        }
        return list;
    }

    public void citydelete(){
        int i = citybasedate.getWritableDatabase().delete(citybasedate.city_name, null, null);

    }

    public void countiesdelete(){
        int delete = citybasedate.getWritableDatabase().delete(citybasedate.county_name, null, null);
    }

    public List<County> countiesquery(){
        List<County> list = new ArrayList<>();
        Cursor cursor = citybasedate.getReadableDatabase().query(citybasedate.county_name, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            County county = new County();
            county.setId(cursor.getInt(0));
            county.setCountyName(cursor.getString(1));
            county.setWeatherId(cursor.getString(2));
            county.setCityId(cursor.getInt(3));
            list.add(county);
        }
        return list;
    }

    public void cityadd(City city){
        ContentValues values = new ContentValues();
        values.put("cityName",city.getCityName());
        values.put("cityCode",city.getCityCode());
        values.put("provinceId",city.getProvinceId());
        citybasedate.getWritableDatabase().insert(citybasedate.city_name,null,values);
    }

    public void countyadd(County county) {
        ContentValues values = new ContentValues();
        values.put("countyName",county.getCountyName());
        values.put("cityId",county.getCityId());
        values.put("weatherId",county.getWeatherId());
        citybasedate.getWritableDatabase().insert(citybasedate.county_name,null,values);
    }
}
