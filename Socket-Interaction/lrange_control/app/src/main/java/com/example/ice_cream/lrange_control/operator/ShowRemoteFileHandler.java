package com.example.ice_cream.lrange_control.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ice_cream.lrange_control.data.NetFileData;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;
import com.example.ice_cream.lrange_control.view.NetFileListAdapter;

import java.util.ArrayList;

public class ShowRemoteFileHandler extends Handler {
    private Context context;
    private ListView listView;

    public ShowRemoteFileHandler(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle =msg.getData();
        ArrayList<String> list =bundle.getStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG);
        ArrayList<NetFileData> netFileList=new ArrayList<NetFileData>();
        String path=list.get(0);
        if(path.equals("false")){
            Toast.makeText(context,list.get(1),Toast.LENGTH_SHORT).show();
        }
        else {
            NetFileData nfd=new NetFileData(list.get(1),2);
            netFileList.add(nfd);
            NetFileData nfd2=new NetFileData(list.get(2),3);
            netFileList.add(nfd2);
            for(int i=3;i<list.size();i++)
            {
                Log.e("-------------test:", "显示---------- "+list.get(i));
                    NetFileData nfd3=new NetFileData(list.get(i),path);
                    netFileList.add(nfd3);
                }

            }
            NetFileListAdapter adapter = new NetFileListAdapter(context, netFileList);
            listView.setAdapter(adapter);

    }

}
