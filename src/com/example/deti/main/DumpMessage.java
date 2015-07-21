package com.example.deti.main;

import android.os.Handler;
import android.os.Message;

import java.util.*;


import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;

public class DumpMessage {
    private static DumpMessage instance = null;

    private HashMap<String, IMsgBack> mCallbackMap = null;
    private Object obj = new Object();
    private Handler mHandler;
    private final int DISPACH = 0x1;

    public static DumpMessage getInstance() {
        if (instance == null) {
            instance = new DumpMessage();
        }
        return instance;
    }

    private DumpMessage() {
        mCallbackMap = new HashMap<String, IMsgBack>();
        mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                if (msg == null || obj == null || mCallbackMap == null)
                    return;
                if (msg.what == DISPACH) {
                    synchronized (obj) {
                        if (!mCallbackMap.isEmpty()) {
                            Iterator<Map.Entry<String, IMsgBack>> it = mCallbackMap.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, IMsgBack> entry = it.next();
                                if (entry.getValue() != null)
                                    entry.getValue().onMsg((AppMessage) msg.obj);
                            }
                        }
                    }
                }
            }
        };
    }


    public void destroy() {
        synchronized (obj) {
            if (mCallbackMap != null)
                mCallbackMap.clear();
//			mCallbackMap = null;
        }
    }

    public String RegistryCallback(IMsgBack con) {
        synchronized (obj) {
            String key = generateUUID();
            while (mCallbackMap.containsKey(key))
                key = generateUUID();

            MessageObj mo = new MessageObj(key, con);
            mCallbackMap.put(mo.getKey(), mo.getCallback());
            return key;
        }
    }

    public void UnRegistryCallback(String key) {
        synchronized (obj) {
            if (mCallbackMap != null && mCallbackMap.size() > 0) {
                mCallbackMap.remove(key);
            }
        }
    }

    public int dispatch(AppMessage appMessage) {
        synchronized (obj) {
            Message hMsg = mHandler.obtainMessage(DISPACH);
            hMsg.what = DISPACH;
            hMsg.obj = appMessage;
            if (mHandler != null)
                mHandler.sendMessage(hMsg);
        }
        return 0;
    }

    class MessageObj {
        String key;
        IMsgBack callback;

        MessageObj(String key, IMsgBack callback) {
            this.key = key;
            this.callback = callback;
        }

        public String getKey() {
            return key;
        }

        public IMsgBack getCallback() {
            return callback;
        }
    }

    private String generateUUID() {
        String strUuid = "";
        Random rd = new Random(new Date().getTime());
        for (int i = 0; i < 32; i++) {
            char nibble = (char) (rd.nextInt() % 16);
            strUuid += (char) ((nibble < 10) ? ('0' + nibble) : ('a' + (nibble - 10)));
            if (i == 7 || i == 11 || i == 15 || i == 19) {
                strUuid += "-";
            }
        }
        return strUuid;
    }
}
