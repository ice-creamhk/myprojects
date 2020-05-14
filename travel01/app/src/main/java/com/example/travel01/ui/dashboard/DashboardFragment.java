package com.example.travel01.ui.dashboard;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.travel01.DatabaseHelper;
import com.example.travel01.ProvincesAndCitys;
import com.example.travel01.R;
import com.example.travel01.city_attractions;
import com.example.travel01.readdatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.widget.AdapterView.*;


//景点
public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private static final String URL ="http://www.hotelaah.com/lvyou/index.html";
    HashMap<String,String> citys;
    ListView citys_list;
    ArrayAdapter arrayAdapter;
    List<String> listCityNames;
    List<String>  listHrefs;
    private SQLiteDatabase db;
    private Spinner province_sp,city_sp;
    private Button pc_ok_bt;
    private ArrayAdapter<String> province_sp_adapter;
    private ArrayAdapter<String> city_sp_adapter;
    private List<String> province_list;
    private List<String> city_list;
    private List<String> city_province_list;
    Cursor cursor;
    String select_city;
    String select_province;




    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj.equals("成功解析")){
//                Log.d("获取网页--------", "结果为："+citys);
                arrayAdapter=new ArrayAdapter(getActivity(),R.layout.travelcityrowview_lay,R.id.tx, listCityNames);
                citys_list.setAdapter(arrayAdapter);
            }



        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        citys=new HashMap<>();
        citys_list=root.findViewById(R.id.citys_list);
        listCityNames=new ArrayList<>() ;
        listHrefs=new ArrayList<>() ;
//        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        InputStream is = getResources().openRawResource(R.raw.mydb6);
        db= readdatabase.openDatabase(is);
        province_sp=root.findViewById(R.id.province_sp);
        city_sp=root.findViewById(R.id.city_sp);
        pc_ok_bt=root.findViewById(R.id.pc_ok_bt);

        province_list=new ArrayList<>();
        city_list=new ArrayList<>();
        city_province_list=new ArrayList<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                Document document= null;
                try {
                    document = Jsoup.connect(URL).get();
                    Elements links=document.getElementsByTag("ul");
                    int flag=0;
                    for(Element link:links){
                        if(flag==1){
                            break;
                        }
                        Elements contents=link.select("a[href]");
                        for(Element content:contents){
                            String linkHref = content.attr("href");
                            String linkText = content.text();
                            if (linkText.equals("电话区号")){
                                flag=1;
                                break;
                            }
                            int sb=0;
                            for (int i = 0; i <listCityNames.size() ; i++) {
                                if(listCityNames.get(i).equals(linkText)){
                                    sb=1;
                                }
                            }
                            if(sb==0){
                                listCityNames.add(linkText);
                                listHrefs.add(linkHref);
                                citys.put(linkText,linkHref);
                            }

                        }
                    }
                    message.obj="成功解析";
                    handler.sendMessage(message);

                } catch (IOException e) {
                    message.obj="解析失败";
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();



        //读取省份和城市到数据库
//        readPCitysExceltoDB();


        getpcdata();

        citys_list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String SelectCityName=listCityNames.get(i);
//                Log.d("点击链接为", "onItemClick: "+listHrefs.get(i));
                String str=listHrefs.get(i);
                boolean status=str.contains("_");
                if(status){
                    Intent intent=new Intent(getActivity(), city_attractions.class);
                    intent.putExtra("SelectCityName",SelectCityName);
                    intent.putExtra("SelectHref",listHrefs.get(i));
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"请选择城市",Toast.LENGTH_LONG).show();
                }

            }
        });

        province_sp_adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,province_list);
        province_sp.setAdapter(province_sp_adapter);

        province_sp.setOnItemSelectedListener( new spinnerItemSelected());


        city_sp.setOnItemSelectedListener(new cityspinnerItemSelected());
    
        pc_ok_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), city_attractions.class);
                int flag = 0;
                for (int i = 0; i <listCityNames.size() ; i++) {
                    if (listCityNames.get(i).equals(select_city)){
                        flag=i;
                    }
                }
                intent.putExtra("SelectCityName",select_city);
                intent.putExtra("SelectHref",listHrefs.get(flag));
                startActivity(intent);
            }
        });


        return root;
    }

    class spinnerItemSelected implements OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner= (Spinner) adapterView;
                select_province= (String) spinner.getItemAtPosition(i);
                List<String> now_citys=new ArrayList<>();
            for (int j = 0; j <city_province_list.size() ; j++) {
                if(city_province_list.get(j).equals(select_province)){
                    now_citys.add(city_list.get(j));
                }
            }

            city_sp_adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,now_citys);
            city_sp.setAdapter(city_sp_adapter);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

class cityspinnerItemSelected implements OnItemSelectedListener{

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner= (Spinner) adapterView;
        select_city= (String) spinner.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


    void getpcdata(){
        cursor=db.rawQuery("select * from "+DatabaseHelper.PCTABLE_NAME,null);
        while (cursor.moveToNext()){
            String pn=cursor.getString(cursor.getColumnIndex(DatabaseHelper.PCPROVINCES_NAME));
            String cn=cursor.getString(cursor.getColumnIndex(DatabaseHelper.PCCITYS_NAME));
//            ProvincesAndCitys citys=new ProvincesAndCitys(pn,cn);
            if((!pn.equals("1") )&&(!pn.equals("台湾省") )){
                int flag=0;
                for (int i = 0; i <province_list.size() ; i++) {
                    if(province_list.get(i).equals(pn)){
                        flag=1;
                    }
                }
                if(flag==0){
                    province_list.add(pn);
                }
                city_province_list.add(pn);
                city_list.add(cn);
            }
        }


    }



    //将省份和城市的excel中的数据读到sqlite数据库
    void readPCitysExceltoDB(){
        Workbook book = null;
        try {
            book = Workbook.getWorkbook(new File((Environment.getExternalStorageDirectory() + "/" + "provincesAndCitys.xls")));
            Log.d("成功打开", "provincesAndCitys ");
        } catch (IOException e) {
            Log.d("打开失败", "provincesAndCitys ");
            e.printStackTrace();
        } catch (BiffException e) {
            Log.d("打开失败", "provincesAndCitys ");
            e.printStackTrace();
        }

        Sheet sheet = book.getSheet("ProvinceAndCity");
        sheet.getRow(0);
        Log.d("开始获取", "readPCitysExceltoDB: ");

        int rows = sheet.getRows();
        ProvincesAndCitys  provincesAndCitys;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <33; j++) {
                String str=(sheet.getCell(j,i)).getContents();
                if (!str.equals("")){
                    String Cityname=str;
                    String Provincename=(sheet.getCell(j,0)).getContents();
                    if(i==0){
                        Provincename="1";
                    }
                    provincesAndCitys=new ProvincesAndCitys(Provincename,Cityname);
                    saveInfoToDataBase(provincesAndCitys);

                    Log.d("获取到", "省份城市为： "+Provincename+Cityname);
                }
            }

        }
        book.close();

    }


    //将数据存到sqlite数据库
    private  void saveInfoToDataBase(ProvincesAndCitys provincesAndCitys){
        if(db==null)
            return;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PCPROVINCES_NAME,provincesAndCitys.getProvincename());
        values.put(DatabaseHelper.PCCITYS_NAME,provincesAndCitys.getCityname());
        db.insert(DatabaseHelper.PCTABLE_NAME,null,values);
        return;

    }

}