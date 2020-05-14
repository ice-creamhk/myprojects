package com.example.travel01.ui.home;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.travel01.CheckNetCon;
import com.example.travel01.Citys;
import com.example.travel01.MapLocation;
import com.example.travel01.R;
import com.example.travel01.SelectCity;
import com.example.travel01.TodayWeather;
import com.example.travel01.databinding.FragmentHomeBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    private ImageView Update_bt, selectcitys_bt, map_img,left_img,empty_img;
    private TextView today_city_tv, today_time_tv, today_humidty_tv, today_week_tv,  detail_tv, fl_tv,
            today_tem_tv, today_climate_tv, today_wind_tv, today_cityname_tv;

    private ImageView weatherstate_img, pm_img, refresh_img;
    RelativeLayout detail_re;
    String nowclimate = "";

    private String updatecitycode = "-1";
    private List<Citys> citylist;
    TodayWeather todayWeather = null;
    View root;
    String value = "";

    FragmentHomeBinding binding;




    //更新天气
    private Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message message) {
            Log.d("数据返回", "-------------");
            switch (message.what) {
                case 1:
                    Log.d("数据已返回，准备更新", "-------------");
                    updateTodayWeather((TodayWeather) message.obj);
                    break;
                default:
                    break;
            }
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);

        Update_bt=binding.refreshImg;
        Update_bt.setOnClickListener(this);

        selectcitys_bt=binding.selectcitysImg;
        selectcitys_bt.setOnClickListener(this);

        map_img=binding.mapImg;
        detail_re=binding.detailRe;
        left_img=binding.leftImg;
        empty_img=binding.emptyImg;


        homeViewModel= ViewModelProviders.of(requireActivity(),new SavedStateViewModelFactory(getActivity().getApplication(),this)).get(HomeViewModel.class);
        binding.setData(homeViewModel);
        binding.setLifecycleOwner(requireActivity());

        today_city_tv = binding.titleTv;
        today_cityname_tv=binding.TcitynameTv;
        today_time_tv=binding.TtimeTv;
        today_humidty_tv=binding.ThumidityTv;
        today_week_tv=binding.TweekTv;
         detail_tv=binding.detailTv;
        fl_tv=binding.flTv;
        today_tem_tv=binding.TTemTv;
        today_climate_tv=binding.TclimateTv;
        today_wind_tv=binding.TwindTv;


        nowclimate= homeViewModel.getclimate().getValue();
        Log.d("初始天气是", "onCreateView: "+nowclimate);
        if(!nowclimate.equals("")){
            setre();
        }



        if (CheckNetCon.getNetState(getActivity()) == CheckNetCon.NET_NONE) {
            Log.d("MWEATHER", "网络Error");
//            Toast.makeText(getActivity(),"网络Error",Toast.LENGTH_LONG).show();
        } else {
            Log.d("MWEATHER", "网络OK");
//            Toast.makeText(getActivity(),"网络OK",Toast.LENGTH_LONG).show();
//            getWeatherDatafromNet("101010100");
        }

        if (updatecitycode != "-1") {
            Log.d("成功获取城市", updatecitycode + "----------");
            getWeatherDatafromNet(updatecitycode);
        }

        map_img.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapLocation.class);
                startActivity(intent);
            }
        });





        return binding.getRoot() ;
    }


    //通过指定城市id爬取天气API的数据流并分析，准备更新天气
    private void getWeatherDatafromNet(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                        Log.d("date from url", str);
                    }
                    String response = sb.toString();
                    todayWeather = parseXML(response);
                    if (todayWeather != null) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = todayWeather;
                        mhandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Log.d("出现错误：", "无法读取网络");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //通过XmlPullParserFactory，获取今日天气
    private TodayWeather parseXML(String xmlData) {
        TodayWeather todayWeather = null;

        int fengliCount = 0;
        int fengxiangCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            //从XML解析工厂中获得工厂实例
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //获得XML解析器
            XmlPullParser xmlPullParser = factory.newPullParser();
            //获得输入流
            xmlPullParser.setInput(new StringReader(xmlData));
            //获得当前节点的事件类型
            int eventType = xmlPullParser.getEventType();
            //当不是结束根节点是循环每个节点
            while (eventType != xmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
//                        Log.d("parse","start doc");
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
//                        String name = xmlPullParser.getName();
//                        Log.d("全部信息", "------------- "+name);
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
//                                Log.d("city", xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
//                                Log.d("updatetime", xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
//                                Log.d("wendu", xmlPullParser.getText());
                                todayWeather.setTem(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
//                                Log.d("fengli", xmlPullParser.getText());
                                todayWeather.setWindpower(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
//                                Log.d("shidu", xmlPullParser.getText());
                                todayWeather.setHum(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
//                                Log.d("fengxiang", xmlPullParser.getText());
                                todayWeather.setWinddirection(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("detail")) {
                                eventType = xmlPullParser.next();
//                                Log.d("detail", xmlPullParser.getText());
                                todayWeather.setDetail(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fl_1")) {
                                eventType = xmlPullParser.next();
//                                Log.d("fl_1", xmlPullParser.getText());
                                todayWeather.setFl_1(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("date", xmlPullParser.getText());
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("high", xmlPullParser.getText());
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("low", xmlPullParser.getText());
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("type", xmlPullParser.getText());
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    //更新今日天气
    public void updateTodayWeather(TodayWeather todayWeather) {
        today_city_tv.setText(todayWeather.getCity() + "天气");
        today_cityname_tv.setText(todayWeather.getCity());
        today_time_tv.setText(todayWeather.getUpdatetime());
        today_humidty_tv.setText("湿度:" + todayWeather.getHum());
        detail_tv.setText(todayWeather.getDetail());
        fl_tv.setText(todayWeather.getFl_1());
        today_week_tv.setText(todayWeather.getDate());
        today_tem_tv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        today_climate_tv.setText(todayWeather.getType());
        today_wind_tv.setText("风力:" + todayWeather.getWinddirection());

        nowclimate = todayWeather.getType();
        setre();
        homeViewModel.setCityname(todayWeather.getCity());
        homeViewModel.setTime(todayWeather.getUpdatetime());
        homeViewModel.setHumidty(todayWeather.getHum());
        homeViewModel.setdetail(todayWeather.getDetail());
        homeViewModel.setfl_1(todayWeather.getFl_1());
        homeViewModel.setWeek(todayWeather.getDate());
        homeViewModel.setTem(todayWeather.getHigh() + "~" + todayWeather.getLow());
        homeViewModel.setClimate(todayWeather.getType());
        homeViewModel.setWind(todayWeather.getWinddirection());

        homeViewModel.save();

        Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
    }



    void setre(){
        Resources resources = getContext().getResources();
        if (nowclimate.equals("小雪") || nowclimate.equals("中雪") || nowclimate.equals("大雪")) {
            Drawable btnDrawable = resources.getDrawable(R.drawable.snow);
            detail_re.setBackgroundDrawable(btnDrawable);
        } else if (nowclimate.equals("小雨") || nowclimate.equals("中雨") || nowclimate.equals("大雨") || nowclimate.equals("阵雨")) {
            Drawable btnDrawable = resources.getDrawable(R.drawable.rain);
            detail_re.setBackgroundDrawable(btnDrawable);
        } else if (nowclimate.equals("阴") || nowclimate.equals("多云")) {
            Drawable btnDrawable = resources.getDrawable(R.drawable.cloudy);
            detail_re.setBackgroundDrawable(btnDrawable);
        } else if (nowclimate.equals("晴")) {
            Drawable btnDrawable = resources.getDrawable(R.drawable.sunny);
            detail_re.setBackgroundDrawable(btnDrawable);
        } else {
            Drawable btnDrawable = resources.getDrawable(R.drawable.common);
            detail_re.setBackgroundDrawable(btnDrawable);
        }
        empty_img.setVisibility(View.INVISIBLE);
    }



    //监听事件
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.refresh_img) {
            if (!updatecitycode.equals("-1")) {
                getWeatherDatafromNet(updatecitycode);
            }


        }

        if (view.getId() == R.id.selectcitys_img) {
            Intent intent = new Intent(getActivity(), SelectCity.class);
//            startActivity(intent);
            startActivityForResult(intent, 0);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            updatecitycode = data.getStringExtra("citycode");
//            String str=homeViewModel.citycode;
            Log.d("获取到城市代号：", "----------" + updatecitycode);
        } else {
            updatecitycode = "-1";
        }

    }


}