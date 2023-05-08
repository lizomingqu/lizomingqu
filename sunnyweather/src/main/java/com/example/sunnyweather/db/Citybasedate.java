package com.example.sunnyweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Citybasedate extends SQLiteOpenHelper {

    private static Citybasedate citybasedate;
    private static String db_name = "City_db";
    public static String city_name = "City_info";
    public static String county_name = "County_info";
    public static String province_name = "Province_info";
    private static int version = 1;

    public static Citybasedate getInstance(Context context) {
        if (citybasedate == null){
            citybasedate = new Citybasedate(context,db_name,null,version);
        }
        return citybasedate;
    }

    private Citybasedate(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+city_name+"(" +
                "id  integer PRIMARY KEY AUTOINCREMENT," +
                "cityName VARCHAR," +
                "cityCode VARCHAR," +
                "provinceId VARCHAR);";
        db.execSQL(sql);

        sql = "CREATE TABLE "+county_name+"(" +
                "id  integer PRIMARY KEY AUTOINCREMENT," +
                "countyName VARCHAR," +
                "weatherId VARCHAR," +
                "cityId VARCHAR);";

        db.execSQL(sql);

        sql = "CREATE TABLE "+province_name+"(" +
                "id  integer PRIMARY KEY AUTOINCREMENT," +
                "provinceName VARCHAR," +
                "provinceCode VARCHAR);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
