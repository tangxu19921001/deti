package com.example.deti.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.deti.R;
import com.example.deti.adapter.OfferRankAdapter;
import com.example.deti.adapter.SuperManRankAdapter;
import com.example.deti.io.Setting;
import com.example.deti.myTask.TaskDetail;
import com.example.deti.parse.ToJsonLogin;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/20.
 */
public class HomeFragment extends Fragment implements IMsgBack {
    private static final String Tag = HomeFragment.class.getSimpleName();
    private ListView womenList;
    private ListView manList;
    /*private View indexView;
    private TextView offerRankText;
    private TextView superManRankText;
    private LinearLayout describe;*/
    private int width;
    private float density;
    private int indexPaddingLeft;
    private String msgKey;

    //index下标转换
    private static final int INDEX_NAVIGATION_OFFER = 0;
    private static final int INDEX_NAVIGATION_SUPER = 1;
    private int indexNavigation = INDEX_NAVIGATION_OFFER;
    private TaskThread taskThread = new TaskThread();
    private Handler handler;
    private Context context;
    private ArrayList<Integer> taskIds = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.home_tab_layout, null);
        womenList = (ListView) inflateView.findViewById(R.id.womenList);
        manList = (ListView) inflateView.findViewById(R.id.manList);
       /* indexView = inflateView.findViewById(R.id.index_view);
        describe = (LinearLayout) inflateView.findViewById(R.id.describe);
        offerRankText = (TextView) inflateView.findViewById(R.id.offer_rank_text);
        superManRankText = (TextView) inflateView.findViewById(R.id.super_man_rank_text);*/
        context = this.getActivity();
       /* initHandler();
        init();
        msgKey = DumpMessage.getInstance().RegistryCallback(HomeFragment.this);
        initList();*/


        return inflateView;
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case Global.MSG_GET_TASK_LIST:
                        ArrayList<TaskDetail> taskDetails = (ArrayList<TaskDetail>) msg.obj;
                        OfferRankAdapter offerRankAdapter = new OfferRankAdapter(getActivity(),taskDetails);
                        womenList.setAdapter(offerRankAdapter);
                        womenList.setOnItemClickListener(new OnItemChildClickListener());
                        break;
                    case Global.MSG_ACCEPT_SUCCESS:
                        if (msg.arg1 ==1){
                            Util.showToast(R.string.accept_success, context);
                        }else {
                            Util.showToast(R.string.accept_fail, context);
                        }
                        break;

                }
            }
        };
    }

    private class OnItemChildClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //position =i;

            if (Setting.getInstance().getUserName() !=null&&Setting.getInstance().getPassword()!=null){
                String urlStr = Global.SERVICE_URL + "accept?";
                StringBuffer buffer = new StringBuffer(urlStr);
                String r = Setting.getInstance().getUserId() +"";

                buffer.append("taskId=").append(taskIds.get(i).toString()).append("&receiverId=").append(r);
                urlStr = buffer.toString();
                /*taskThread.addTask(new HttpTask(urlStr, Global.MSG_ACCEPT_SUCCESS,HomeFragment.this));*/
            }

        }
    }

    private void initList() {

        String urlStr = Global.SERVICE_URL + "getUnfinishedTask";
       /* taskThread.addTask(new HttpTask(urlStr, Global.MSG_GET_TASK_LIST, this));*/
    }

    private void init() {
        Context con = getActivity();
        DisplayMetrics metric = con.getResources().getDisplayMetrics();
        width = metric.widthPixels;     // 屏幕宽度（像素）
        density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）

        SuperManRankAdapter superManRankAdapter = new SuperManRankAdapter(con);

        manList.setAdapter(superManRankAdapter);
//        offerRankText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                womenList.setVisibility(View.VISIBLE);
//                manList.setVisibility(View.GONE);
//                describe.setVisibility(View.VISIBLE);
//                indexNavigation = INDEX_NAVIGATION_OFFER;
//                initIndex();
//            }
//        });
//
//        superManRankText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                manList.setVisibility(View.VISIBLE);
//                womenList.setVisibility(View.GONE);
//                describe.setVisibility(View.GONE);
//                indexNavigation = INDEX_NAVIGATION_SUPER;
//                initIndex();
//            }
//        });
//
//        initIndex();
    }

 /*   private void initIndex() {
        if (indexNavigation == INDEX_NAVIGATION_OFFER) {
            indexPaddingLeft = (int) (width / 2 - 100 * density) / 2;
        } else if (indexNavigation == INDEX_NAVIGATION_SUPER) {
            indexPaddingLeft = (int) ((width / 2 * 3 - 100 * density) / 2);
        }

       *//* LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) indexView.getLayoutParams();
        params.setMargins(indexPaddingLeft, 0, 0, 0);
        indexView.setLayoutParams(params);*//*
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (taskThread != null) {
            taskThread.threadDestroy();
        }
        if (msgKey != null) {
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
        if (taskIds!= null){
            taskIds =null;
        }
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int msgType = appMessage.getMsgType();
        //start 注册
        if (msgType == Global.MSG_GET_TASK_LIST) {
            ToJsonLogin toJsonLogin = new ToJsonLogin(appMessage.getMsg());
            ArrayList<TaskDetail> taskDetails = toJsonLogin.getUnFinishedTask();
            for(int i=0;i<taskDetails.size();i++){
                taskIds.add(taskDetails.get(i).getTaskId());
            }
            Message successMsg = handler.obtainMessage();

            successMsg.what = Global.MSG_GET_TASK_LIST;
            successMsg.obj = taskDetails;
            handler.sendMessage(successMsg);

        }
        if (msgType == Global.MSG_ACCEPT_SUCCESS){
            Message successMsg = handler.obtainMessage();

            successMsg.what = Global.MSG_ACCEPT_SUCCESS;
            successMsg.arg1 = Integer.parseInt(appMessage.getMsg());
            handler.sendMessage(successMsg);
        }
    }
}