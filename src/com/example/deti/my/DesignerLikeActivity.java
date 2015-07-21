package com.example.deti.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.deti.R;
import com.example.deti.entity.Designer;
import com.example.deti.entity.DesignerList;
import com.example.deti.entity.Person;
import com.example.deti.io.Setting;
import com.example.deti.main.DumpMessage;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;
import com.example.deti.util.Global;
import com.example.deti.util.HttpTask;
import com.example.deti.util.TaskThread;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/7/18.
 */
public class DesignerLikeActivity  extends Activity implements IMsgBack {
    private TextView title;
    private ImageView backImage;
    private ListView listView;
    private TaskThread taskThread;
    private Handler handler;
    private List<Designer> designerAdapterList;
    private ImageLoader imageLoader;
    private String msgKey;
    private static final String Tag = DesignerLikeActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.designer_like_layout);
        msgKey = DumpMessage.getInstance().RegistryCallback(DesignerLikeActivity.this);
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
        //TODO �����ҳ��������Ҫ��
        Integer integer = 1;
        Integer pageSize = 99;
        hashMap.put("pageIndex", integer.toString());
        hashMap.put("pageSize", pageSize.toString());
        taskThread.addTask(new HttpTask(Global.GET_FAVORITE_DESIGNER_URL, Global.MSG_MY_LIKE_DESIGNER, DesignerLikeActivity.this, hashMap));

    }

    private void init() {
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.my_like_designer);
        backImage = (ImageView) findViewById(R.id.title_image_back);
        backImage.setOnClickListener(menuClick);
        listView = (ListView) findViewById(R.id.my_designer_like_list);
        imageLoader=ImageLoader.getInstance();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (designerAdapterList != null) {
                    Designer designer = designerAdapterList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(DesignerLikeActivity.this, DesignerDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("designer", designer);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


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
                    case Global.MSG_MY_LIKE_DESIGNER:
                        //TODO ��ListView����������
                        DesignerLikeAdapter designerLikeAdapter = new DesignerLikeAdapter(designerAdapterList,DesignerLikeActivity.this,imageLoader);
                        listView.setAdapter(designerLikeAdapter);


                        break;
                    case Global.MSG_REQUEST_WRONG:

                        Toast.makeText(DesignerLikeActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();

                }
            }
        };
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageLoader!=null) {
            imageLoader.clearMemoryCache();
            imageLoader.clearDiscCache();
        }
        if (taskThread!=null){
            taskThread.threadDestroy();
        }
        if (msgKey !=null){
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        //start ע��
        if (msgType == Global.MSG_MY_LIKE_DESIGNER) {
            JsonParse jsonParse = new JsonParse();
            Log.i(Tag,appMessage.getMsg());
            DesignerList designerList = jsonParse.getPerson(appMessage.getMsg(), DesignerList.class);

            if (designerList.isResult() == true) {
                designerAdapterList = designerList.getUserList();
                successMsg.what = Global.MSG_MY_LIKE_DESIGNER;

                handler.sendMessage(successMsg);
            } else {
                successMsg.what = Global.MSG_REQUEST_WRONG;
                successMsg.obj = designerList.getMessage();
                handler.sendMessage(successMsg);
            }

        }
    }
}
