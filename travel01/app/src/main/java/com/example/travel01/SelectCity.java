package com.example.travel01;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.travel01.databinding.CitysLayBinding;
import com.example.travel01.ui.home.HomeViewModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SelectCity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back_bt;
    private SQLiteDatabase db;
    Cursor cursor;
    private ListView allcitys_lv;
    SimpleCursorAdapter cursorAdapter;
    TextView tv1, tv2;
    String citylistcode;
    List<Citys> citylist;
    public static String CITYCODE_KEY = "";
    CitysLayBinding bing;
    HomeViewModel homeViewModel;
    public String now_citycode;
    private EditText weathercityselect_et;
    private Button weathercityselect_bt;
    Cursor selectcursor;
    private Button allcity_bt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bing = DataBindingUtil.setContentView(this, R.layout.citys_lay);
//        setContentView(R.layout.citys_lay);

        //通过binding直接替代findviewbyid
        back_bt = bing.titleSelectCityBack;
        back_bt.setOnClickListener(this);
        allcitys_lv = findViewById(R.id.allcitys_lv);
        weathercityselect_bt = findViewById(R.id.weathercityselect_bt);
        weathercityselect_et = findViewById(R.id.weathercityselect_et);
        allcity_bt = findViewById(R.id.allcity_bt);

//        db = new DatabaseHelper(this).getWritableDatabase();
        InputStream is = getResources().openRawResource(R.raw.mydb6);
        db = readdatabase.openDatabase(is);

        cursor = db.rawQuery("select  *, count(distinct cityid) from " + DatabaseHelper.TABLE_NAME + " group by cityid ", null);

        //传参
        homeViewModel = ViewModelProviders.of(this, new SavedStateViewModelFactory(getApplication(), this)).get(HomeViewModel.class);
        bing.setData(homeViewModel);
        bing.setLifecycleOwner(this);

        citylist = new ArrayList<>();
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.rowview, cursor, new String[]{DatabaseHelper.CITYS_NAME, DatabaseHelper.CITYS_ID}, new int[]{R.id.tv1, R.id.tv2});
        allcitys_lv.setAdapter(cursorAdapter);

//        readExcelToDB(this);//将excel中的数据读到sqlite数据库
        getdbdata();
        allcitys_lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selectcity = citylist.get(i).getCitynm();
                citylistcode = citylist.get(i).getCityid();
                if (selectcursor != null && (!selectcursor.getString(cursor.getColumnIndex(DatabaseHelper.CITYS_NAME)).equals(""))) {
                    citylistcode = selectcursor.getString(cursor.getColumnIndex(DatabaseHelper.CITYS_ID));
                }
                Log.d("点击城市和代号：", selectcity + citylistcode + "----------");

//                homeViewModel.citycode=citylistcode;

                Intent intent = new Intent();
                intent.putExtra("citycode", citylistcode);
                setResult(1, intent);
                finish();
            }
        });


        weathercityselect_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showselectcity(weathercityselect_et.getText().toString());
            }
        });

        allcity_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor = db.rawQuery("select  *, count(distinct cityid) from " + DatabaseHelper.TABLE_NAME + " group by cityid ", null);
                cursorAdapter.changeCursor(cursor);
                cursorAdapter.notifyDataSetChanged();
            }
        });
    }


    //将城市代码的excel中的数据读到sqlite数据库
    private void readExcelToDB(Context context) {

        Workbook book = null;
        try {
            book = Workbook.getWorkbook(new File((Environment.getExternalStorageDirectory() + "/" + "cityscode.xls")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        book.getNumberOfSheets();
        Sheet sheet = book.getSheet(0);
        int rows = sheet.getRows();
        Citys info = null;
        for (int i = 1; i < rows; ++i) {
            String citynm = (sheet.getCell(0, i)).getContents();
            String cityid = (sheet.getCell(1, i)).getContents();
            info = new Citys(citynm, cityid);
            saveInfoToDataBase(info);
        }
        book.close();
    }
    //将数据存到sqlite数据库
    private void saveInfoToDataBase(Citys citys) {
        if (db == null)
            return;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CITYS_NAME, citys.getCitynm());
        values.put(DatabaseHelper.CITYS_ID, citys.getCityid());
        db.insert(DatabaseHelper.TABLE_NAME, null, values);

        return;
    }

    //获取数据库数据
    private void getdbdata() {
        while (cursor.moveToNext()) {
            String citynm = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITYS_NAME));
            String cityid = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITYS_ID));
            Citys citys = new Citys(citynm, cityid);
            citylist.add(citys);
        }

    }


    void showselectcity(String cityname) {
        selectcursor = db.rawQuery("select  *, count(distinct cityid) from " + DatabaseHelper.TABLE_NAME + " where  citynm= '" + cityname + "' group by cityid ", null);
        cursorAdapter.changeCursor(selectcursor);
        cursorAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPause() {
        cursor.close();
        db.close();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_selectCity_back:
                finish();
                break;
//            case R.id.weathercityselect_bt:
//               showselectcity(weathercityselect_et.getText().toString());
//                break;
//            case R.id.allcity_bt:
//                cursorAdapter.changeCursor(cursor);
//                cursorAdapter.notifyDataSetChanged();
//                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
