package com.example.ice_cream.lrange_control.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AppValues extends Application {
    public   String ip;
    public   int port;
    public String KEY_IP="KEY_IP";
    public String KEY_PORT="KEY_PORT";
    public  IpData ipData;

    public IpData getIpData() {
        this.ip=ipData.ip;
        this.port=ipData.port;
        return ipData;
    }

    public void setIpData(String ip,int port) {
        IpData ipData=new IpData(ip,port);
        this.ipData = ipData;
        this.ip=ip;
        this.port=port;
    }

    public  void saveData(Context context){//通过SharedPreferences存数据
        //需要上下文传递进来，通过上下文来获得SharedPreferences对象
        SharedPreferences sp=context.getSharedPreferences(context.getClass().getCanonicalName(), Context.MODE_PRIVATE) ;
        //sharedPreferences第一个参数是字符串，对应一个xml文件的文件名称，这里干脆用context对应类的名称
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_IP,ipData.ip );//KEY_IP,KEY_PORT等为自定义String类型常量
        editor.putInt(KEY_PORT, ipData.port);
        editor.commit();

    }
    public  void loadData(Context context){//通过SharedPreferences取数据
        SharedPreferences sp=context.getSharedPreferences(context.getClass().getCanonicalName(), Context.MODE_PRIVATE) ;
        String ip = sp.getString(KEY_IP, "10.218.186.71");
        int port = sp.getInt(KEY_PORT, 8019);
        this.ipData=new IpData(ip, port);
    }
}
