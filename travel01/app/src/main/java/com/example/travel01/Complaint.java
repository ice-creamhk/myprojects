package com.example.travel01;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class Complaint extends AppCompatActivity {

    EditText com_et;
    Button com_bt;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_lay);
        com_et=findViewById(R.id.com_et);
        com_bt=findViewById(R.id.com_bt);
        final String nowname= getIntent().getStringExtra("name");

        InputStream is = getResources().openRawResource(R.raw.mydb6);
        db= readdatabase.openDatabase(is);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String sql_create_table=String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT ,%s TEXT)","complaints","name","complaintstext");
//                db.execSQL(sql_create_table);
//                Log.d("complaints", "创建成功 ");
//            }
//        }).start();


        com_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textstr=com_et.getText().toString();
                if(!textstr.equals("")){
                    ContentValues values = new ContentValues();
                    values.put("name",nowname);
                    values.put("complaintstext",textstr);
                    Log.d("投诉写入", "-----"+nowname+textstr);
                    db.insert("complaints",null,values);
                }
                    finish();
            }
        });



    }




}
