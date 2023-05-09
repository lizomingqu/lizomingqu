package com.example.earte.util;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    public void SendHttpgetUtil(String url,String key, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("key", key);
        urlBuilder.addQueryParameter("num", "10");
        String requestUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(requestUrl).get().build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
