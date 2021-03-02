package com.sunnyweather.android.logic.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sunnyweather.android.SunnyWeatherApplication;
import com.sunnyweather.android.logic.model.Place;

public class PlaceDao {
    private static PlaceDao placeDao = null;
    public static PlaceDao getInstance() {
        if (placeDao == null) {
            placeDao = new PlaceDao();
        }
        return placeDao;
    }

    public void savePlace(Place place) {
        SharedPreferences.Editor editor= sharedPreferences().edit();
        editor.putString("place", new Gson().toJson(place));
        editor.commit();
    }

    public Place getSavedPlace() {
        String placeJson = sharedPreferences().getString("place", "");
        return new Gson().fromJson(placeJson, Place.class);
    }

    public boolean isPlaceSaved() {
        return sharedPreferences().contains("place");
    }

    private SharedPreferences sharedPreferences() {
        return SunnyWeatherApplication.getAppContext().getSharedPreferences("sunny_weather", Context.MODE_PRIVATE);
    }
}
