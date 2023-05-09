



```xml
<uses-permission android:name="android.permission.INTERNET" />
<application
             android:usesCleartextTraffic="true">
    
</application>
```

上面是获取网络的配置，没有这个就没法访问网络

首页面的代码就是一个Fragment

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SunnerActivity">

    <fragment
        android:id="@+id/choose_area_fragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:name="com.example.sunnyweather.ChooseAreaFragment"/>

</FrameLayout>
```

```xml
android:name="com.example.sunnyweather.ChooseAreaFragment"
```

上面的这个name用来指定这个fragment用来展示那个fragment的类



展示图：

![](C:\Users\ASUS\Desktop\作业\罗知强\sunny\首页.png)





来到这个ChooseAreaFragment的界面制作：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#fff">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary">

        <TextView
            android:id="@+id/title_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:textColor="#fff"
            android:textSize="20sp"/>

        <Button
            android:id="@+id/back_button"
            android:layout_width="25dp"
            android:layout_height="25dp"
            android:layout_marginLeft="10dp"
            android:layout_centerVertical="true"
            android:layout_alignParentLeft="true"
            android:background="@drawable/arrow_back_24"/>

    </RelativeLayout>

    <ListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</LinearLayout>
```



![](C:\Users\ASUS\Desktop\作业\罗知强\sunny\ChooseAreaFragment的界面.png)



因为是fragmang所以获取一下界面以及控件

```java
@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.choose_area, container, false);
    titleText = (TextView) view.findViewById(R.id.title_text);
    backButton = (Button) view.findViewById(R.id.back_button);
    listView = (ListView) view.findViewById(R.id.list_view);
    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
    listView.setAdapter(adapter);
    return view;
}
```

然后到onActivityCreated里面做事件操作，因为要做县市省需要知道现在是哪个界面，以及你点击了哪个城市进去的

```java
private static final int LEVEL_PROVINCE = 0;//区分界面，分别处理不同的·界面
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
```

```java
selectedProvince = provinceList.get(position);//这个类来接收
```

![](C:\Users\ASUS\Desktop\作业\罗知强\sunny\事件.png)



上面通过各自的方法共3个

```java
private void queryCities() {
    titleText.setText(selectedProvince.getProvinceName());
    backButton.setVisibility(View.VISIBLE);//显示按钮
    cityDao = CityDao.getInstance(getActivity());//获取类
    cityList = cityDao.cityquery();//查询数据库的内容
    if (cityList.size()>0){
        dataList.clear();//清理之前的数据
        cityDao.citydelete();//删除之前的数据以免下次还自动打开相同的城市
        for (int i = 0; i < cityList.size(); i++) {
            City city = cityList.get(i);
            dataList.add(city.getCityName());
        }
        adapter.notifyDataSetChanged();//刷新界面
        listView.setSelection(0);
        currentLevel = LEVEL_CITY;//区别现在在哪

    }else {
        int provinceCode = selectedProvince.getProvinceCode();
        String address = "http://guolin.tech/api/china"+"/"+provinceCode;//这里的就是你选择哪个的时候存储的
        Log.d("ninger",address);//打印
        queryFromServer(address,"city");
    }
}
private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        cityDao = CityDao.getInstance(getActivity());
        CountyList = cityDao.countiesquery();
        if (CountyList.size()>0){
            dataList.clear();
            cityDao.countiesdelete();
            for (int i = 0; i < CountyList.size(); i++) {
                County county = CountyList.get(i);
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            Log.d("ninger", address);
            queryFromServer(address,"county");
        }
    }
private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        cityDao = CityDao.getInstance(getActivity());
         provinceList = cityDao.provincequery();
        if (provinceList.size()>0){
            dataList.clear();
            for (int i = 0; i < provinceList.size(); i++) {
                Province province = provinceList.get(i);
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }
```

cityquery是我创建的数据库，为的是将数据保存·在本地

```java
public class Citybasedate extends SQLiteOpenHelper {

    private static Citybasedate citybasedate;
    private static String db_name = "City_db";
    public static String city_name = "City_info";
    public static String county_name = "County_info";
    public static String province_name = "Province_info";
    private static int version = 1;

    public static Citybasedate getInstance(Context context) {
        if (citybasedate == null){
            citybasedate = new Citybasedate(context,db_name,null,version);
        }
        return citybasedate;
    }

    private Citybasedate(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+city_name+"(" +
                "id  integer PRIMARY KEY AUTOINCREMENT," +
                "cityName VARCHAR," +
                "cityCode VARCHAR," +
                "provinceId VARCHAR);";
        db.execSQL(sql);

        sql = "CREATE TABLE "+county_name+"(" +
                "id  integer PRIMARY KEY AUTOINCREMENT," +
                "countyName VARCHAR," +
                "weatherId VARCHAR," +
                "cityId VARCHAR);";

        db.execSQL(sql);

        sql = "CREATE TABLE "+province_name+"(" +
                "id  integer PRIMARY KEY AUTOINCREMENT," +
                "provinceName VARCHAR," +
                "provinceCode VARCHAR);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
```

在dao下创建一个类来快速的操作数据库

```java
public class CityDao {

    private static CityDao cityDao;
    private Citybasedate citybasedate;

    private CityDao(Context context) {
        if (citybasedate==null){
            citybasedate = Citybasedate.getInstance(context);
        }
    }

    public static CityDao getInstance(Context context){
        if (cityDao == null){
            cityDao = new CityDao(context);
        }
        return cityDao;
    }

    public void provinceadd(Province province){
        ContentValues values = new ContentValues();
        values.put("provinceName",province.getProvinceName());
        values.put("provinceCode",province.getProvinceCode());
        citybasedate.getWritableDatabase().insert(citybasedate.province_name,null,values);
    }

    public List<Province> provincequery(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = citybasedate.getReadableDatabase().query(citybasedate.province_name, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Province province = new Province();
            province.setId(cursor.getInt(0));
            province.setProvinceName(cursor.getString(1));
            province.setProvinceCode(cursor.getInt(2));
            list.add(province);
        }
        return list;
    }

    public List<City> cityquery(){
        List<City> list = new ArrayList<>();
        Cursor cursor = citybasedate.getReadableDatabase().query(citybasedate.city_name, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setId(cursor.getInt(0));
            city.setCityName(cursor.getString(1));
            city.setCityCode(cursor.getInt(2));
            city.setProvinceId(cursor.getInt(3));
            list.add(city);
        }
        return list;
    }

    public void citydelete(){
        int i = citybasedate.getWritableDatabase().delete(citybasedate.city_name, null, null);

    }

    public void countiesdelete(){
        int delete = citybasedate.getWritableDatabase().delete(citybasedate.county_name, null, null);
    }

    public List<County> countiesquery(){
        List<County> list = new ArrayList<>();
        Cursor cursor = citybasedate.getReadableDatabase().query(citybasedate.county_name, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            County county = new County();
            county.setId(cursor.getInt(0));
            county.setCountyName(cursor.getString(1));
            county.setWeatherId(cursor.getString(2));
            county.setCityId(cursor.getInt(3));
            list.add(county);
        }
        return list;
    }

    public void cityadd(City city){
        ContentValues values = new ContentValues();
        values.put("cityName",city.getCityName());
        values.put("cityCode",city.getCityCode());
        values.put("provinceId",city.getProvinceId());
        citybasedate.getWritableDatabase().insert(citybasedate.city_name,null,values);
    }

    public void countyadd(County county) {
        ContentValues values = new ContentValues();
        values.put("countyName",county.getCountyName());
        values.put("cityId",county.getCityId());
        values.put("weatherId",county.getWeatherId());
        citybasedate.getWritableDatabase().insert(citybasedate.county_name,null,values);
    }
}
```

以上步骤都完成了，queryProvinces，queryCounties，queryCities这三个方法需要一个公共的去访问网络的方法



同时判断是哪个方法调用的来区别一下同时将下面的加载内容的方法调用一下，并创建Http的类来访问网络

```java
private void queryFromServer( String address, String type) {
    showProgressDialog();
    HttpUtil.sendOkHttpRequest(address, new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    closeProgressDialog();
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            String responseText = response.body().string();
            boolean result = false;
            if ("province".equals(type)){
                result = Utility.handleProvinceResponse(responseText,getActivity());
            }else if ("city".equals(type)){
                result = Utility.handleCityResponse(responseText,selectedProvince.getProvinceCode(),getActivity());
            } else if ("county".equals(type)) {
                result = Utility.handleCountyResponse(responseText,selectedCity.getId(),getActivity());
            }
            if (result){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if ("province".equals(type)){
                            queryProvinces();
                        } else if ("city".equals(type)) {
                            queryCities();
                        } else if ("county".equals(type)) {
                            queryCounties();
                        }
                    }
                });
            }
        }
    });
}
```



调用网络会有耗时操作让用户有个正在加载的感觉

```java
/**
 * 显示进度条
 */
private void showProgressDialog() {
    if (progressDialog == null){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载...");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    progressDialog.show();
}

/**
 * 关闭进度条
 */
private void closeProgressDialog(){
    if (progressDialog!=null){
        progressDialog.dismiss();
    }
}
```







HttpUtil这个类是用来访问网络的，通过callback接口来返回

```java
public class HttpUtil {

    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).get().build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendOkHttppostRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(RequestBody.create(null, "")).build();
        client.newCall(request).enqueue(callback);
    }
}
```

既然有访问了，那得将东西获取出来

Utility这个类用来解析JSON文件的，这里我没有用到插件，根据JSON返回的数来进行修改

```java
public class Utility {


    //省
    public static boolean handleProvinceResponse(String response, Context context){
        if (!TextUtils.isEmpty(response)){

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    CityDao cityDao = CityDao.getInstance(context);
                    Province province = new Province();
                    province.setProvinceName(name);
                    province.setProvinceCode(id);
                    cityDao.provinceadd(province);
                }
                return true;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    //市
    public static boolean handleCityResponse(String response,int provinceId,Context context){
        if (!TextUtils.isEmpty(response)){
            JSONArray jsonArray = null;
            try {
                CityDao cityDao = CityDao.getInstance(context);
                City city = new City();
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    int id = jsonArray.getJSONObject(i).getInt("id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    city.setCityName(name);
                    city.setCityCode(id);
                    city.setProvinceId(provinceId);
                    cityDao.cityadd(city);
                }
                return true;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    //县
    public static boolean handleCountyResponse(String response,int cityId,Context context){
        if (!TextUtils.isEmpty(response)){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String id = jsonArray.getJSONObject(i).getString("weather_id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    CityDao cityDao = CityDao.getInstance(context);
                    County county = new County();
                    county.setCountyName(name);
                    county.setWeatherId(id);
                    county.setCityId(cityId);
                    cityDao.countyadd(county);

                    /*SharedPreferences weather = context.getSharedPreferences("weather", 0);
                    SharedPreferences.Editor edit = weather.edit();
                    SharedPreferences.Editor weather1 = edit.putString("weather_id", id);
                    edit.commit();*/

                }
                return true;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response,Context context){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather");
            for (int i = 0; i < heWeather.length(); i++) {


                Basic basicone = new Basic();
                Suggestion suggest = new Suggestion();
                Aqi aqitest = new Aqi();
                Now nowone = new Now();
                Weather weather = new Weather();


                JSONObject basic = heWeather.getJSONObject(i).getJSONObject("basic");
                String city = basic.getString("city");
                String id = basic.getString("id");
                JSONObject update = basic.getJSONObject("update");
                String data = update.getString("loc");

                Log.d("ninger",city+"/"+id+"/"+data);

                basicone.cityName = city;
                basicone.weatherId = id;
                Basic.Update update1 = new Basic.Update();
                update1.updateTime = data;
                basicone.update = update1;
                Log.d("ninger",city+"/"+id+"/"+data);

                JSONObject now = heWeather.getJSONObject(i).getJSONObject("now");
                String wind_dir = now.getString("wind_dir");
                JSONObject cond = now.getJSONObject("cond");
                String txt = cond.getString("txt");
                nowone.temperature =wind_dir;
                Log.d("ninger",wind_dir+"/"+txt);
                Now.More more = new Now.More();
                more.info = txt;
                nowone.more = more;
                Log.d("ninger",wind_dir+"/"+txt);
                List<Forecast> forecastList = new ArrayList<>();
                JSONArray forecast = heWeather.getJSONObject(i).getJSONArray("daily_forecast");
                Log.d("ninger",forecast.length()+"");
                for (int j = 0; j < forecast.length(); j++) {
                    JSONObject jsonObject1 = forecast.getJSONObject(j);
                    Log.d("ninger",jsonObject1.toString()+"");
                    String date = jsonObject1.getString("date");
                    Log.d("ninger",date);
                    String txt_d = jsonObject1.getJSONObject("cond").getString("txt_d");
                    Log.d("ninger",txt_d);
                    JSONObject tmp = jsonObject1.getJSONObject("tmp");
                    String max = tmp.getString("max");
                    Log.d("ninger",max);
                    String min = tmp.getString("min");
                    Log.d("ninger",min);
                    Forecast forecastone = new Forecast();
                    forecastone.date = date;
                    Forecast.Temperature temperature = new Forecast.Temperature();
                    temperature.max = max;
                    temperature.min = min;
                    forecastone.temperature = temperature;

                    Forecast.More more1 = new Forecast.More();
                    more1.info = txt_d;
                    forecastone.more=more1;

                    forecastList.add(forecastone);
                    Log.d("ninger",date+"/"+max+"/"+min+"/"+txt_d);
                    Log.d("ninger",forecastList.toString());
                }

                weather.forecastList=forecastList;
                JSONObject aqi = heWeather.getJSONObject(i).getJSONObject("aqi");
                JSONObject city_api = aqi.getJSONObject("city");
                String aqi_zs = city_api.getString("aqi");
                String pm25 = city_api.getString("pm25");

                Aqi.AQICity aqiCity = new Aqi.AQICity();
                aqiCity.api = aqi_zs;
                aqiCity.pm25 = pm25;
                aqitest.aqiCity = aqiCity;
                Log.d("ninger",aqi_zs+"/"+pm25);

                JSONObject suggestion = heWeather.getJSONObject(i).getJSONObject("suggestion");
                String comf_txt = suggestion.getJSONObject("comf").getString("txt");
                String sport_txt = suggestion.getJSONObject("sport").getString("txt");
                String cw_txt = suggestion.getJSONObject("cw").getString("txt");

                Suggestion.Comfort comfort = new Suggestion.Comfort();
                Suggestion.Sport sport = new Suggestion.Sport();
                Suggestion.CarWash carWash = new Suggestion.CarWash();

                comfort.info = comf_txt;
                sport.info = sport_txt;
                carWash.info = cw_txt;

                suggest.comfort=comfort;
                suggest.sport = sport;
                suggest.carWash = carWash;
                Log.d("ninger",comf_txt+"/"+sport_txt+"/"+cw_txt);

                String status = heWeather.getJSONObject(i).getString("status");

                weather.status =  status;
                weather.aqi = aqitest;
                weather.basic = basicone;
                weather.now = nowone;
                weather.suggestion = suggest;

                return weather;
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static String handleWeatherimgResponse(String response,Context context){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray urls = data.getJSONArray("urls");
            for (int i = 0; i < urls.length(); i++) {
                String img = urls.getString(0);
                return img;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
```

解析的内容：

![](C:\Users\ASUS\Desktop\作业\罗知强\sunny\解析的内容.png)





上面的县市省都做好了就跳转到最后的城市的展示页面

```java
public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView titleUpdateTime;
    private TextView wetherInfoText;
    private LinearLayout forecastLayout;
    private TextView apiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private String url = "https://api.xygeng.cn/bing/";
    public SwipeRefreshLayout swipe_refresh;
    public DrawerLayout drawerLayout;
    private Button nav_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        drawerLayout = findViewById(R.id.drawer_layout);
        nav_button = findViewById(R.id.nav_button);

        swipe_refresh = findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeResources(R.color.colorPrimary);

        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        degreeText = findViewById(R.id.degree_text);
        titleUpdateTime = findViewById(R.id.title_update_time);
        wetherInfoText = findViewById(R.id.wether_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        apiText = findViewById(R.id.api_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);

        SharedPreferences weather = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = weather.edit();
        String weatherString = weather.getString("weather", null);
        String weatherId;
        if (weatherString!=null){
            Weather weather1 = Utility.handleWeatherResponse(weatherString, getApplication());
            weatherId = weather1.basic.weatherId;
            showWeatherInfo(weather1);
            /*SharedPreferences.Editor weather2 = edit.putString("weather", null);
            weather2.commit();*/
        }else {
            weatherId = getIntent().getStringExtra("weather_id");
            Log.d("ninger",weatherId);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
            /*SharedPreferences.Editor weather2 = edit.putString("weather", null);
            weather2.commit();*/
        }

        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });

       /* String bing_pic = weather.getString("bing_pic", null);
        if (bing_pic!=null){
            String img = Utility.handleWeatherimgResponse(bing_pic, getApplication());
            Log.d("ninger",img);
            initers(img);
        }else {
            loadBingPic();
        }*/
    }
    //获取完整的城市的JSON
    public void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=QMGUg1vq2FK7wyBi";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipe_refresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText, getApplication());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            edit.putString("weather",responseText);
                            edit.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipe_refresh.setRefreshing(false);
                    }
                });
            }
        });

    }
    //将界面展示上去
    private void showWeatherInfo(Weather weather) {
        if (weather!=null&&"ok".equals(weather.status)){

            String cityName = weather.basic.cityName;
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
            String degree = weather.now.temperature;
            String weatherInfo = weather.now.more.info;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            wetherInfoText.setText(weatherInfo);
            forecastLayout.removeAllViews();
            for (Forecast forecast : weather.forecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                TextView datetext = view.findViewById(R.id.datetext);
                TextView info_text = view.findViewById(R.id.info_text);
                TextView max_text = view.findViewById(R.id.max_text);
                TextView min_text = view.findViewById(R.id.min_text);

                datetext.setText(forecast.date);
                info_text.setText(forecast.more.info);
                max_text.setText(forecast.temperature.max);
                min_text.setText(forecast.temperature.min);

                Log.d("ninger",forecast.toString());

                forecastLayout.addView(view);
            }

            String api = weather.aqi.aqiCity.api;
            String pm25 = weather.aqi.aqiCity.pm25;

            apiText.setText(api);
            pm25Text.setText(pm25);

            String comfort = "舒适度："+weather.suggestion.comfort.info;
            String carWash = "洗车指数："+ weather.suggestion.carWash.info;
            String sport = "运动指数："+weather.suggestion.sport.info;

            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);
            weatherLayout.setVisibility(View.VISIBLE);

            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);

        }else {
            Toast.makeText(this, "获取天气·失败", Toast.LENGTH_SHORT).show();
        }
    }
}
```



![](C:\Users\ASUS\Desktop\作业\罗知强\5.8\最终输出.jpg)



创建一个服务用来刷新天气

```java
public class AutoUpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updataBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pending = PendingIntent.getService(this,0,i,0);
        manager.cancel(pending);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pending);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updataBingPic() {

    }

    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weather = preferences.getString("weather", null);
        if (weather!=null){

            Weather weatherone = Utility.handleWeatherResponse(weather, getApplication());
            String weatherId = weatherone.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=QMGUg1vq2FK7wyBi";

            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText, getApplication());
                    if (weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplication()).edit();
                        edit.putString("weather",responseText);
                        edit.apply();
                    }
                }
            });
        }
    }
}
```





