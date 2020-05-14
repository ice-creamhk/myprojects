package com.example.ice_cream.lrange_control.operator;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.NetFileData;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;
import com.example.ice_cream.lrange_control.socket.FileUpLoadSocketThread;
import com.example.ice_cream.lrange_control.view.FileProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileUploadBeginHandler extends Handler {
    private Context context;
    public int port;
    public String data_lenth;
    AppValues application;
    private int time_out = 10000;
    File file;

    public FileUploadBeginHandler(Context context,File file) {
        super();
        this.context = context;
        this.file=file;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        application = (AppValues) context.getApplicationContext();
        application.loadData(context);
        FileProgressDialog fileProgressDialog=new FileProgressDialog(context,file.toString());

        Bundle bundle = msg.getData();
        ArrayList<String> list = bundle.getStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG);
        port = Integer.valueOf(list.get(3));
        data_lenth = list.get(4);

        FileUpLoadSocketThread fileUpLoadSocketThread = new FileUpLoadSocketThread(application.ip, port,
                this, file, Long.valueOf(data_lenth), context);
        fileUpLoadSocketThread.start();
    }
}
