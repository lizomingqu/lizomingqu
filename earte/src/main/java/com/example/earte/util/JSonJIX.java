package com.example.earte.util;

import android.content.Context;
import android.util.Log;

import com.example.earte.dao.SqlTer;
import com.example.earte.db.BookTESST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSonJIX {

    public List<BookTESST> JSONGET(String arras, Context context){
        try {
            JSONObject jsonObject = new JSONObject(arras);
            JSONObject result = jsonObject.getJSONObject("result");

            JSONArray newslist = result.getJSONArray("newslist");
            List<BookTESST> list = new ArrayList<>();
            for (int i = 0; i < newslist.length(); i++) {
                BookTESST bookTESST = new BookTESST();
                JSONObject jsonObject1 = newslist.getJSONObject(i);
                String id = jsonObject1.getString("id");
                Log.d("ninger",id+"");
                String ctime = jsonObject1.getString("ctime");
                String title = jsonObject1.getString("title");
                String description = jsonObject1.getString("description");
                String source = jsonObject1.getString("source");
                String picUrl = jsonObject1.getString("picUrl");
                String url = jsonObject1.getString("url");
                Log.d("ninger",ctime+"");
                Log.d("ninger",title+"");
                Log.d("ninger",description+"");
                Log.d("ninger",source+"");
                Log.d("ninger",picUrl+"");
                Log.d("ninger",url+"");

                bookTESST.ctime = ctime;
                bookTESST.title = title;
                bookTESST.description = description;
                bookTESST.source = source;
                bookTESST.picUrl = picUrl;
                bookTESST.url = url;

                list.add(bookTESST);
                Log.d("ninger",list.toString());

            }
            return list;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
