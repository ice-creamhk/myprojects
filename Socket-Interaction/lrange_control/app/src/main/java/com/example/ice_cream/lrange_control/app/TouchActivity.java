package com.example.ice_cream.lrange_control.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.IpData;
import com.example.ice_cream.lrange_control.operator.MousePadOnGestureListener;
import com.example.ice_cream.lrange_control.operator.RollGestureListener;
import com.example.ice_cream.lrange_control.operator.ShowNonUiUpdateCmdHandler;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;

public class TouchActivity  extends Fragment {
    TextView mousePad;
    TextView mouseroll;
    Button  left_bt;
    ToggleButton lock_bt;
    Button  right_bt;
    AppValues application;
    private GestureDetector mGestureDetector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.touch_lay, container, false);
        application= (AppValues) getActivity().getApplication();
        application.loadData(getContext());
        IpData ipData=new IpData();
        ipData=application.getIpData();
        mousePad=view.findViewById(R.id.touch_tv);
        mouseroll=view.findViewById(R.id.roll_tv);
        left_bt=view.findViewById(R.id.left_bt);
        right_bt=view.findViewById(R.id.right_bt);
        lock_bt=view.findViewById(R.id.lock_bt);
        final ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(getContext());
        final IpData finalIpData = ipData;


        left_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CmdClientSocket cmdClientSocket = new CmdClientSocket(finalIpData.ip, finalIpData.port, showNonUiUpdateCmdHandler);
                cmdClientSocket.work("clk:left");
            }
        });
        right_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CmdClientSocket cmdClientSocket = new CmdClientSocket(finalIpData.ip, finalIpData.port, showNonUiUpdateCmdHandler);
                cmdClientSocket.work("clk:right");
            }
        });
        lock_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CmdClientSocket cmdClientSocket = new CmdClientSocket(finalIpData.ip, finalIpData.port, showNonUiUpdateCmdHandler);
                if(lock_bt.isChecked()){
                    cmdClientSocket.work("clk:left_press");
                }
                else {
                    cmdClientSocket.work("clk:left_release");
                }

            }
        });


        final GestureDetector mGestureDetector1=new GestureDetector(getContext(), new MousePadOnGestureListener(getContext(),ipData));
        mousePad.setOnTouchListener(new TextView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                mGestureDetector1.onTouchEvent(event);
                return true;
            }
        });
        final GestureDetector mGestureDetector2=new GestureDetector(getContext(), new RollGestureListener(getContext(),ipData));
        mouseroll.setOnTouchListener(new TextView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector2.onTouchEvent(event);
                return true;
            }
        });
        return  view;
    }



    @Override
    public void onPause() {
        if(lock_bt.isChecked()){
            Toast.makeText(getContext(),"Please unlock!",Toast.LENGTH_LONG).show();
        }
        super.onPause();
    }
}
