package com.example.ice_cream.lrange_control.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ice_cream.lrange_control.R;
import com.example.ice_cream.lrange_control.data.NetFileData;
import java.util.ArrayList;

public class NetFileListAdapter  extends ArrayAdapter {
    Context context;
    ArrayList<NetFileData> netFileList;
    public NetFileListAdapter(Context context, ArrayList<NetFileData> netFileList)
    {
        super(context,android.R.layout.simple_list_item_1,netFileList);//
        this.context=context;
        this.netFileList=netFileList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_lay,null,false);
        }
        ImageView img=convertView.findViewById(R.id.imageView);
        TextView tv_name=convertView.findViewById(R.id.tv_name);
        TextView tv_date=convertView.findViewById(R.id.tv_date);
        TextView tv_size=convertView.findViewById(R.id.tv_size);

        if(netFileList.get(position).isDirectory()){
            img.setImageResource(R.drawable.pic1);
        }
        else {
            img.setImageResource(R.drawable.pic0);
        }
        tv_name.setText(netFileList.get(position).getFileName());
        Log.e("-------------test:", "******"+netFileList.get(position).getFileName());
        if(netFileList.get(position).getFileType()==2||netFileList.get(position).getFileType()==3){
            tv_date.setText("");
            tv_size.setText("");
        }else {
            tv_date.setText(netFileList.get(position).getFileModifiedDate());
            tv_size.setText(netFileList.get(position).getFileSizeStr());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return netFileList.size();
    }
}
