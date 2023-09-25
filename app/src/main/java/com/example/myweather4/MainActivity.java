package com.example.myweather4;


import static com.example.myweather4.tool.WeatherApplication.chosenPlace;
import static com.example.myweather4.tool.WeatherApplication.nowWeather;
import static com.example.myweather4.tool.WeatherApplication.showMore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myweather4.model.Place;
import com.example.myweather4.model.SearchCityResponse;
import com.example.myweather4.model2.RealtimeResponse;
import com.example.myweather4.network.PlaceSearchService;
import com.example.myweather4.network.PlaceServiceCreator;
import com.example.myweather4.network.RealtimeServiceCreator;
import com.example.myweather4.network.RealtimeWeatherService;
import com.example.myweather4.tool.WeatherApplication;

import java.util.Calendar;
import java.util.Date;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_view_toolbar);
        setSupportActionBar(mainToolbar);

        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        searchBar = (LinearLayout) findViewById(R.id.search_barII);
        searchEdit = (EditText) findViewById(R.id.search_place);
        confirmBtn.setOnClickListener(this);
        weatherInfoText = (TextView) findViewById(R.id.main_info_view);

        if (showMore) {
//            weatherInfoText.setText(WeatherApplication.chosenPlace.toString());
            mainToolbar.setTitle(chosenPlace.getName());
            mainToolbar.setSubtitle("时区-" + chosenPlace.getTz() +
                    " " + chosenPlace.getCountry() +
                    " " + chosenPlace.getCountry());
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

        }
        return true;
    }
}