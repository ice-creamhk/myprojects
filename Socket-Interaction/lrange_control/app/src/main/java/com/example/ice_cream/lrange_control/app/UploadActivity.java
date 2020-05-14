package com.example.ice_cream.lrange_control.app;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.IpData;
import com.example.ice_cream.lrange_control.operator.FileUploadBeginHandler;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;

import java.io.File;

import static android.app.Activity.RESULT_OK;


public class UploadActivity extends Fragment {

    AppValues application;
    String path=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_lay, container, false);


        application = (AppValues) getActivity().getApplication();
        IpData ipData = new IpData();
        application.loadData(getContext());
        ipData = application.getIpData();

        Button bt = view.findViewById(R.id.selfiles_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });



        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
             path=uri.getPath();
             String beforepath=Environment.getExternalStorageDirectory().getAbsolutePath();
            System.out.println("本地文件地址："+path);
            String cmd = path.substring(0, path.indexOf(":"));// 命令类型
            String str = path.substring(cmd.length() + 1);// 文件地址
            Log.e("test", "----------------ip是 "+application.ip);
            String abspath = "ulf:" +str;
            FileUploadBeginHandler fileUploadBeginHandler = new FileUploadBeginHandler(getContext(),new File(beforepath+"//"+str));
            CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, fileUploadBeginHandler);
            cmdClientSocket.work(abspath);
        }

    }

}