package com.example.deti.user;

import com.example.deti.util.AppMessage;

/**
 * Created by Administrator on 2015/1/17.
 * <p/>
 * when you want handle UI in thread，you can use IMsgBack，you should override onMsg（）
 */
public interface IMsgBack {
    public void onMsg(AppMessage msg);
}
