package com.example.deti.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.deti.R;
import com.example.deti.adapter.NewsAdapter;
import com.example.deti.db.DBManager;
import com.example.deti.db.NewsMessage;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;
import com.example.deti.util.Global;

import java.util.ArrayList;


/**
 *
 * Created by Administrator on 2015/1/20.
 */
public class MenFragment extends Fragment  implements IMsgBack{
    private final static String Tag = MenFragment.class.getSimpleName();
    private Context context;
    private String key;
    private  NewsAdapter newsAdapter;
    private  ListView newsListView;
    private Handler handler;
    private ArrayList<NewsMessage> newsContentList = new ArrayList<NewsMessage>();
    private DBManager dbManager;
    private boolean shouldResum = true;
    private View inflateView;


    private final int MSG_UPDATE_MESSAGE_LIST_VIEW = 10;
    private final  int  MSG_RESUME_LIST_VIEW =11;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.i(Tag,"onCreate-->" );
          super.onCreate(savedInstanceState);
       key = DumpMessage.getInstance().RegistryCallback(this);
        context = getActivity();
        dbManager = new DBManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(Tag,"onCreateView-->" );
        if (inflateView != null){
            ((ViewGroup)(inflateView.getParent())).removeView(inflateView);
            shouldResum = false;
            return inflateView;
        }
        inflateView = inflater.inflate(R.layout.news_tab_layout, null);
        newsContentList = dbManager.query();
        initView(inflateView);
        initHandler();
        return inflateView;
    }

    private void initHandler(){
        handler = new Handler(){
          @Override
        public void dispatchMessage(Message msg ){
              switch (msg.what){
                  case MSG_UPDATE_MESSAGE_LIST_VIEW:
                      newsAdapter.appendList((String)msg.obj);
                      break;
                  case MSG_RESUME_LIST_VIEW:
                      newsAdapter.resume();

              }
          }
        };
    }
    private void initView(View inflateView) {
        if ( newsAdapter== null || newsListView == null){

            newsAdapter = new NewsAdapter(context);
            newsListView = (ListView) inflateView.findViewById(R.id.news_list);
            newsListView.setAdapter(newsAdapter);
            if (newsContentList !=null){

            newsAdapter.setMsgList(newsContentList);


            }
        }

    }

    @Override
    public void onResume(){
        Log.i(Tag,"onResume-->" );
        super.onResume();
        /*if ( newsAdapter !=null){
            newsAdapter.setMsgList(newsContentList);
            Message msgResume = handler.obtainMessage();
            msgResume.what = MSG_RESUME_LIST_VIEW;
            handler.sendMessage(msgResume);
        }*/

    }
    @Override
    public void onPause(){
        Log.i(Tag,"onPause-->" );
        super.onPause();
       /* if (newsAdapter !=null){
            newsAdapter.pause();
        }*/
    }
    @Override
    public void onDestroy(){
        Log.i(Tag,"onDestroy-->" );
        super.onDestroy();
        dbManager.closeDB();

        DumpMessage.getInstance().UnRegistryCallback(key);
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        if (appMessage == null){
            return;
        }
        int msgType = appMessage.getMsgType();
        if (msgType == -1){
            return;
        }
       /* if (msgType == Global.MSG_NOTIFICATION_RECEIVED || msgType == Global.MSG_MESSAGE_RECEIVED){
            String msg = appMessage.getMsg();
            NewsMessage newsMessage = new NewsMessage(msg);
            dbManager.add(newsMessage);
            Message newsMsg = handler.obtainMessage();
            newsMsg.what = MSG_UPDATE_MESSAGE_LIST_VIEW;
            newsMsg.obj = msg;
            handler.sendMessage(newsMsg);

            Log.i(Tag,"newsFragment回调函数接收到了msg:" + msg);
        }*/

    }
}
