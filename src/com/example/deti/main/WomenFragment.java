package com.example.deti.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.deti.R;
import com.example.deti.adapter.MyTaskItemAdapter;
import com.example.deti.io.Setting;
import com.example.deti.myTask.EvaluateActivity;
import com.example.deti.myTask.TaskDetail;
import com.example.deti.myTask.TaskDetailActivity;
import com.example.deti.parse.ToJsonLogin;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/20.
 */
public class WomenFragment extends Fragment  implements IMsgBack{
    private final static String Tag = WomenFragment.class.getSimpleName();
    private Context context;
    private String key;
    private TaskThread taskThread = new TaskThread();
    private Handler handler;
    private   ListView myTaskList;
    private ArrayList<Boolean> isReceiveds = new ArrayList<Boolean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        key = DumpMessage.getInstance().RegistryCallback(this);
        View inflateView = inflater.inflate(R.layout.demand_tab_layout, null);

        initView(inflateView);
        initHandler();
        initList();
        return inflateView;
    }
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case  Global.MSG_TASK_DETAIL:
                        ArrayList<TaskDetail>taskDetails =(ArrayList<TaskDetail>) msg.obj;
                     MyTaskItemAdapter myTaskItemAdapter = new MyTaskItemAdapter(context,taskDetails);
                     myTaskList.setAdapter(myTaskItemAdapter);
                        myTaskList.setOnItemClickListener(new OnItemChildClickListener());

                }
            }
        };
    }

    private class OnItemChildClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //position =i;
            if (null!=isReceiveds.get(i)&&isReceiveds.get(i)==true){
                Intent intent = new Intent(context, EvaluateActivity.class);
                intent.putExtra("position",i);

                startActivity(intent);
            }else if (null!=isReceiveds.get(i)&&isReceiveds.get(i)==false){
                Util.showToast(R.string.not_receive, context);
            }else {
                Util.showToast(R.string.unknown_error, context);
            }


        }
    }

    private void initView(View inflateView) {
        inflateView.findViewById(R.id.back).setVisibility(View.GONE);
        TextView title = (TextView) inflateView.findViewById(R.id.title);
        title.setText(R.string.my_task);
        myTaskList = (ListView) inflateView.findViewById(R.id.my_task_list);

      /*  MyTaskItemAdapter myTaskItemAdapter = new MyTaskItemAdapter(context);
        myTaskList.setAdapter(myTaskItemAdapter);*/
        TextView giveTask = (TextView) inflateView.findViewById(R.id.give_task);
        giveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetailActivity.class);
                startActivity(intent);
            }
        });
    }
     private void  initList(){
         if (Setting.getInstance().getUserName() !=null&&Setting.getInstance().getPassword()!=null){
             String urlStr = Global.SERVICE_URL + "login?";
             StringBuffer buffer = new StringBuffer(urlStr);
             try {
                 buffer.append("name=").append(URLEncoder.encode(Setting.getInstance().getUserName(), "utf-8")).append("&password=").append(Setting.getInstance().getPassword());
             } catch (UnsupportedEncodingException e1) {
                 e1.printStackTrace();
             }
             urlStr = buffer.toString();
            // taskThread.addTask(new HttpTask(urlStr, Global.MSG_TASK_DETAIL,this));
         }

     }
    @Override
    public void onDestroy(){
        Log.i(Tag, "onDestroy-->");
        super.onDestroy();
        if (taskThread != null) {
            taskThread.threadDestroy();
        }

        DumpMessage.getInstance().UnRegistryCallback(key);
        if (isReceiveds !=null){
            isReceiveds =null;
        }
    }
    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag,appMessage.toString());
        int  msgType = appMessage.getMsgType();
        //start 注册
        if (msgType == Global.MSG_TASK_DETAIL){
            ToJsonLogin toJsonLogin = new ToJsonLogin(appMessage.getMsg());
            ArrayList<TaskDetail> taskDetails = toJsonLogin.parseTaskDetail();
            for (int i=0;i<taskDetails.size();i++){
                isReceiveds.add(taskDetails.get(i).getIsReceived());
            }
            Message successMsg = handler.obtainMessage();
            successMsg.what =  Global.MSG_TASK_DETAIL;
            successMsg.obj = taskDetails;
            handler.sendMessage(successMsg);
        }
    }
}
