package com.example.ice_cream.lrange_control.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.HotKeyData;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;

import java.util.ArrayList;

public class HotKeyDialog  {
    Context context;
    ArrayList<HotKeyData> hotKeyList;//热键列表，用于HotKeyGridAdapter填充数据
    String title;//对话框的标题
    CmdClientSocket cmdClientSocket;//用于HotKeyGridAdapter的视图点击触发
    public  int gridview_idex;
    public HotKeyDialog(Context context, ArrayList<HotKeyData> hotKeyList, String title, CmdClientSocket cmdClientSocket){
        this.context=context;
        this.cmdClientSocket=cmdClientSocket;
        this.hotKeyList=hotKeyList;
        this.title=title;
    }

    public  void show(){
        final android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(context);
        View inflate= LayoutInflater.from(context).inflate(R.layout.context_lay,null,false);
        builder.setView(inflate);
        GridView gv=inflate.findViewById(R.id.gridview);
        GridAdapter gridAdapter=new GridAdapter(context,hotKeyList);
        gv.setAdapter(gridAdapter);

        gv.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridview_idex=position;

            }
        });

        builder.setNegativeButton("取消",null);
        builder.show();

    }


}
