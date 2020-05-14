package com.example.ice_cream.lrange_control.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.HotKeyData;
import com.example.ice_cream.lrange_control.data.IpData;
import com.example.ice_cream.lrange_control.data.NetFileData;
import com.example.ice_cream.lrange_control.operator.CheckLocalDownloadFolder;
import com.example.ice_cream.lrange_control.operator.FileTransferBeginHandler;
import com.example.ice_cream.lrange_control.operator.HotKeyGenerator;
import com.example.ice_cream.lrange_control.operator.ShowNonUiUpdateCmdHandler;
import com.example.ice_cream.lrange_control.operator.ShowRemoteFileHandler;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;
import com.example.ice_cream.lrange_control.socket.FileDownLoadSocketThread;
import com.example.ice_cream.lrange_control.view.HotKeyDialog;

import java.io.File;
import java.util.List;

public class RemoteFileFragment extends Fragment {

    Handler handler;
    TextView tv;
    ShowRemoteFileHandler srfhandler;
    ListView listView;
    LinearLayout lt;
    RecyclerView rec_view;
    public int context_menu_idex;

    AppValues application;
    int down_port = 0;
    long data_lenth = 0;

    public void showHotKeyDialog(NetFileData netFileData) {
        ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());//直接Toast显示的句柄，不更新ListView
        CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, showNonUiUpdateCmdHandler);
        new HotKeyDialog(getContext(), HotKeyGenerator.getHotkeyList(netFileData), "热键操作表", cmdClientSocket).show();
    }

    //实现下载
    public void download(NetFileData netFileData) {
        if (netFileData.getFileType() != 0) {
            Toast.makeText(getContext(), "暂未实现整个文件夹下载功能", Toast.LENGTH_SHORT).show();
        } else {
            String str = "dlf:" + netFileData.getFilePath() + "/" + netFileData.getFileName();
            FileTransferBeginHandler fileTransferBeginHandler = new FileTransferBeginHandler(getContext(), netFileData);
            CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, fileTransferBeginHandler);
            cmdClientSocket.work(str);


        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        new MenuInflater(getContext()).inflate(R.menu.contextmenu, menu);
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        context_menu_idex = contextMenuInfo.position;
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        NetFileData netFileData = (NetFileData) listView.getAdapter().getItem(context_menu_idex);//其中listView为显示文件列表的视图
        switch (item.getItemId()) {
            case R.id.con_item:
                showHotKeyDialog(netFileData);//能根据netFileData类型决定弹出相应的热键对话框
                break;
            case R.id.dld_item:
                download(netFileData);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


    public void getpathshow(NetFileData fileData) {
        String pwd = fileData.getFilePath();
        Log.e("当前路径是:", "************ " + pwd);
        String filePath = "";
        if (pwd.endsWith("/") | pwd.endsWith("\\") | pwd.endsWith("\\\\")) {
            if (!pwd.equals(".../") && !pwd.equals("...\\") && !pwd.equals("...\\\\") &&
                    !pwd.equals("../") && !pwd.equals("..\\") && !pwd.equals("..\\\\")) {
                filePath = pwd + fileData.getFileName();
            } else {
                filePath = fileData.getFileName();
            }
        } else {
            if (!pwd.equals("...") && !pwd.equals("..")) {
                filePath = pwd + File.separator + fileData.getFileName();
            } else {
                filePath = fileData.getFileName();
            }

        }

        if (fileData.getFileType() >= 1) {
            ShowRemoteFileHandler showRemoteFileHandler = new ShowRemoteFileHandler(getContext(), listView);//会更新ListView的句柄
            CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, showRemoteFileHandler);
            cmdClientSocket.work("dir:" + filePath);
            Log.e("下一级路径是:", "************ " + filePath);
        } else {
            ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());//直接Toast显示的句柄，不更新ListView
            CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, showNonUiUpdateCmdHandler);
            cmdClientSocket.work("opn:" + filePath);
        }
    }

    public void getpanshow() {
        String filePath = "...";
        ShowRemoteFileHandler showRemoteFileHandler = new ShowRemoteFileHandler(getContext(), listView);//会更新ListView的句柄
        CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, showRemoteFileHandler);
        cmdClientSocket.work("...:" + filePath);
    }

    public void getbefore(NetFileData fileData) {
        String filePath = "..";
        ShowRemoteFileHandler showRemoteFileHandler = new ShowRemoteFileHandler(getContext(), listView);//会更新ListView的句柄
        CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, showRemoteFileHandler);
        cmdClientSocket.work("..:" + filePath);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = new MenuInflater(getContext());
        menuInflater.inflate(R.menu.op_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_show:
                lt.setVisibility(View.VISIBLE);
                break;
            case R.id.item_touch:
                Intent intent = new Intent(getContext(), TouchActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        application.saveData(getContext());
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_lay, container, false);
        listView = view.findViewById(R.id.listview);
        registerForContextMenu(listView);

        srfhandler = new ShowRemoteFileHandler(getContext(), listView);
        lt = view.findViewById(R.id.top_llt);
        rec_view = view.findViewById(R.id.rec_view);


        application = (AppValues) getActivity().getApplication();
        application.loadData(getContext());
        getpanshow();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetFileData fileData = (NetFileData) parent.getItemAtPosition(position);
                String pt = String.valueOf(position);
                if (pt.equals("0")) {
                    getpanshow();
                } else if (pt.equals("1")) {
                    getbefore(fileData);
                } else {
                    getpathshow(fileData);
                }

            }
        });

        return view;
    }
}
