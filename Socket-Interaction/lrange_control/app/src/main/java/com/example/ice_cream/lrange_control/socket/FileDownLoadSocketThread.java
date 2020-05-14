package com.example.ice_cream.lrange_control.socket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileDownLoadSocketThread extends Thread {
    public String ip;
    public int port;
    public Handler handler;
    public File file;
    public long fileSize;
    private Socket socket;
    private int time_out = 10000;

    private DataInputStream dis;
    private FileOutputStream fos;

    Context context;

    public FileDownLoadSocketThread(String ip, int port, Handler handler, File file, long fileSize, Context context) {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.file = file;
        this.context = context;
    }

    private void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        System.out.println("------------下载ip是:" + ip);
        System.out.println("------------下载端口是:" + port);
        socket = new Socket();
        socket.connect(address, time_out);

    }


    @Override
    public void run() {
        try {
            connect();
            System.out.println("======== 连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dis = new DataInputStream(socket.getInputStream());
            String fileName = dis.readUTF();
            long fileLength = dis.readLong();
            File pfile = new File(file.getAbsolutePath() + File.separatorChar, fileName);
            System.out.println("======== 接收地址" + pfile.toString());

            if(!pfile.exists()){
                pfile.createNewFile();
            }
            fos = new FileOutputStream(pfile);
            byte[] bytes = new byte[1024];
            int length = 0;
            System.out.println("======== 开始接收文件");
            long progress = 0;
            while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
                progress += length;
                System.out.print("| " + (100 * progress / pfile.length()) + "% |");
            }
            System.out.println("======== 文件接收成功 File Name：" + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
                socket.close();
            } catch (Exception e) {
            }
        }
        super.run();
    }


}
