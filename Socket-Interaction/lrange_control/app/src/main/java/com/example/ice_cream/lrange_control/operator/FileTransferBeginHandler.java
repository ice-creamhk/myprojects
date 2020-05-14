package com.example.ice_cream.lrange_control.operator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.IpData;
import com.example.ice_cream.lrange_control.data.NetFileData;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;
import com.example.ice_cream.lrange_control.socket.FileDownLoadSocketThread;
import com.example.ice_cream.lrange_control.view.FileProgressDialog;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class FileTransferBeginHandler extends Handler { //
    private NetFileData fileData;
    private Context context;
    public int port;
    public String data_lenth;
    AppValues application;
    private int time_out = 10000;
    public FileTransferBeginHandler(Context context, NetFileData fileData) {
        super();
        this.fileData = fileData;
        this.context = context;
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        application = (AppValues) context.getApplicationContext();
        application.loadData(context);
        FileProgressDialog fileProgressDialog=new FileProgressDialog(context,fileData.getFileName());


        String data_address = null;
        try {
            CheckLocalDownloadFolder checkLocalDownloadFolder=new CheckLocalDownloadFolder();
            data_address = checkLocalDownloadFolder.check();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(data_address);
        Bundle bundle = msg.getData();
        ArrayList<String> list = bundle.getStringArrayList(CmdClientSocket.KEY_SERVER_ACK_MSG);
        port = Integer.valueOf(list.get(3));
        data_lenth = list.get(4);

        FileDownLoadSocketThread fileDownLoadSocketThread = new FileDownLoadSocketThread(application.ip, port,
                this, file, Long.valueOf(data_lenth), context);
        fileDownLoadSocketThread.start();
    }
}
