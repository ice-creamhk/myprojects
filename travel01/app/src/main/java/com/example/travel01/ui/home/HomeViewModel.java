package com.example.travel01.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.travel01.R;

public class HomeViewModel extends AndroidViewModel {

    SavedStateHandle handle;
    public String home_shpname = getApplication().getResources().getString(R.string.home_shpname);
    public String cityname = getApplication().getResources().getString(R.string.cityname);
    public String time = getApplication().getResources().getString(R.string.time);
    public String humidty = getApplication().getResources().getString(R.string.humidty);
    public String week = getApplication().getResources().getString(R.string.week);
    public String detail = getApplication().getResources().getString(R.string.detail);
    public String fl_1 = getApplication().getResources().getString(R.string.fl_1);
    public String tem = getApplication().getResources().getString(R.string.tem);
    public String climate = getApplication().getResources().getString(R.string.climate);
    public String wind = getApplication().getResources().getString(R.string.wind);


    public HomeViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;

        if (!handle.contains(cityname)) {
            load();
        }
    }


    void load() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(home_shpname, Context.MODE_PRIVATE);
        String str1 = sharedPreferences.getString(cityname, "");
        String str2 = sharedPreferences.getString(time, "");
        String str3 = sharedPreferences.getString(humidty, "");
        String str4 = sharedPreferences.getString(week, "");
        String str5 = sharedPreferences.getString(detail, "");
        String str6 = sharedPreferences.getString(fl_1, "");
        String str7 = sharedPreferences.getString(tem, "");
        String str8 = sharedPreferences.getString(climate, "");
        String str9 = sharedPreferences.getString(wind, "");


        handle.set(cityname, str1);
        handle.set(time, str2);
        handle.set(humidty, str3);
        handle.set(week, str4);
        handle.set(detail, str5);
        handle.set(fl_1, str6);
        handle.set(tem, str7);
        handle.set(climate, str8);
        handle.set(wind, str9);


    }

    void save() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(home_shpname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(cityname, getcityname().getValue());
        editor.putString(time, gettime().getValue());
        editor.putString(humidty, gethumidty().getValue());
        editor.putString(week, getweek().getValue());
        editor.putString(detail, getdetail().getValue());
        editor.putString(fl_1, getfl_1().getValue());
        editor.putString(tem, gettem().getValue());
        editor.putString(climate, getclimate().getValue());
        editor.putString(wind, getwind().getValue());

        editor.apply();
    }


    public LiveData<String> getcityname() {
        return handle.getLiveData(cityname);
    }

    public LiveData<String> gettime() {
        return handle.getLiveData(time);
    }

    public LiveData<String> gethumidty() {
        return handle.getLiveData(humidty);
    }

    public LiveData<String> getweek() {
        return handle.getLiveData(week);
    }

    public LiveData<String> getdetail() {
        return handle.getLiveData(detail);
    }

    public LiveData<String> getfl_1() {
        return handle.getLiveData(fl_1);
    }

    public LiveData<String> gettem() {
        return handle.getLiveData(tem);
    }

    public LiveData<String> getclimate() {
        return handle.getLiveData(climate);
    }

    public LiveData<String> getwind() {
        return handle.getLiveData(wind);
    }

    public void setCityname(String string) {
        handle.set(cityname, string);
    }

    public void setTime(String string) {
        handle.set(time, string);
    }

    public void setHumidty(String string) {
        handle.set(humidty, string);
    }

    public void setWeek(String string) {
        handle.set(week, string);
    }

    public void setdetail(String string) {
        handle.set(detail, string);
    }

    public void setfl_1(String string) {
        handle.set(fl_1, string);
    }

    public void setTem(String string) {
        handle.set(tem, string);
    }

    public void setClimate(String string) {
        handle.set(climate, string);
    }

    public void setWind(String string) {
        handle.set(wind, string);
    }
}