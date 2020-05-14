package com.example.travel01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final  String dbname="mydb6";
    private  static  DatabaseHelper helper;

    public  static final String  TABLE_NAME="citys";
    public  static final String  CITYS_NAME="citynm";
    public  static final String  CITYS_ID="cityid";

    public  static final String  PCTABLE_NAME="provincesandcitys";
    public  static final String  PCPROVINCES_NAME="provincename";
    public  static final String  PCCITYS_NAME="cityname";

    public  static final String  HOTELTABLE_NAME="hotels";
    public  static final String HOTEL_CITY_NAME="hotelcityname";
    public  static final String HOTEL_HREF_NAME="hrefs";

    //个人信息表
    public  static final String  INFOTABLENAME="info";
    public  static final String INFONAME="name";
    public  static final String INFOPASSWORD="password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql_create_table=String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT ,%s TEXT)",TABLE_NAME,CITYS_NAME,CITYS_ID);
//        db.execSQL(sql_create_table);
//
//        String sql=String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT ,%s TEXT)",PCTABLE_NAME,PCCITYS_NAME,PCPROVINCES_NAME);
//        db.execSQL(sql);
//
//        String hotel_sql=String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT ,%s TEXT)",HOTELTABLE_NAME,HOTEL_CITY_NAME,HOTEL_HREF_NAME);
//        db.execSQL(hotel_sql);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
