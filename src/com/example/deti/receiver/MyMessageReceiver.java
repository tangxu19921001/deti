package com.example.deti.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.example.deti.main.DumpMessage;
import com.example.deti.util.AppMessage;
import com.example.deti.util.Global;


/**
 * Created by Administrator on 2015/2/2.
 */
public class MyMessageReceiver extends BroadcastReceiver {
    private final static String Tag = MyMessageReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i(Tag,"MyMessageReceiver--->" + intent.getAction() +"extras-->" + bundle.toString());
        //TODO 别名与标签
             if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.getAction()){
                 String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
              //   DumpMessage.getInstance().dispatch(new AppMessage(content, Global.MSG_MESSAGE_RECEIVED));
             //    DumpMessage.getInstance().dispatch(new AppMessage(content, Global.MSG_COUNT_UPDATE));

             }else if(JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.getAction()){
                 String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            //     DumpMessage.getInstance().dispatch(new AppMessage(content,Global.MSG_NOTIFICATION_RECEIVED));
            //     DumpMessage.getInstance().dispatch(new AppMessage(content, Global.MSG_COUNT_UPDATE));
             }

    }
}
