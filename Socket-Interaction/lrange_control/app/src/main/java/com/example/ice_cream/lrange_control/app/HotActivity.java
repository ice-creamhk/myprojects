package com.example.ice_cream.lrange_control.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.AppValues;
import com.example.ice_cream.lrange_control.data.IpData;
import com.example.ice_cream.lrange_control.data.NetFileData;
import com.example.ice_cream.lrange_control.operator.ShowNonUiUpdateCmdHandler;
import com.example.ice_cream.lrange_control.socket.CmdClientSocket;
import com.example.ice_cream.lrange_control.socket.DatabaseHelper;

import java.util.ArrayList;

public class HotActivity extends Fragment {
    public  long context_menu_idex;
    ListView hot_listview;
    EditText cmd_et;
    Button hot_add_bt;
//    ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> lists;
    public ArrayList<ArrayList<String>> list_orders;
    public String[] names;
    public ArrayList<String> orders;
    AppValues application;
    SQLiteDatabase db;
    Handler handler;
    Button add_bt;
    EditText add_et;
    ArrayList<String> cmd_name=new ArrayList<String>();
    ArrayList<String> cmd_list=new ArrayList<String>();
    ArrayList<Long> cmd_id=new ArrayList<Long>();
    int now_index;
    SimpleCursorAdapter simpleCursorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_edit_lay, container, false);
        cmd_name=new ArrayList<String>();
        cmd_list=new ArrayList<String>();
        hot_listview=view.findViewById(R.id.hot_listview);
        cmd_et=view.findViewById(R.id.cmd_et);
        hot_add_bt=view.findViewById(R.id.hot_add_bt);
        add_bt=view.findViewById(R.id.add_bt);
        add_et=view.findViewById(R.id.add_et);
        names=new String[]{"倚天屠龙记","斗鱼直播","延时关机"};
        list_orders=new ArrayList<ArrayList<String>>();


        ArrayList<String> yttlj=new ArrayList<String>();
        yttlj.add("cmd:cmd /c start www.baidu.com\\");
        yttlj.add("cps:倚天屠龙记");
        yttlj.add("key:vk_control+vk_v");
        yttlj.add("mva:1249,936");
        yttlj.add("dyd:2000");
        yttlj.add("clk:left");
        ArrayList<String> douyu=new  ArrayList<String>();
        douyu.add("cmd:cmd /c start www.douyu.com\\");
        ArrayList<String> xiumian=new  ArrayList<String>();
        xiumian.add("slp:5000");
        list_orders.add(yttlj);
        list_orders.add(douyu);
        list_orders.add(xiumian);


        lists=new ArrayList<String>();

        for(int i=0;i<names.length;i++){
            lists.add(names[i]);
        }

        application= (AppValues) getActivity().getApplication();
        application.loadData(getContext());
        IpData ipData=application.getIpData();


        db=new DatabaseHelper(getContext()).getWritableDatabase();
//        ContentValues cv=new ContentValues();
//        cv.put(DatabaseHelper.CMD_NAME,"斗鱼直播");
//        cv.put(DatabaseHelper.CMD_LIST,"cmd:cmd /c start www.douyu.com\\");
        final View inflate= LayoutInflater.from(getContext()).inflate(R.layout.add_cmd_lay,null);
        TextView add_cmd_tv=inflate.findViewById(R.id.add_cmd_tv);
        String sql="select  * from HOTDATA ";
        Cursor cursor=db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String   a=  cursor.getString(cursor.getColumnIndex("CMD_NAME"));
            String    b= cursor.getString(cursor.getColumnIndex("CMD_LIST"));
            long show_id=cursor.getInt(cursor.getColumnIndex("_id"));
            cmd_name.add(a);
            cmd_list.add(b);
            cmd_id.add(show_id);
        }
        System.out.println("wennjianming"+cmd_name);


        simpleCursorAdapter=new SimpleCursorAdapter(getContext(),R.layout.add_cmd_lay,cursor,new String[] {"CMD_NAME"},new int[] {R.id.add_cmd_tv});
//        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,cmd_name);

        hot_listview.setAdapter(simpleCursorAdapter);
        registerForContextMenu(hot_listview);




        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add_cmd_name=add_et.getText().toString();
                String add_cmd=cmd_et.getText().toString();
                ContentValues cv=new ContentValues();
                cv.put(DatabaseHelper.CMD_NAME,add_cmd_name);
                cv.put(DatabaseHelper.CMD_LIST,add_cmd);
                cmd_name.add(add_cmd);
                cmd_list.add(add_cmd_name);
                db.insert("HOTDATA","",cv);
                simpleCursorAdapter.getCursor().requery();
            }
        });




//        hot_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//
//            }
//        });

        hot_add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String str=cmd_et.getText().toString();
                final String[] data=str.split("\\n");
                for (int i = 0; i < data.length; i++) {
                    final int finalI = i;
                    String cmd = data[finalI].substring(0, data[finalI].indexOf(":"));// 命令类型
                    String all_str="";
                        all_str=data[finalI];
                        final String finalAll_str = all_str;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Looper.prepare();
                                System.out.println("----------执行："+data[finalI]);
                                ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler = new ShowNonUiUpdateCmdHandler(getContext());//直接Toast显示的句柄，不更新ListView
                                CmdClientSocket cmdClientSocket = new CmdClientSocket(application.ip, application.port, showNonUiUpdateCmdHandler);
                                cmdClientSocket.work(finalAll_str);
                                Looper.loop();
                            }
                        }).start();
                    }

            }
        });


        return  view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        new MenuInflater(getContext()).inflate(R.menu.hotcontext,menu);
        AdapterView.AdapterContextMenuInfo menuInflater=   (AdapterView.AdapterContextMenuInfo) menuInfo;

        now_index=menuInflater.position;
        long contextMenuInfo= ((AdapterView.AdapterContextMenuInfo) menuInfo).id;
        context_menu_idex=contextMenuInfo;
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.hot_del_item:
                deldata();
                break;
            case  R.id.hot_send_item:
                String sql="select  * from HOTDATA where _id= "+context_menu_idex;
                System.out.print(sql);
                Cursor cursor=db.rawQuery(sql,null);
                cursor.moveToFirst();
                String str0=cursor.getString(cursor.getColumnIndex("CMD_LIST"));
                String[] str1=str0.split("\n");
                for (int i = 0; i <str1.length ; i++) {
                    ShowNonUiUpdateCmdHandler showNonUiUpdateCmdHandler=new ShowNonUiUpdateCmdHandler(getContext());
                    CmdClientSocket cmdClientSocket = new CmdClientSocket( application.ip, application.port,showNonUiUpdateCmdHandler);
                    cmdClientSocket.work(str1[i]);
                }
                break;
            default:break;
        }
        return super.onContextItemSelected(item);
    }

    private void deldata() {
        String delete_sql = "delete from HOTDATA where _id=" + context_menu_idex;
        db.execSQL(delete_sql);
        simpleCursorAdapter.getCursor().requery();

    }
}
