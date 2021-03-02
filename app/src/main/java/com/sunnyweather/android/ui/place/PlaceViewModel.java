package com.sunnyweather.android.ui.place;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.sunnyweather.android.logic.Repository;
import com.sunnyweather.android.logic.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceViewModel extends ViewModel {
    private MutableLiveData<String> searchLiveData = new MutableLiveData<>();
    ArrayList<Place> placeList = new ArrayList<>();
    LiveData<List<Place>> placeLiveData = Transformations.switchMap(searchLiveData, query -> Repository.getInstance().searchPlaces(query));

    void searchPlaces(String query) {
        searchLiveData.setValue(query);
    }

    public void savePlace(Place place) {
        Repository.getInstance().savePlace(place);
    }

    public Place getSavedPlace() {
        return Repository.getInstance().getSavedPlace();
    }

    public boolean isPlaceSaved() {
        return Repository.getInstance().isPlaceSaved();
    }
}
