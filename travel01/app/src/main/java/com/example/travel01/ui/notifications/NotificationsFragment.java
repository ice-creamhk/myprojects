package com.example.travel01.ui.notifications;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.travel01.DatabaseHelper;
import com.example.travel01.R;
import com.example.travel01.hotels;
import com.example.travel01.readdatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//酒店
public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    List<String> listcitynames;
    List<String> listcityhrefs;
    ListView citys_list;
    ArrayAdapter arrayAdapter;
    private SQLiteDatabase db;
    Cursor cursor;
    List<String> datalistcitynames;
    List<String> datalistcityhrefs;
    List<String> nowdatalistcitynames;
    EditText search_et;
    Button search_bt;
    Button allshows_bt;

//(不可删除）
//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.obj.equals("成功解析")) {
//                arrayAdapter=new ArrayAdapter(getActivity(),R.layout.hotelname_lay,R.id.hotelname_tx,listcitynames);
//                citys_list.setAdapter(arrayAdapter);
//                Log.d("成功存入", "结果为"+listcitynames);
//
//            }
//        }
//
//    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        listcitynames=new ArrayList<>();
        listcityhrefs=new ArrayList<>();
        datalistcitynames=new ArrayList<>();
        datalistcityhrefs=new ArrayList<>();
        nowdatalistcitynames=new ArrayList<>();
        citys_list=root.findViewById(R.id.citys_list);
        search_et=root.findViewById(R.id.search_et);
        search_bt=root.findViewById(R.id.search_bt);
        allshows_bt=root.findViewById(R.id.allshows_bt);

//        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        InputStream is = getResources().openRawResource(R.raw.mydb6);
        db= readdatabase.openDatabase(is);


        //爬取携程网城市数据并且存到数据库(不可删除）
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Message message=new Message();
//                String URL="https://hotels.ctrip.com/domestic-city-hotel.html";
//                Document document = null;
//                try {
//                    document = Jsoup.connect(URL).get();
//                    Elements links=document.getElementsByTag("dl");
//
//                    Elements dds=links.select("a[href]");
//                    for(Element content:dds){
//                        //获取酒店地名
//                        String cityname=content.ownText();
//                        //获取酒店链接
//                        String cityhref=content.attr("href");
//                        listcitynames.add(cityname);
//                        listcityhrefs.add(cityhref);
//
//                    }
////                    Log.d("---------", "run: "+citys);
//                    saveInfoToDataBase();
//                    message.obj="成功解析";
//                    handler.sendMessage(message);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        }
//        ).start();
        getdbdata();
        arrayAdapter=new ArrayAdapter(getActivity(),R.layout.hotelname_lay,R.id.hotelname_tx,datalistcitynames);
        citys_list.setAdapter(arrayAdapter);

        citys_list.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(getActivity(), hotels.class);
                intent.putExtra("SelectCityName",datalistcitynames.get(i));
                intent.putExtra("SelectHref",datalistcityhrefs.get(i));
                startActivity(intent);
            }
        });

        search_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchcity=search_et.getText().toString();
                for (int i = 0; i <datalistcitynames.size() ; i++) {
                    if(datalistcitynames.get(i).equals(searchcity)){
                        String searchcityhref=datalistcityhrefs.get(i);
                        datalistcitynames.clear();
                        datalistcityhrefs.clear();
                        datalistcitynames.add(searchcity);
                        datalistcityhrefs.add(searchcityhref);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });


        allshows_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                datalistcitynames.clear();
                datalistcityhrefs.clear();
                getdbdata();
                arrayAdapter.notifyDataSetChanged();
            }
        });


        return root;
    }

    //将数据存到sqlite数据库
    private  void saveInfoToDataBase(){
        if(db==null)
            return;

        for (int i = 0; i <listcitynames.size() ; i++) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.HOTEL_CITY_NAME,listcitynames.get(i));
            values.put(DatabaseHelper.HOTEL_HREF_NAME,listcityhrefs.get(i));
            db.insert(DatabaseHelper.HOTELTABLE_NAME,null,values);
        }

        return;
    }

    //获取数据库数据
    private void getdbdata(){
        cursor=db.rawQuery("select  * from "+DatabaseHelper.HOTELTABLE_NAME,null);
        while (cursor.moveToNext()){
            String cityname=cursor.getString(cursor.getColumnIndex(DatabaseHelper.HOTEL_CITY_NAME));
            String cityhref=cursor.getString(cursor.getColumnIndex(DatabaseHelper.HOTEL_HREF_NAME));
            datalistcitynames.add(cityname);
            datalistcityhrefs.add(cityhref);
        }

    }

}