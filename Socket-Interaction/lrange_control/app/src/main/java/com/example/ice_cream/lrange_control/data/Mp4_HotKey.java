package com.example.ice_cream.lrange_control.data;

import java.util.ArrayList;

public class Mp4_HotKey extends BaseHotKey {
    @Override
    ArrayList<HotKeyData> generateData() {
        ArrayList<HotKeyData> hotKeyList=new ArrayList<HotKeyData>();
        hotKeyList.add(new HotKeyData("暂停","key:VK_SPACE"));
        hotKeyList.add(new HotKeyData("ESC","key:VK_ESCAPE"));
        hotKeyList.add(new HotKeyData("全屏/退出全屏","key:VK_ENTER"));
        hotKeyList.add(new HotKeyData("快进","key:VK_CONTROL+VK_ALT+VK_RIGHT"));
        hotKeyList.add(new HotKeyData("快退","key:VK_CONTROL+VK_ALT+VK_LEFT"));
        hotKeyList.add(new HotKeyData("增加音量","key:VK_UP"));
        hotKeyList.add(new HotKeyData("减小音量","key:VK_DOWN"));
        hotKeyList.add(new HotKeyData("加速播放","key:VK_CONTROL+VK_1"));
        hotKeyList.add(new HotKeyData("减速播放","key:VK_CONTROL+VK_0"));
        hotKeyList.add(new HotKeyData("下载","dlf:"));
        return hotKeyList;
    }
}
