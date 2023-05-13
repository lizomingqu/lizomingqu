package com.example.searchmytest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Tester> {

    private final Context context;
    private final List<Testers> list;
    private onClitest onClitest;

    public MyAdapter(Context context, List<Testers> list,onClitest onClitest) {
        this.context = context;
        this.list = list;
        this.onClitest = onClitest;
    }

    @NonNull
    @Override
    public MyAdapter.Tester onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Tester(View.inflate(context,R.layout.item_tester,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.Tester holder, int position) {
        Testers testers = list.get(position);

        holder.tv_title.setText(testers.title);
        holder.tv_superChapterName.setText(testers.superChapterName);
        holder.tv_niceDate.setText(testers.niceDate);
        holder.ll_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClitest.onCliter(v,testers.link);
            }
        });
    }

    public interface onClitest{
        void onCliter(View v,String test);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Tester extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_superChapterName;
        TextView tv_niceDate;
        LinearLayout ll_test;
        public Tester(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_superChapterName = itemView.findViewById(R.id.tv_superChapterName);
            tv_niceDate = itemView.findViewById(R.id.tv_niceDate);
            ll_test = itemView.findViewById(R.id.ll_test);
        }
    }
}
