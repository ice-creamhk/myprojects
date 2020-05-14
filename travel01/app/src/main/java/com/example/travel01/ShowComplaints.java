package com.example.travel01;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;

public class ShowComplaints extends AppCompatActivity {

    ListView coms_lv;
    private SQLiteDatabase db;
    Cursor cursor;
    ArrayList<String> lists;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcomplints_lay);
        coms_lv=findViewById(R.id.coms_list);

        String nowname=getIntent().getStringExtra("name");
        InputStream is = getResources().openRawResource(R.raw.mydb6);
        db = readdatabase.openDatabase(is);
        lists=new ArrayList<>();

        cursor = db.rawQuery("select  *  from " + " complaints "+"where name= '"+nowname+"'", null);
        while (cursor.moveToNext()) {
            String coms = cursor.getString(cursor.getColumnIndex("complaintstext"));
            lists.add(coms);
        }

        arrayAdapter=new ArrayAdapter<String>(ShowComplaints.this,android.R.layout.simple_list_item_1,lists);
        coms_lv.setAdapter(arrayAdapter);

    }

}
