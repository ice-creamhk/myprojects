package com.example.travel01;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class attractiondetails extends Activity {

    String attrname = "";
    String geturl;
    TextView more_tv;
    Button more_back;
    String str = "";
    String picsrc;
    RelativeLayout sb_re;
    ImageView change_img;
    Drawable get_imgresouse = null;


    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals("成功解析")) {
                more_tv.setText(str);

            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attr_moredetails_lay);
        more_tv = findViewById(R.id.more_tv);
        more_back = findViewById(R.id.more_back);
        sb_re = findViewById(R.id.sb_re);

        change_img = findViewById(R.id.change_img);

        attrname = getIntent().getStringExtra("attrname");
        if (!attrname.equals("")) {
            geturl = "https://baike.baidu.com/item/" + attrname;
        }


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("获得景点", "数据为: " + attrname);
                Document document = null;

                try {
                    document = Jsoup.connect(geturl).get();

                    Element element2 = document.select("div.lemma-summary").first();

                    Elements elements = element2.select("div.para");
                    for (Element element3 : elements) {
                        str = str + element3.text();
                    }

                    Element element3 = document.select("div.summary-pic").first();

                    if (element3 != null) {
                        Element element = element3.selectFirst("img[src]");
                        picsrc = element.attr("src");

                        attractiondetails.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(attractiondetails.this)
                                        .load(picsrc)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                Log.d("下载失败", "------------ ");
                                                return false;
                                            }

                                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                Log.d("下载成功", "------------ ");
                                                get_imgresouse = resource;
                                                return true;
                                            }
                                        })
                                        .into(1200, 800);
                            }
                        });


                    }

                    Message message = new Message();
                    message.obj = "成功解析";
                    handler.sendMessage(message);


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();

        more_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        change_img.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (get_imgresouse == null) {
                    Toast.makeText(attractiondetails.this, "找不到该景点图片！", Toast.LENGTH_LONG);
                } else {
                    change_img.setImageDrawable(get_imgresouse);
                }


            }
        });


    }


}
