package com.example.ice_cream.lrange_control.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.ice_cream.lrange_control.data.NetFileData;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;
import com.example.ice_cream.lrange_control.view.NetFileListAdapter;

import java.util.ArrayList;

public class ShowNonUiUpdateCmdHandler extends Handler {
         Context context;

    public ShowNonUiUpdateCmdHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle =msg.getData();
        ArrayList<String> list =bundle.getStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG);
        String path=list.get(0);
        if(path.equals("false")){
            Toast.makeText(context,list.get(1),Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,list.get(3),Toast.LENGTH_SHORT).show();

        }

    }

}
