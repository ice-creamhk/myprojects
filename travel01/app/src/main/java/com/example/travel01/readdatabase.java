package com.example.travel01;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class readdatabase  {

    private static final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()+"/mydbtest2";
    private static final String DATABASE_FILENAME = "mydb6.db";
    private SQLiteDatabase db;


    public readdatabase() {

    }

    public static SQLiteDatabase openDatabase(InputStream is)
    {
        try
        {
            // 获得mydb.db文件的绝对路径
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
//             如果目录中存在，创建这个目录
            if (!dir.exists()) {
                dir.mkdir();
                Log.d("创建新目录成功", "--------" );
            }
            else {
                Log.d("目录已存在", "--------" );
            }
            // 如果目录中不存在mydb.db文件，则从res\raw目录中复制这个文件到SD卡的目录
            if (!(new File(databaseFilename)).exists())
            {
                Log.d("开始复制", "------" );
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                // 开始复制dictionary.db文件
                while ((count = is.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }

                fos.close();
            }else {
                Log.d("数据库已存在", "--------" );
            }
            is.close();
            // 打开/sdcard/dictionary目录中的dictionary.db文件
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);
            Log.d("数据加载完毕", "------" );
            return database;
        }
        catch (Exception e)
        {
        }
        return null;
    }





}
