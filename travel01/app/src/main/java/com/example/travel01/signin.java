package com.example.travel01;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class signin extends AppCompatActivity {
    EditText  normalname_et;
    EditText  normalmima_et;
    EditText  zhucename_et;
    EditText  zhucemima_et;

    Button normol_bt ;
    Button  zhuce_bt;

    private SQLiteDatabase db;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_lay);
        normalname_et=findViewById(R.id.normalname_et);
        normalmima_et=findViewById(R.id.normalmima_et);
        zhucename_et=findViewById(R.id.zhucename_et);
        zhucemima_et=findViewById(R.id.zhucemima_et);

        normol_bt=findViewById(R.id.normol_bt);
        zhuce_bt=findViewById(R.id.zhuce_bt);
//        db = new DatabaseHelper(this).getWritableDatabase();
        InputStream is = getResources().openRawResource(R.raw.mydb6);
        db=readdatabase.openDatabase(is);
        //创建个人信息表
//        createpersoninfo();

        normol_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!normalname_et.getText().toString().equals("")) && (!normalmima_et.getText().toString().equals(""))) {
                    String[] cols={"password"};
                    String selection = " name " + "=?";
                    String[] selectionArgs = { normalname_et.getText().toString() };
                    Cursor  cursor=db.query("info",cols,selection,selectionArgs,null,null,null);
                    cursor.moveToNext();
                    if(cursor.getCount()==1){
                        String truepassword = cursor.getString(cursor.getColumnIndex("password"));
                        cursor.close();
                        if (truepassword.equals(normalmima_et.getText().toString())) {
                            Intent intent = new Intent();
                            intent.putExtra("nowname", normalname_et.getText().toString());
                            setResult(3, intent);
                            finish();
                        }
                        else {
                            Toast.makeText(signin.this,"用户名或者密码错误！",Toast.LENGTH_SHORT).show();
                        }
                    }



                }else {
                    Toast.makeText(signin.this,"请填写正确的信息！",Toast.LENGTH_SHORT).show();
                }
            }
        });


        zhuce_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!zhucename_et.getText().toString().equals(""))&&(!zhucemima_et.getText().toString().equals(""))){
                    ContentValues values = new ContentValues();
                    values.put("name", String.valueOf(zhucename_et.getText()));
                    values.put("password", String.valueOf(zhucemima_et.getText()));
                    db.insert("info",null,values);

                    Intent intent = new Intent();
                    intent.putExtra("nowname", zhucename_et.getText().toString());

                    setResult(3, intent);
                    finish();
                }
                else {
                    Toast.makeText(signin.this,"请填写正确的信息！",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    void createpersoninfo(){
        String sql=String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT ,%s TEXT)","info","name","password");
        db.execSQL(sql);

    }


}
