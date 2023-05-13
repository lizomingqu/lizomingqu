package com.example.searchmytest;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class JSONutil {

    public static List<Testers> JSONGetUtil(String json, Context context){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray datas = data.getJSONArray("datas");
            List<Testers> list = new ArrayList<>();
            for (int i = 0; i < datas.length(); i++) {
                String link = datas.getJSONObject(i).getString("link");
                Log.d("ninger",link);
                String superChapterName = datas.getJSONObject(i).getString("superChapterName");
                String niceDate = datas.getJSONObject(i).getString("niceDate");
                String shareUser = datas.getJSONObject(i).getString("shareUser");
                String title = datas.getJSONObject(i).getString("title");
                Testers tester = new Testers();
                tester.link = link;
                tester.superChapterName = superChapterName;
                tester.niceDate = niceDate;
                tester.title = title;
                Log.d("ninger",tester.toString());
                list.add(tester);
                Log.d("ninger",list.toString());
            }
            return list;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
