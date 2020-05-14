package com.example.travel01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hotels  extends Activity {
    String URL;
    List<String> HotelNameList;
    List<String> HotelPriceList;
    List<String> HotelAddressList;
    ImageView hotelback_img;
    TextView hoteltv;
    SimpleAdapter simpleAdapter;
    List<Map<String,String>> alllists;
    ListView hotel_lv;
    int num=0;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals("成功解析")){
                simpleAdapter.notifyDataSetChanged();
//                Log.d("成功更新", "第"+num+"次------------------------------");
            }

        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_lay);

        HotelNameList=new ArrayList<>();
        HotelPriceList=new ArrayList<>();
        HotelAddressList=new ArrayList<>();
        hotelback_img=findViewById(R.id.hotelback_img);
        hoteltv=findViewById(R.id.hoteltitle_tv);
        alllists=new ArrayList<>();
        hotel_lv=findViewById(R.id.hotel_lv);


        String cityhref=getIntent().getStringExtra("SelectHref");
        String cityname=getIntent().getStringExtra("SelectCityName");
        URL="https://hotels.ctrip.com"+cityhref;
        hoteltv.setText("当前为："+cityname+"境内酒店");
        hotelback_img.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document= null;
                try {
                    document = Jsoup.connect(URL).get();
                    Element element=document.getElementById("hotel_list");
                    Elements elements=element.getElementsByClass("hotel_item");
                    int i=1;

                    for(Element aaa:elements){
                        //获取酒店名称
                        Elements element1=aaa.getElementsByClass("hotel_name").tagName("h2");
                        for(Element element11:element1){
                            String HotelName=element11.getElementsByTag("a").attr("title");
                            HotelNameList.add(HotelName);

                        }

                        //获取酒店地址
                        Elements element2=aaa.getElementsByClass("hotel_item_htladdress");
                        for (Element element21:element2){
                            String str=element21.ownText();
                            String  HotelAddress;
                            String  cf=str.substring(0,3);
                            if(cf.equals("【 】")){
                                  HotelAddress=str.substring(3);
                            }else {
                                HotelAddress=str;
                            }
                            HotelAddressList.add(HotelAddress);

                        }
                    }

                    //整合
                    for (int j = 0; j <HotelNameList.size() ; j++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("HotelName",HotelNameList.get(j));
                        map.put("HotelAddress",HotelAddressList.get(j));
                        alllists.add(map);

                    }
//                    num++;

                    //获取酒店价格
//                        https://hotels.ctrip.com/Domestic/Tool/AjaxHotelListPrice.aspx
                    Message message=new Message();
                    message.obj="成功解析";
                    handler.sendMessage(message);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        ).start();



        simpleAdapter=new SimpleAdapter(this,alllists,R.layout.hotelrowview,new String[]{"HotelName","HotelAddress"},
                new int[]{R.id.eachhotelname,R.id.eachhoteladress});

        hotel_lv.setAdapter(simpleAdapter);


    }




}
