package com.example.searchmytest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModuleActivity extends AppCompatActivity {

    private RecyclerView recycle_view;
    private EditText ed_test;
    private Button btn_test;
    private String url = "https://www.wanandroid.com/article/query/0/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        ed_test = findViewById(R.id.ed_test);
        btn_test = findViewById(R.id.btn_test);
        recycle_view = findViewById(R.id.recycle_view);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = ed_test.getText().toString();
                if (test.equals("")){
                    Toast.makeText(ModuleActivity.this, "请输入搜索词", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("ninger",url);
                HttpUtil.HttpPost(test, url, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String string = response.body().string();
                        Log.d("ninger", string);
                        List<Testers> list = JSONutil.JSONGetUtil(string, getApplication());
                        Log.d("ninger",list.toString());
                        if (list.size() == 0){
                            Toast.makeText(ModuleActivity.this, "未查到", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("ninger","jl");
                                MyAdapter adapter = new MyAdapter(getApplication(), list, new MyAdapter.onClitest() {

                                    @Override
                                    public void onCliter(View v, String test) {
                                        String link = test;
                                        Intent intent = new Intent(getApplicationContext(),Activity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("link",link);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                                recycle_view.setLayoutManager(new GridLayoutManager(getApplication(),2));
                                recycle_view.setAdapter(adapter);
                            }
                        });
                    }
                });
            }
        });

    }
    private ProgressDialog progressDialog;

}