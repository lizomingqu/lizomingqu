package com.example.searchmytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        Bundle extras = getIntent().getExtras();
        String link = extras.getString("link");
        WebView viewById = findViewById(R.id.wv_view);
        viewById.loadUrl(link);
    }
}