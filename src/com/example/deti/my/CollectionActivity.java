package com.example.deti.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deti.R;
import com.example.deti.entity.Person;
import com.example.deti.io.Setting;
import com.example.deti.main.MainTabActivity;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/17.
 */
public class CollectionActivity extends Activity implements IMsgBack {
    private TextView title;
    private ImageView backImage;
    private ListView listView;
    private TaskThread taskThread;
    private Handler handler;

    private static final String Tag = CollectionActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);
        init();
        initHandler();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (taskThread == null) {
            taskThread = new TaskThread();
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cellphone", Setting.getInstance().getUserPhone());
        //TODO 如果分页做，这里要改
        Integer integer = 1;
        Integer pageSize = 99;
        hashMap.put("pageIndex", integer.toString());
        hashMap.put("pageSize", pageSize.toString());
        taskThread.addTask(new HttpTask(Global.GET_FAVORITE_DESIGN, Global.MSG_FAVORITE, CollectionActivity.this, hashMap));

    }

    private void init() {
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.my_collection);
        backImage = (ImageView) findViewById(R.id.title_image_back);
        backImage.setOnClickListener(menuClick);
        listView = (ListView) findViewById(R.id.my_collection_list);
//        listView.setAdapter();
    }

    View.OnClickListener menuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case Global.MSG_FAVORITE:

                        break;
                    case Global.MSG_REQUEST_WRONG:

                        Toast.makeText(CollectionActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        //start 注册
        if (msgType == Global.MSG_FAVORITE) {
            JsonParse jsonParse = new JsonParse();
            Person person1 = jsonParse.getPerson(appMessage.getMsg(), Person.class);

            if (person1.getResult() == true) {
                successMsg.what = Global.MSG_FAVORITE;
                handler.sendMessage(successMsg);
            } else {
                successMsg.what = Global.MSG_REQUEST_WRONG;
                successMsg.obj = person1.getMessage();
                handler.sendMessage(successMsg);
            }

        }
    }
}