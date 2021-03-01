package com.sunnyweather.android.logic.model;

// 将Realtime和Daily对象封装起来
public class Weather {

    private RealtimeResponse.Realtime realtime;
    private DailyResponse.Daily daily;

    public Weather(RealtimeResponse.Realtime realtime, DailyResponse.Daily daily) {
        this.realtime = realtime;
        this.daily = daily;
    }

    public RealtimeResponse.Realtime getRealtime() {
        return realtime;
    }

    public void setRealtime(RealtimeResponse.Realtime realtime) {
        this.realtime = realtime;
    }

    public DailyResponse.Daily getDaily() {
        return daily;
    }

    public void setDaily(DailyResponse.Daily daily) {
        this.daily = daily;
    }
}

