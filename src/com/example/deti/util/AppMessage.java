package com.example.deti.util;

/**
 * Created by Administrator on 2015/2/2.
 */
public class AppMessage {
    private int msgType;
    private String msg;

    public int getMsgType() {
        return msgType;
    }



    public String getMsg() {
        return msg;
    }



public AppMessage(String msg,int msgType){
    this.msg = msg;
    this.msgType = msgType;
}
}
