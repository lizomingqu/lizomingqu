一个搜索工具

搜索想要查询的内容

RecyclerView的展示问题

```java
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
}
```

主界面的内容

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ModuleActivity">

    <EditText
        android:id="@+id/ed_test"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:background="@drawable/ed_test"
        android:layout_marginLeft="20dp"
        android:text="yrlo"
        android:layout_marginTop="20dp"
        android:layout_marginRight="20dp"
        android:paddingLeft="20dp"
        android:hint="请输入想获取的照片"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycle_view"
        android:layout_width="match_parent"
        android:layout_below="@+id/ed_test"
        android:layout_marginRight="20dp"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="10dp"
        android:layout_height="wrap_content"/>

    <Button
        android:id="@+id/btn_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignTop="@+id/ed_test"
        android:layout_alignEnd="@+id/ed_test"
        android:text="搜索" />

</RelativeLayout>
```



adapter适配器：

```java
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
```

适配器的页面:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="wrap_content"
    android:padding="10dp"
    android:id="@+id/ll_test"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/tv_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="一站式筛选"
        android:textColor="@color/black"
        android:textStyle="bold"
     />

    <LinearLayout
        android:orientation="horizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="来自："/>
        <TextView
            android:id="@+id/tv_superChapterName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="广场Tab"/>
    </LinearLayout>

    <LinearLayout
        android:orientation="horizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="发布事件："/>
        <TextView
            android:id="@+id/tv_niceDate"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="2023-04-24 13:22"/>
    </LinearLayout>

</LinearLayout>
```