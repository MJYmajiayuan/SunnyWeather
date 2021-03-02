package com.sunnyweather.android.ui.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunnyweather.android.R;
import com.sunnyweather.android.logic.model.DailyResponse;
import com.sunnyweather.android.logic.model.RealtimeResponse;
import com.sunnyweather.android.logic.model.Sky;
import com.sunnyweather.android.logic.model.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    public WeatherViewModel viewModel;
    private TextView placeName;
    private TextView currentTemp;
    private TextView currentSky;
    private TextView currentAQI;
    private RelativeLayout nowLayout;
    private LinearLayout forecastLayout;
    private TextView coldRiskText;
    private TextView dressingText;
    private TextView ultravioletText;
    private TextView carWashingText;
    private ScrollView weatherLayout;
    private SwipeRefreshLayout swipeRefresh;
    private Button navBtn;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置沉浸式状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_weather);

        // 初始化控件
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        navBtn = (Button) findViewById(R.id.navBtn);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        Log.d("Debug", "WeatherActivity setContentView completed...");

        /**
         * 从intent中获取数据
         */
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        if (viewModel.locationLng.isEmpty()) {
            String lng = getIntent().getStringExtra("location_lng");
            viewModel.locationLng = (lng == null ? "" : lng);
        }
        if (viewModel.locationLat.isEmpty()) {
            String lat = getIntent().getStringExtra("location_lat");
            viewModel.locationLat = (lat == null ? "" : lat);
        }
        if (viewModel.placeName.isEmpty()) {
            String placeName = getIntent().getStringExtra("place_name");
            viewModel.placeName = (placeName == null ? "" : placeName);
        }

        Log.d("Debug", "ViewModel completed");

        /**
         * 对liveData设置观察者
         */
        viewModel.weatherLiveData.observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                Log.d("Debug", "weatherLiveData changed...");
                if (weather != null) {
                    showWeatherInfo(weather);
                } else {
                    Toast.makeText(WeatherActivity.this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show();
                }
                swipeRefresh.setRefreshing(false);
            }
        });

        /**
         * 下拉菜单的逻辑处理
         */
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary);
        refreshWeather();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWeather();
            }
        });
        Log.d("Debug", "Observe completed");
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat);

        /**
         * 滑动菜单逻辑处理
         */
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void showWeatherInfo(Weather weather) {

        Log.d("Debug", "showWeatherInfo start");

        placeName = (TextView) findViewById(R.id.placeName);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        currentSky = (TextView) findViewById(R.id.currentSky);
        currentAQI = (TextView) findViewById(R.id.currentAQI);
        nowLayout = (RelativeLayout) findViewById(R.id.nowLayout);
        forecastLayout = (LinearLayout) findViewById(R.id.forecastLayout);
        coldRiskText = (TextView) findViewById(R.id.coldRiskText);
        dressingText = (TextView) findViewById(R.id.dressingText);
        ultravioletText = (TextView) findViewById(R.id.ultravioletText);
        carWashingText = (TextView) findViewById(R.id.carWashingText);
        weatherLayout = (ScrollView) findViewById(R.id.weatherLayout);

        // 设置城市
        placeName.setText(viewModel.placeName);
        RealtimeResponse.Realtime realtime = weather.getRealtime();
        DailyResponse.Daily daily = weather.getDaily();
        // 填充now.xml布局中的数据
        String currentTempText = (int) realtime.getTemperature() + "℃";
        currentTemp.setText(currentTempText);
        currentSky.setText(Sky.getSky(realtime.getSkycon()).getInfo());
        String currentPM25Text = "空气指数" + realtime.getAirQuality().getAqi().getChn();
        currentAQI.setText(currentPM25Text);
        //设置背景图
        nowLayout.setBackgroundResource(Sky.getSky(realtime.getSkycon()).getBg());

        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews();
        int days = daily.getSkycon().size();
        for (int i = 0; i < days; i++) {
            DailyResponse.Skycon skycon = daily.getSkycon().get(i);
            DailyResponse.Temperature temperature = daily.getTemperature().get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);

            TextView dateInfo = (TextView) view.findViewById(R.id.dateInfo);
            ImageView skyIcon = (ImageView) view.findViewById(R.id.skyIcon);
            TextView skyInfo = (TextView) view.findViewById(R.id.skyInfo);
            TextView temperatureInfo = (TextView) view.findViewById(R.id.temperatureInfo);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());    // 设置日期格式
            dateInfo.setText(simpleDateFormat.format(skycon.getDate()));    // 设置日期
            Sky sky = Sky.getSky(skycon.getValue());
            skyIcon.setImageResource(sky.getIcon());    // 设置天气图标
            skyInfo.setText(sky.getInfo());             // 设置天气
            String tempText = (int)temperature.getMin() + "~" + (int)temperature.getMax() + "℃";
            temperatureInfo.setText(tempText);
            forecastLayout.addView(view);
        }

        // 填充life_index.xml布局中的数据
        DailyResponse.LifeIndex lifeIndex = daily.getLifeIndex();
        coldRiskText.setText(lifeIndex.getColdRisk().get(0).getDesc());
        dressingText.setText(lifeIndex.getDressing().get(0).getDesc());
        ultravioletText.setText(lifeIndex.getUltraviolet().get(0).getDesc());
        carWashingText.setText(lifeIndex.getCarWashing().get(0).getDesc());
        weatherLayout.setVisibility(View.VISIBLE);

        Log.d("Debug", "showWeatherInfo completed");
    }

    public void refreshWeather() {
        swipeRefresh.setRefreshing(true);
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat);
    }
}