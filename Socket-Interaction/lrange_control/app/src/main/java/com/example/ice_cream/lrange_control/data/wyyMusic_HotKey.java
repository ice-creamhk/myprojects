package com.example.ice_cream.lrange_control.data;

import java.util.ArrayList;

public class wyyMusic_HotKey extends BaseHotKey {
    @Override
    ArrayList<HotKeyData> generateData() {
        ArrayList<HotKeyData> hotKeyList=new ArrayList<HotKeyData>();
        hotKeyList.add(new HotKeyData("暂停","key:VK_CONTROL+VK_ALT+VK_P"));
        hotKeyList.add(new HotKeyData("mini/完整模式","key:VK_CONTROL+VK_ALT+VK_M"));
        hotKeyList.add(new HotKeyData("上一首","key:VK_CONTROL+VK_ALT+VK_LEFT"));
        hotKeyList.add(new HotKeyData("下一首","key:VK_CONTROL+VK_ALT+VK_RIGHT"));
        hotKeyList.add(new HotKeyData("音量加","key:VK_CONTROL+VK_ALT+VK_UP"));
        hotKeyList.add(new HotKeyData("音量减","key:VK_CONTROL+VK_ALT+VK_DOWN"));
        return hotKeyList;
    }
}
