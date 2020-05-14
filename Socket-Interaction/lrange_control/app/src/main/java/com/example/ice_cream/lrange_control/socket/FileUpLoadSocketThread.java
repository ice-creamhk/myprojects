package com.example.ice_cream.lrange_control.socket;

import android.content.Context;
import android.os.Handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class FileUpLoadSocketThread  extends Thread {
    public String ip;
    public int port;
    public Handler handler;
    public File file;
    public long fileSize;
    private Socket socket;
    private int time_out = 10000;

    private FileInputStream fis;

    private DataOutputStream dos;

    Context context;

    public FileUpLoadSocketThread(String ip, int port, Handler handler, File file, long fileSize, Context context) {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.file = file;
        this.context = context;
    }

    private void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        System.out.println("------------传输ip是:" + ip);
        System.out.println("------------传输端口是:" + port);
        socket = new Socket();
        socket.connect(address, time_out);

    }



    @Override
    public void run() {

        try {
            connect();
            System.out.println("成功连接，开始传输"+file.toString());
            fis = new FileInputStream(file);
            dos = new DataOutputStream(socket.getOutputStream());

            // 文件名和长度
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();

            // 开始传输文件
            System.out.println("======== 开始传输文件 ========");
            byte[] bytes = new byte[8*1024];
            int length = 0;
            long progress = 0;
            while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
                progress += length;
                System.out.print("| " + (100*progress/file.length()) + "% |");
            }
            System.out.println();
            System.out.println("======== 文件传输成功 ========");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if(dos != null)
                try {
                    dos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }




        super.run();
    }
}
