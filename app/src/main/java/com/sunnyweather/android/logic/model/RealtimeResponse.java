package com.sunnyweather.android.logic.model;

import com.google.gson.annotations.SerializedName;

/*
将所有的数据模型类都定义在RealtimeResponse的内部
这样可以防止和其他接口的数据模型类有同名冲突的情况
 */
public class RealtimeResponse {

    public static class Result {
        private Realtime realtime;

        public Result(Realtime realtime) {
            this.realtime = realtime;
        }

        public Realtime getRealtime() {
            return realtime;
        }

        public void setRealtime(Realtime realtime) {
            this.realtime = realtime;
        }
    }

    public static class Realtime {
        private String skycon;
        private float temperature;
        @SerializedName("air_quality")
        private AirQuality airQuality;

        public Realtime(String skycon, float temperature, AirQuality airQuality) {
            this.skycon = skycon;
            this.temperature = temperature;
            this.airQuality = airQuality;
        }

        public String getSkycon() {
            return skycon;
        }

        public void setSkycon(String skycon) {
            this.skycon = skycon;
        }

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public AirQuality getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(AirQuality airQuality) {
            this.airQuality = airQuality;
        }
    }

    public static class AirQuality {
        private AQI aqi;

        public AirQuality(AQI aqi) {
            this.aqi = aqi;
        }

        public AQI getAqi() {
            return aqi;
        }

        public void setAqi(AQI aqi) {
            this.aqi = aqi;
        }
    }

    public static class AQI {
        private float chn;

        public AQI(float chn) {
            this.chn = chn;
        }

        public float getChn() {
            return chn;
        }

        public void setChn(float chn) {
            this.chn = chn;
        }
    }

    private String status;
    private Result result;

    public RealtimeResponse(String status, Result result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
