package com.example.ice_cream.lrange_control.operator;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.ice_cream.lrange_control.data.IpData;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;

public class MousePadOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    String cmd;
    Context context;
    IpData ipData;
    ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler;
    public MousePadOnGestureListener(Context context, IpData ipData) {
        showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(context);
        this.context=context;
        this.ipData=ipData;
    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        cmd="clk:left";
        Log.e("test", "*****************************************ip:"+ipData.ip );
        CmdClientSocket cmdClientSocket = new CmdClientSocket(ipData.ip, ipData.port, showNonUiUpdateCmdHandler);
        cmdClientSocket.work(cmd);
        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("disx:",""+distanceX);
        Log.d("disy:",""+distanceY);
        cmd="mov:"+(int)-distanceX+","+(int)-distanceY;//手势方向与鼠标控制方向相反，对值取反

        CmdClientSocket cmdClientSocket = new CmdClientSocket(ipData.ip, ipData.port, showNonUiUpdateCmdHandler);

        cmdClientSocket.work(cmd);
        return true;
    }

}
