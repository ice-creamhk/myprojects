package com.example.ice_cream.lrange_control.operator;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

//检查外部存储卡是否有一个指定的下载文件夹
public class CheckLocalDownloadFolder  {

    public  CheckLocalDownloadFolder(){

    }


    public static String check() throws IOException {
        String repath=getSDPath();
        File file = new File(repath+"/appdownload");
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            boolean result=file.mkdirs();
            System.out.println("创建文件夹情况"+result);
        }
        System.out.println("---------下载文件地址是： "+file.toString());
        return  file.toString();
    }

    public static  String getSDPath(){
        File sdDir = null;

        String sdDir1 = null;
        File sdDir2 = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir1 = Environment.getExternalStorageDirectory().getAbsolutePath();//!!!!!!!!!!!!!请记住这玩意，老子花了六个多小时啊！！！！！！！！
        }
        System.out.println("*********获取 Android 的数据目录: "+sdDir1);
        return sdDir1;

    }



}
