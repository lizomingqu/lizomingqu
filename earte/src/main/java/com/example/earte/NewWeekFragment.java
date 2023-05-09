package com.example.earte;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.earte.adapter.BookAdapter;
import com.example.earte.dao.SqlTer;
import com.example.earte.db.BookTESST;
import com.example.earte.util.HttpUtil;
import com.example.earte.util.JSonJIX;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewWeekFragment extends Fragment {

    private ListView lister;
    private HttpUtil httpUtil = new HttpUtil();
    private JSonJIX jSonJIX = new JSonJIX();
    private SqlTer sqlTer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_week, container, false);

        lister = view.findViewById(R.id.lister);

        return view;
    }

    private String url = "https://apis.tianapi.com/esports/index";
    private String key = "ff31d2b4587b3f02dfc42c8040d190a3";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            showHttp();
    }

    private void showDB() {
        List<BookTESST> query = sqlTer.query();
        if (query.size()!=0){
            BookAdapter adapter = new BookAdapter(getActivity(),query);
            lister.setAdapter(adapter);

            lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(),DetailedInformationActivity.class);
                    Bundle bundle = new Bundle();
                    BookTESST bookTESST = query.get(position);
                    bundle.putString("title",bookTESST.title);
                    bundle.putString("picUrl",bookTESST.picUrl);
                    bundle.putString("description", bookTESST.description);
                    bundle.putString("source", bookTESST.source);
                    bundle.putString("ctime", bookTESST.ctime);
                    bundle.putString("url", bookTESST.url);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            lister.setSelection(0);
        }else {
            showHttp();
        }
        sqlTer.delete();
    }

    private void showHttp() {
        httpUtil.SendHttpgetUtil(url,key, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String httpdj = response.body().string();
                List<BookTESST> jsonget = jSonJIX.JSONGET(httpdj, getActivity());
                sqlTer = SqlTer.getInstance(getActivity());
                sqlTer.addTester(jsonget);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDB();
                    }
                });
            }
        });

    }


}