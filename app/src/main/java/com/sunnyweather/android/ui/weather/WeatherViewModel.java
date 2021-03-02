package com.sunnyweather.android.ui.weather;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.sunnyweather.android.logic.Repository;
import com.sunnyweather.android.logic.model.Location;
import com.sunnyweather.android.logic.model.Weather;

public class WeatherViewModel extends ViewModel {
    private MutableLiveData<Location> locationLiveData = new MutableLiveData<>();
    public String locationLng = "";
    public String locationLat = "";
    public String placeName = "";

    LiveData<Weather> weatherLiveData = Transformations.switchMap(locationLiveData, location ->
            Repository.getInstance().refreshWeather(location.getLng(), location.getLat()));

    void refreshWeather(String lng, String lat) {
        Log.d("Debug", "WeatherViewModel refreshWeather execute...");
        locationLiveData.setValue(new Location(lng, lat));
    }
}
