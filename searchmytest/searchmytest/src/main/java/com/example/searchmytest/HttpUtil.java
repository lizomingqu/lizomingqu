package com.example.searchmytest;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {


    public static void Httpget(String name, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(name).get().build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void HttpPost(String name,String uri,Callback callback){

        RequestBody formBody = new FormBody.Builder().add("k", name).build();

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(uri).post(formBody).build();
        okHttpClient.newCall(request).enqueue(callback);

    }
    private static String uri = "https://pixabay.com/api/?key=36285465-959ae0e011e9a9f8b0052da9a&image_type=photo";

    public static String HttpString(String name){
        HttpUrl.Builder builder = HttpUrl.parse(uri).newBuilder();
        builder.addQueryParameter("q", name);
        String url = builder.build().toString();
        return url;
    }

}
