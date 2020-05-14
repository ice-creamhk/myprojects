package com.example.ice_cream.lrange_control.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class CmdClientSocket {

    private String ip;
    private int port;
    private ArrayList<String> cmd;
    private int time_out=10000;
    private Handler handler;
    private Socket socket;
    public static final String KEY_SERVER_ACK_MSG = "KEY_SERVER_ACK_MSG";
    private OutputStreamWriter writer;
    private BufferedReader bufferedReader;
    public CmdClientSocket(String ip,int port,  Handler handler) {
        this.port = port;
        this.ip = ip;
        this.handler = handler;
    }
    private void connect() throws IOException {
        InetSocketAddress address=new InetSocketAddress(ip,port);
        socket=new Socket();
        socket.connect(address,time_out);

    }
    private void writeCmd(String cmd) throws IOException {
        BufferedOutputStream os=new BufferedOutputStream(socket.getOutputStream());
        writer=new OutputStreamWriter(os,"UTF-8");
        writer.write(cmd+"\n");
        writer.flush();
    }
    private ArrayList<String> readSocketMsg() throws IOException {
        ArrayList<String> msgList=new ArrayList<>();
        InputStreamReader isr=new InputStreamReader(socket.getInputStream(),"UTF-8");
        bufferedReader=new BufferedReader(isr);
        String numStr = bufferedReader.readLine();
        String isOK=bufferedReader.readLine();
        Log.e("isOK", "----------isok的值是: "+isOK );
        if(!isOK.equals("ok")){
            msgList.add("false");
            msgList.add(isOK);
            return  msgList;
        }

        int linNum = Integer.parseInt(numStr);
        Log.e("--------------test:", "行数: "+linNum);
        String path = bufferedReader.readLine();
        msgList.add(path);
        msgList.add("...");
        msgList.add("..");
        for (int i = 0; i <linNum-1; i++) {
            String s = bufferedReader.readLine();
            Log.e("--------------test:", "传递: "+s);
            msgList.add(s);
        }
        return msgList;
    }
    private void close() throws IOException {
        bufferedReader.close();
        writer.close();
        socket.close();
    }
    private void doCmdTask(String cmd){
        ArrayList<String> msgList=new ArrayList<>();
        try {
            connect();
            writeCmd(cmd);
            msgList = readSocketMsg();
            close();
        } catch (IOException e) {
            msgList.add("false");
            msgList.add(e.toString());//在msgList列表中放入错误信息
            e.printStackTrace();

        }
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SERVER_ACK_MSG,msgList);

        message.setData(bundle);
        handler.sendMessage(message);

    }
    public void work(final String cmd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCmdTask(cmd);
            }
        }).start();
    }
}
