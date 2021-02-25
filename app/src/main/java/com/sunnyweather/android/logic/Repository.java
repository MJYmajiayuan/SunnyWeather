package com.sunnyweather.android.logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sunnyweather.android.logic.model.Place;
import com.sunnyweather.android.logic.model.PlaceResponse;
import com.sunnyweather.android.logic.network.PlaceService;
import com.sunnyweather.android.logic.network.ServiceCreator;

import java.util.List;

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
}
