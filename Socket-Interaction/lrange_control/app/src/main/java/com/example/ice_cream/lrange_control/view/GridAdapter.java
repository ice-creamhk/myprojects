package com.example.ice_cream.lrange_control.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.HotKeyData;
import com.example.ice_cream.lrange_control.operator.ShowNonUiUpdateCmdHandler;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;

import java.util.List;

public class GridAdapter extends ArrayAdapter {
    Context context;
    List<HotKeyData> list;
    CmdClientSocket cmdClientSocket;

    public GridAdapter(Context context, List<HotKeyData> list) {
            super(context,android.R.layout.simple_list_item_1,list);
            this.context=context;
            this.list=list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.con_lay,null,false);
        }
        final TextView keyname=convertView.findViewById(R.id.con_tv);
        final AppValues application=(AppValues)context.getApplicationContext();
        keyname.setText(list.get(position).getHotkeyName());
        keyname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送热键命令
                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(context);//直接Toast显示的句柄，不更新ListView
                CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port,showNonUiUpdateCmdHandler);
                cmdClientSocket.work(list.get(position).getHotkeyCmd());


            }
        });
        return  convertView;

    }

    @Override
    public int getCount() {
        return list.size();
    }
}
