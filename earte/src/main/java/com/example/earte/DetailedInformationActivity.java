package com.example.earte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailedInformationActivity extends AppCompatActivity {

    private ImageView iv_imgone;
    private TextView tv_title;
    private TextView tv_message;
    private TextView tv_data;
    private TextView tv_uri;
    private Button btn_tucx;
    private DrawerLayout drawerLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_information);
        initView();

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String picUrl = bundle.getString("picUrl");
        String description = bundle.getString("description");
        String source = bundle.getString("source");
        String ctime = bundle.getString("ctime");
        String url = bundle.getString("url");

        tv_title.setText(title);
        String urles = "https:"+ picUrl;

        Glide.with(this).load(urles).into(iv_imgone);

        tv_message.setText(description);
        tv_data.setText(ctime);
        tv_uri.setText("https:"+url);

    }


    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_imgone = findViewById(R.id.iv_imgone);
        tv_message = findViewById(R.id.tv_message);
        tv_data = findViewById(R.id.tv_data);
        tv_uri = findViewById(R.id.tv_uri);
        btn_tucx = findViewById(R.id.btn_tucx);
        btn_tucx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}