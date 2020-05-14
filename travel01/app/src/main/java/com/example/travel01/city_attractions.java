package com.example.travel01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class city_attractions  extends Activity {
    List<String> listNumbers;
    List<String>  listattrs;
    List<String>  listspots;
    String  SelectCityName;
    String  SelectHref;
    String URL;

    ListView attr_list;
    SimpleAdapter simpleAdapter;

    List<Map<String,String>> lists;

    ImageView attrback_img;
    TextView attrtitle_tv;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals("成功解析")){
                simpleAdapter.notifyDataSetChanged();
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attractionsdetail_lay);
        listNumbers=new ArrayList<>();
        listattrs=new ArrayList<>();
        listspots=new ArrayList<>();
        attr_list=findViewById(R.id.attr_list);
        attrback_img=findViewById(R.id.attrback_img);
        attrtitle_tv=findViewById(R.id.attrtitle_tv);

        SelectCityName=getIntent().getStringExtra("SelectCityName");
        attrtitle_tv.setText("当前城市是："+SelectCityName);
        SelectHref=getIntent().getStringExtra("SelectHref");
        URL="http://www.hotelaah.com/lvyou/"+SelectHref;

        lists=new ArrayList<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document= null;
                try {
                    document = Jsoup.connect(URL).get();
                    Elements elements=document.getElementsByTag("tbody");
                    int i=1;
                    int flag=0;
                    for(Element element:elements){
                        if(flag==1){
                            break;
                        }

                        if(i==6){
                            Elements trs=element.select("tr");
                            int j=1;
                            Element lastTr=trs.last();
                            for (Element tr:trs ){
                                if(j>=6){
                                    if(tr.equals(lastTr)){
                                        flag=1;
                                        break;
                                    }
                                    Elements tds=tr.select("td");
                                    int num=1;
                                    for(Element td:tds){
                                        if(num==1){
                                            listNumbers.add(td.text());
                                        }
                                        if(num==2){
                                            listattrs.add(td.text());
                                        }
                                        if(num==3){
                                            listspots.add(td.text());
                                        }
                                        num++;

                                    }

                                }
                                j++;
                            }


                        }

                        i++;
                    }

                    for (int k = 0; k <listattrs.size(); k++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("number",listNumbers.get(k));
                        map.put("attr",listattrs.get(k));
                        map.put("spot",listspots.get(k));
                        lists.add(map);

                    }
                    lists.remove(0);
                    Log.d("获取所有景点--------", "结果为："+lists.get(0));
                    Message message=new Message();
                    message.obj="成功解析";
                    handler.sendMessage(message);


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();


        simpleAdapter=new SimpleAdapter(this,lists,R.layout.attr_rowview_lay,new String[]{"number","attr","spot"},
                new int[]{R.id.attrnumber_tv,R.id.attrname_tv,R.id.attrspot_tv});

        attr_list.setAdapter(simpleAdapter);

        attr_list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                final String str="https://baike.baidu.com/item/"+listattrs.get(position+1);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Document document= null;

                        try {
                            document = Jsoup.connect(str).get();
                            Element element2=document.select("div.lemma-summary").first();
                            if(element2!=null) {
                                Intent intent=new Intent(city_attractions.this, attractiondetails.class);
                                intent.putExtra("attrname",listattrs.get(position+1));
                                startActivity(intent);
                            }else {
                                Looper.prepare();
                                Toast.makeText(city_attractions.this,"找不到该景点详细信息",Toast.LENGTH_LONG);
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



            }
        });




        attrback_img.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
