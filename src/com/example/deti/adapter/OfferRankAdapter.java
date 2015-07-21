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
 * Created by Administrator on 2015/1/22.
 */
public class OfferRankAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TaskDetail>taskDetails;

    public OfferRankAdapter(Context context,ArrayList<TaskDetail>taskDetails) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.offer_rank_item, null);

        }
        TextView notFinishedTaskName = (TextView)convertView.findViewById(R.id.notFinishedTaskName);
        TextView notFinishedTaskDetail = (TextView)convertView.findViewById(R.id.notFinishedTaskDetail);

        if (taskDetails !=null){
            String taskName = taskDetails.get(position).getTaskName();
            String taskContent = taskDetails.get(position).getTaskContent();
            notFinishedTaskName.setText(taskName);
            notFinishedTaskDetail.setText(taskContent);

        }
        return convertView;

    }
}
