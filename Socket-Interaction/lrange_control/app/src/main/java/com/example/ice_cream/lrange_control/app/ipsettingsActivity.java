package com.example.ice_cream.lrange_control.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.HotKeyData;
import com.example.ice_cream.lrange_control.data.IpData;
import com.example.ice_cream.lrange_control.operator.ShowNonUiUpdateCmdHandler;
import com.example.ice_cream.lrange_control.operator.ShowRemoteFileHandler;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;

import java.util.List;

public class ipsettingsActivity extends Fragment {
    List<HotKeyData> keyDataList;
    AppValues application;
    ShowRemoteFileHandler srfhandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ip_lay, container, false);
        final EditText et_ip = view.findViewById(R.id.et_ip);
        final EditText et_port = view.findViewById(R.id.et_port);
        application = (AppValues) getActivity().getApplication();
        IpData ipData = new IpData();
        application.loadData(getContext());
        ipData = application.getIpData();
        et_ip.setText(ipData.ip);
        et_port.setText(String.valueOf(ipData.port));
        Button bt = view.findViewById(R.id.submit);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = et_ip.getText().toString().trim();
                int port = Integer.parseInt(et_port.getText().toString());
                application.setIpData(ip, port);
                application.loadData(getContext());

                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());//直接Toast显示的句柄，不更新ListView
                CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, showNonUiUpdateCmdHandler);
                cmdClientSocket.work("con:" + ip);

            }
        });


        return view;
    }
}
