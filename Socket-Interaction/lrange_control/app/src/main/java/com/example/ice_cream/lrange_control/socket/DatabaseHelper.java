package com.example.ice_cream.lrange_control.socket;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
   public static  final String DATABASE_NAME="hk_condata";
    public  static  final String TABLE_NAME="HOTDATA";
    public static  final String CMD_NAME="CMD_NAME";
    public static  final String CMD_LIST="CMD_LIST";
    public static  final String    ID="_id";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql= String.format("create TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT ,%s TEXT,%s text)",
                TABLE_NAME,ID,CMD_NAME,CMD_LIST);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
