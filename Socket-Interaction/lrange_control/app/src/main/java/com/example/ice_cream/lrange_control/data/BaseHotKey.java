package com.example.ice_cream.lrange_control.data;

import java.util.ArrayList;

public abstract class BaseHotKey {
    abstract ArrayList<HotKeyData> generateData();
    public  BaseHotKey(){

    }

    public  ArrayList<HotKeyData> getHotkeyList(){
        return  generateData();
    }
}
