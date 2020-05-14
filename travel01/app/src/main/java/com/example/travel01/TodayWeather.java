package com.example.travel01;


import androidx.annotation.NonNull;

public class TodayWeather {
    private  String  city;
    private  String  updatetime;
    private  String  tem;
    private  String  hum;
    private  String  detail;
    private  String  fl_1;
    private  String  winddirection;
    private  String  windpower;
    private  String  date;
    private  String  high;
    private  String  low;
    private  String  type;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFl_1() {
        return fl_1;
    }

    public void setFl_1(String fl_1) {
        this.fl_1 = fl_1;
    }

    public String getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public String getWindpower() {
        return windpower;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "TodayWeather{"+
                "city="+city+'\''+
                ",updatetime="+updatetime+'\''+
                "tem="+tem+'\''+
                "hum="+hum+'\''+
                "detail="+detail+'\''+
                "fl_1="+fl_1+'\''+
                "winddirection="+winddirection+'\''+
                "windpower="+windpower+'\''+
                "date="+date+'\''+
                "high="+high+'\''+
                "low="+low+'\''+
                "type"+type+'\''+"}";
    }
}
