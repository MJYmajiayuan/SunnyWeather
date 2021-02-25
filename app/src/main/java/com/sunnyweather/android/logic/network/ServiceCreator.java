package com.sunnyweather.android.logic.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator {

    private static ServiceCreator serviceCreator = null;
    private static final String BASE_URL = "https://api.caiyunapp.com/";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ServiceCreator getInstance() {
        if (serviceCreator == null) {
            serviceCreator = new ServiceCreator();
        }
        return serviceCreator;
    }

    public <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
