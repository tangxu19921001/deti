package com.example.deti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.deti.R;
import com.example.deti.myTask.TaskDetail;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/23.
 */
public class MyTaskItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TaskDetail>taskDetails;

    public MyTaskItemAdapter(Context context,ArrayList<TaskDetail>taskDetails) {
        this.context = context;
        this.taskDetails = taskDetails;
    }

    @Override
    public int getCount() {
        return taskDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.my_task_item, null);

        }
        TextView timeTextView =(TextView) convertView.findViewById(R.id.time);
        TextView rewardTextView = (TextView)convertView.findViewById(R.id.reward);
        TextView nameTextView =( TextView)convertView.findViewById(R.id.taskName);
        TextView troubleTextView = (TextView)convertView.findViewById(R.id.trouble);

        if (taskDetails !=null){
            String time = taskDetails.get(position).getTaskTime();
            int taskMoney = taskDetails.get(position).getTaskMoney();
            String taskName = taskDetails.get(position).getTaskName();
            String taskContent = taskDetails.get(position).getTaskContent();

            timeTextView.setText(time +"");
            rewardTextView.setText(taskMoney+"");
            nameTextView.setText(taskName);
            troubleTextView.setText(taskContent);
        }
        return convertView;


    }
}
