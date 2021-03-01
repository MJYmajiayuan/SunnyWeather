package com.sunnyweather.android.logic.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/*
将所有的数据模型类都定义在DailyResponse的内部
这样可以防止和其他接口的数据模型类有同名冲突的情况
 */
public class DailyResponse {

    public static class Result {
        private Daily daily;

        public Result(Daily daily) {
            this.daily = daily;
        }

        public Daily getDaily() {
            return daily;
        }

        public void setDaily(Daily daily) {
            this.daily = daily;
        }
    }

    public static class Daily {
        private List<Temperature> temperature;
        private List<Skycon> skycon;
        @SerializedName("life_index")
        private LifeIndex lifeIndex;

        public Daily(List<Temperature> temperature, List<Skycon> skycon, LifeIndex lifeIndex) {
            this.temperature = temperature;
            this.skycon = skycon;
            this.lifeIndex = lifeIndex;
        }

        public List<Temperature> getTemperature() {
            return temperature;
        }

        public void setTemperature(List<Temperature> temperature) {
            this.temperature = temperature;
        }

        public List<Skycon> getSkycon() {
            return skycon;
        }

        public void setSkycon(List<Skycon> skycon) {
            this.skycon = skycon;
        }

        public LifeIndex getLifeIndex() {
            return lifeIndex;
        }

        public void setLifeIndex(LifeIndex lifeIndex) {
            this.lifeIndex = lifeIndex;
        }
    }

    public static class Temperature {
        private float max;
        private float min;

        public Temperature(float max, float min) {
            this.max = max;
            this.min = min;
        }

        public float getMax() {
            return max;
        }

        public void setMax(float max) {
            this.max = max;
        }

        public float getMin() {
            return min;
        }

        public void setMin(float min) {
            this.min = min;
        }
    }

    public static class Skycon {
        private String value;
        private Date date;

        public Skycon(String value, Date date) {
            this.value = value;
            this.date = date;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public static class LifeIndex {
        private List<LifeDescription> carWashing;
        private List<LifeDescription> ultraviolet;
        private List<LifeDescription> dressing;
        private List<LifeDescription> coldRisk;

        public LifeIndex(List<LifeDescription> carWashing, List<LifeDescription> ultraviolet, List<LifeDescription> dressing, List<LifeDescription> coldRisk) {
            this.carWashing = carWashing;
            this.ultraviolet = ultraviolet;
            this.dressing = dressing;
            this.coldRisk = coldRisk;
        }

        public List<LifeDescription> getCarWashing() {
            return carWashing;
        }

        public void setCarWashing(List<LifeDescription> carWashing) {
            this.carWashing = carWashing;
        }

        public List<LifeDescription> getUltraviolet() {
            return ultraviolet;
        }

        public void setUltraviolet(List<LifeDescription> ultraviolet) {
            this.ultraviolet = ultraviolet;
        }

        public List<LifeDescription> getDressing() {
            return dressing;
        }

        public void setDressing(List<LifeDescription> dressing) {
            this.dressing = dressing;
        }

        public List<LifeDescription> getColdRisk() {
            return coldRisk;
        }

        public void setColdRisk(List<LifeDescription> coldRisk) {
            this.coldRisk = coldRisk;
        }
    }

    public static class LifeDescription {
        private String desc;

        public LifeDescription(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    private String status;
    private Result result;

    public DailyResponse(String status, Result result) {
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
