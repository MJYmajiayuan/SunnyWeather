package com.sunnyweather.android.logic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sunnyweather.android.logic.model.DailyResponse;
import com.sunnyweather.android.logic.model.Place;
import com.sunnyweather.android.logic.model.PlaceResponse;
import com.sunnyweather.android.logic.model.RealtimeResponse;
import com.sunnyweather.android.logic.model.Weather;
import com.sunnyweather.android.logic.network.PlaceService;
import com.sunnyweather.android.logic.network.ServiceCreator;
import com.sunnyweather.android.logic.network.WeatherService;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private static Repository repository = null;
    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public LiveData<List<Place>> searchPlaces(String query) {
        MutableLiveData<List<Place>> result = new MutableLiveData<>();

        PlaceService placeService = ServiceCreator.getInstance().create(PlaceService.class);
        placeService.searchPlaces(query).enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                PlaceResponse body = response.body();
                if (body != null && "ok".equals(body.getStatus())) {
                    result.setValue(body.getPlaces());
                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                result.setValue(null);
            }
        });

        return result;
    }

    public LiveData<Weather> refreshWeather(String lng, String lat) {

        Log.d("Debug", "Repository refreshWeather start...");

        MutableLiveData<Weather> result = new MutableLiveData<>();                          // 返回值，泛型为Weather

        RealtimeResponse realtimeResponse = getNullRealtimeResponse();
        DailyResponse dailyResponse = getDailyResponse();
        Weather weather = new Weather(realtimeResponse.getResult().getRealtime(), dailyResponse.getResult().getDaily());

        CountDownLatch countDownLatch = new CountDownLatch(1);

        //通过ServiceCreator创建WeatherService
        WeatherService weatherService = ServiceCreator.getInstance().create(WeatherService.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<RealtimeResponse> response1 = weatherService.getRealtimeWeather(lng, lat).execute();
                    Response<DailyResponse> response2 = weatherService.getDailyResponse(lng, lat).execute();
                    if (response1.isSuccessful() && response2.isSuccessful()) {
                        RealtimeResponse body1 = response1.body();
                        DailyResponse body2 = response2.body();
                        if ("ok".equals(body1.getStatus()) && "ok".equals(body2.getStatus())) {
                            weather.setRealtime(body1.getResult().getRealtime());
                            DailyResponse.Result result1 = body2.getResult();
                            DailyResponse.Daily daily = result1.getDaily();
                            weather.setDaily(daily);
                        }
                    }
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Log.d("Debug", "Repository start await...");
            countDownLatch.await();
            Log.d("Debug", "Repository complete await...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result.setValue(weather);
        return result;
    }

    public RealtimeResponse getNullRealtimeResponse() {
        RealtimeResponse.AQI aqi = new RealtimeResponse.AQI(0);
        RealtimeResponse.AirQuality airQuality = new RealtimeResponse.AirQuality(aqi);
        RealtimeResponse.Realtime realtime = new RealtimeResponse.Realtime("NaN", 0, airQuality);
        RealtimeResponse.Result result = new RealtimeResponse.Result(realtime);
        RealtimeResponse realtimeResponse = new RealtimeResponse("ok", result);

        return realtimeResponse;
    }

    public DailyResponse getDailyResponse() {
        DailyResponse.LifeDescription lifeDescription = new DailyResponse.LifeDescription("NaN");
        List<DailyResponse.LifeDescription> lifeDescriptionList = new LinkedList<>();
        lifeDescriptionList.add(lifeDescription);
        DailyResponse.LifeIndex lifeIndex = new DailyResponse.LifeIndex(lifeDescriptionList, lifeDescriptionList, lifeDescriptionList, lifeDescriptionList);
        DailyResponse.Skycon skycon = new DailyResponse.Skycon("NaN", new Date(0));
        DailyResponse.Temperature temperature = new DailyResponse.Temperature(0, 0);

        List<DailyResponse.Skycon> skyconList = new LinkedList<>();
        skyconList.add(skycon);
        List<DailyResponse.Temperature> temperatureList = new LinkedList<>();
        temperatureList.add(temperature);

        DailyResponse.Daily daily = new DailyResponse.Daily(temperatureList, skyconList, lifeIndex);
        DailyResponse.Result result = new DailyResponse.Result(daily);
        DailyResponse dailyResponse = new DailyResponse("ok", result);

        return dailyResponse;
    }
}
