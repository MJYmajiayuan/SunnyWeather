package com.sunnyweather.android.logic.network;

import com.sunnyweather.android.logic.model.DailyResponse;
import com.sunnyweather.android.logic.model.RealtimeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherService {

    @GET("v2.5/C2oIE62rKVVdmJkB/{lng},{lat}/realtime.json")
    Call<RealtimeResponse> getRealtimeWeather(@Path("lng") String lng, @Path("lat") String lat);

    @GET("v2.5/C2oIE62rKVVdmJkB/{lng},{lat}/daily.json")
    Call<DailyResponse> getDailyResponse(@Path("lng") String lng, @Path("lat") String lat);

}
