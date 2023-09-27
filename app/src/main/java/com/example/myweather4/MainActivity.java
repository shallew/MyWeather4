package com.example.myweather4;


import static com.example.myweather4.tool.WeatherApplication.chosenPlace;
import static com.example.myweather4.tool.WeatherApplication.nowWeather;
import static com.example.myweather4.tool.WeatherApplication.showMore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.myweather4.model.Place;
import com.example.myweather4.model.SearchCityResponse;
import com.example.myweather4.model2.RealtimeResponse;
import com.example.myweather4.model3.DailyWeatherResponse;
import com.example.myweather4.model4.HourlyWeatherResponse;
import com.example.myweather4.network.PlaceSearchService;
import com.example.myweather4.network.PlaceServiceCreator;
import com.example.myweather4.network.RealtimeServiceCreator;
import com.example.myweather4.network.RealtimeWeatherService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button confirmBtn;
    private EditText searchEdit;
    private LinearLayout searchBar;
    private TextView weatherInfoText;
    private LinearLayout mainBackground;
    public LocationClient lc;
    private String autoLocationInfo;//经度和纬度

    public void GPSLocation() {
        LocationClient.setAgreePrivacy(true);
        try {
            lc = new LocationClient(getApplicationContext());
            lc.registerLocationListener(new MyLocationListener());
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray(new String[0]);
                ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
            }
        } catch (Exception e) {
            Log.d("err", e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int result :
                    grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "未同意权限申请", Toast.LENGTH_SHORT).show();
                }
                //如果都同意了
            }
        } else {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(bdLocation.getLongitude()).append(",").append(bdLocation.getLatitude());
                    Message message = new Message();
                    message.obj = sb;
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lc != null) {
            lc.stop();
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                Log.d(":", "handleMessage: " + message.obj.toString());
                autoLocationInfo = message.obj.toString();
            }
            return false;
        }
    });

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GPSLocation();


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_view_toolbar);
        setSupportActionBar(mainToolbar);

        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        searchBar = (LinearLayout) findViewById(R.id.search_barII);
        searchEdit = (EditText) findViewById(R.id.search_place);
        confirmBtn.setOnClickListener(this);
        weatherInfoText = (TextView) findViewById(R.id.main_info_view);


        if (showMore) {
            LinearLayout toolbarLayout = mainToolbar.findViewById(R.id.toolbar_layout);
            toolbarLayout.setVisibility(View.VISIBLE);
            TextView toolbarTitle = (TextView) mainToolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(chosenPlace.getName());
            TextView toolbarSubtitle = (TextView) mainToolbar.findViewById(R.id.toolbar_subtitle);
            toolbarSubtitle.setText(chosenPlace.getCountry() +
                    "-" + chosenPlace.getAdm1() + "-" + chosenPlace.getAdm2() +
                    " " + chosenPlace.getTz());

//            setRealTimeView();
            //设置实况天气
//            setDailyView();
            //设置未来七天的天气
            setHourlyView();
        }

    }
    public void setHourlyView() {
        RealtimeWeatherService realtimeWeatherService = (RealtimeWeatherService) RealtimeServiceCreator.create(RealtimeWeatherService.class);
        realtimeWeatherService.getHourlyWeatherInfo(chosenPlace.getId()).enqueue(new Callback<HourlyWeatherResponse>() {
            @Override
            public void onResponse(Call<HourlyWeatherResponse> call, Response<HourlyWeatherResponse> response) {
                HourlyWeatherResponse body = response.body();
                if (body != null) {
                    if (body.getCode().equals("200")) {
                        weatherInfoText.setText(body.toString());
                    }
                }
            }
            @Override
            public void onFailure(Call<HourlyWeatherResponse> call, Throwable t) {
                Log.d("hourly", t.toString());
            }
        });
    }
    public void setDailyView() {
        RealtimeWeatherService realtimeWeatherService = (RealtimeWeatherService) RealtimeServiceCreator.create(RealtimeWeatherService.class);
        realtimeWeatherService.getDailyWeatherInfo(chosenPlace.getId()).enqueue(new Callback<DailyWeatherResponse>() {
            @Override
            public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                DailyWeatherResponse body = response.body();
                if (body != null) {
                    Log.d("daily", "body != null code = " + body.getCode());
                    if (body.getCode().equals("200")) {
                        weatherInfoText.setText(body.toString());
                    }
                } else {
                    Log.d("daily", "body == null ");
                }
            }

            @Override
            public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {
                Log.d("daily", t.toString());
            }
        });
    }

    //该方法设置实况天气
    public void setRealTimeView() {
        RealtimeWeatherService realtimeWeatherService = (RealtimeWeatherService) RealtimeServiceCreator.create(RealtimeWeatherService.class);
        realtimeWeatherService.getRealtimeWeatherInfo(chosenPlace.getId()).enqueue(new Callback<RealtimeResponse>() {
            @Override
            public void onResponse(Call<RealtimeResponse> call, Response<RealtimeResponse> response) {
                RealtimeResponse body = response.body();
                if (body != null) {
                    Log.d("info", "body != null");
                    if (body.getCode().equals("200")) {
                        nowWeather = body.getNow();
                        weatherInfoText.setText(body.toString());
                    }
                }
            }
            @Override
            public void onFailure(Call<RealtimeResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confirm_btn) {
            String s = searchEdit.getText().toString();
            searchEdit.setText("");//清空搜索框
            //搜索城市
            PlaceSearchService placeSearchService = (PlaceSearchService) PlaceServiceCreator.create(PlaceSearchService.class);
            placeSearchService.searchCity(s).enqueue(new Callback<SearchCityResponse>() {
                @Override
                public void onResponse(Call<SearchCityResponse> call, Response<SearchCityResponse> response) {
                    SearchCityResponse body = response.body();
                    if (body != null) {
                        Log.d("TAG", "body != null code = " + body.getCode());
                        if (body.getCode().equals("200")) {
//                            Log.d("TAG", body.getLocation().toString());
                            List<Place> places = body.getLocation();
                            for (Place p :
                                    places) {
                                Log.d("TAG", p.toString());
                            }
                            String[] strings = places.stream().map(new Function<Place, Object>() {
                                @Override
                                public Object apply(Place place) {
                                    return place.toString();
                                }
                            }).toArray(String[]::new);
                            final int[] tem = new int[1];
                            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                            ab.setTitle("选择地区：").setSingleChoiceItems(strings, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    tem[0] = i;//记录选中的下标
                                }
                            }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    chosenPlace = places.get(tem[0]);
                                    showMore = true;
                                    searchBar.setVisibility(View.GONE);
                                    recreate();//重新加载该活动的实例
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
                        }

                    } else {
                        Log.d("TAG", "body == null");
                    }
                }

                @Override
                public void onFailure(Call<SearchCityResponse> call, Throwable t) {
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            searchBar.setVisibility(View.VISIBLE);//搜索栏可见

        } else if (item.getItemId() == R.id.fab) {

            lc.start();
            PlaceSearchService placeSearchService = (PlaceSearchService) PlaceServiceCreator.create(PlaceSearchService.class);
            placeSearchService.searchCity(autoLocationInfo).enqueue(new Callback<SearchCityResponse>() {
                @Override
                public void onResponse(Call<SearchCityResponse> call, Response<SearchCityResponse> response) {
                    SearchCityResponse body = response.body();
                    if (body != null) {
                        Log.d("l", "body != null code" + body.getCode());
                        if (body.getCode().equals("200")) {
                            Log.d("l", body.toString());
                            chosenPlace = body.getLocation().get(0);//经纬度定位的地方只有一个
                            showMore = true;
                            recreate();//重新加载该活动的实例
                        }
                    } else {
                        Log.d("l", "body == null");
                    }
                }

                @Override
                public void onFailure(Call<SearchCityResponse> call, Throwable t) {
                }
            });
        }
        return true;
    }
}