package com.example.deti.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deti.R;
import com.example.deti.entity.Custom;
import com.example.deti.entity.Design;
import com.example.deti.entity.Designer;
import com.example.deti.entity.DesignerList;
import com.example.deti.main.DumpMessage;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;
import com.example.deti.util.Global;
import com.example.deti.util.HttpTask;
import com.example.deti.util.TaskThread;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/20.
 */
public class GoodsDetailActivity extends Activity implements IMsgBack {
    private static final String Tag = GoodsDetailActivity.class.getSimpleName();
    private String msgKey;
    private Design defaultDesign;
    private int designId;
    private TaskThread taskThread;
    private Handler handler;
    private TextView titleText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail_layout);
        msgKey = DumpMessage.getInstance().RegistryCallback(GoodsDetailActivity.this);
        Intent intent = this.getIntent();
        defaultDesign = (Design)intent.getSerializableExtra("design");
        init();
        initHandler();
    }

    private void init(){
        titleText = (TextView)findViewById(R.id.title);


        if (taskThread==null){
            taskThread = new TaskThread();
        }
        HashMap<String,String>hashMap = new HashMap<>();
        hashMap.put("designId", defaultDesign.getId() + "");
        taskThread.addTask(new HttpTask(Global.GET_SINGLE_DESIGN_INGO_URL, Global.MSG_GET_DESIGN_INFO, GoodsDetailActivity.this, hashMap));
    }

    private void initHandler(){
//        handler = new Handler() {
//            @Override
//            public void dispatchMessage(Message msg) {
//                switch (msg.what) {
//                    case Global.MSG_GET_DESIGN_INFO:
//                        Custom custom
//                        if (msg.obj!=null) {
//                             custom = (Custom) msg.obj;
//                            titleText.setText(custom.getName());
//                        }
//                        Toast.makeText(GoodsDetailActivity.this, R.string.cancel_follow_success, Toast.LENGTH_LONG).show();
//                        isFollowed = false;
//                        break;
//
//                    case Global.MSG_REQUEST_WRONG:
//                        Toast.makeText(GoodsDetailActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        };
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(msgKey!=null){
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
        if (taskThread!=null){
            taskThread.threadDestroy();
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }
    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        JsonParse jsonParse = new JsonParse();
        //start ???
        switch (msgType){
            case Global.MSG_GET_DESIGN_INFO:
                Log.i(Tag,appMessage.getMsg());
                //TODO 此页所有数据都在custom类中。
                Custom custom = jsonParse.getPerson(appMessage.getMsg(), Custom.class);
                if (custom.isResult() == true) {
                    successMsg.what = Global.MSG_GET_DESIGN_INFO;
                    successMsg.obj = custom;
                    handler.sendMessage(successMsg);
                } else {
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = custom.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;
    }
}
}