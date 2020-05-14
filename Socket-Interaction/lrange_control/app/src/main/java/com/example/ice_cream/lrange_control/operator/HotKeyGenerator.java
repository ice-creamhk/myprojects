package com.example.ice_cream.lrange_control.operator;

import android.util.Log;

import com.example.ice_cream.lrange_control.data.HotKeyData;
import com.example.ice_cream.lrange_control.data.Mp4_HotKey;
import com.example.ice_cream.lrange_control.data.NetFileData;
import com.example.ice_cream.lrange_control.data.PPT_HotKey;
import com.example.ice_cream.lrange_control.data.wyyMusic_HotKey;

import java.util.ArrayList;

public class HotKeyGenerator  {

    public HotKeyGenerator(){

    }
   public static ArrayList<HotKeyData> getHotkeyList(NetFileData netFileData){
        String name=netFileData.getFileName();
        String[] size=name.split("\\.");
       Log.e("test", "->>>>>>>>>>>>>>>>>>>>>>>>>>>文件类型是:"+size[1]);
        if(size[1].equals("pptx")){
            ArrayList<HotKeyData> list=new PPT_HotKey().getHotkeyList();
            return   list;
        }
       if(name.equals("cloudmusic.exe")){
           return   new wyyMusic_HotKey().getHotkeyList();
       }
       if(size[1].equals("mp4")){
           return   new Mp4_HotKey().getHotkeyList();
       }
        return null;
   }
}
