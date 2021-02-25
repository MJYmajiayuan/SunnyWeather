package com.sunnyweather.android;

import android.app.Application;
import android.content.Context;

public class SunnyWeatherApplication extends Application {

    private static Context context;
    private static final String TOKEN = "C2oIE62rKVVdmJkB";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static String getToken() {
        return TOKEN;
    }
}
