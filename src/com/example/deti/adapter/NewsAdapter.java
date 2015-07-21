package com.example.deti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.deti.R;
import com.example.deti.db.NewsMessage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/24.
 */
public class NewsAdapter extends BaseAdapter {
    private Context context;
    private int count = 0;
    private ArrayList<String> adapterMsgList = new ArrayList<String>();

    public NewsAdapter(Context context) {
        this.context = context;
    }

    //由这里更新item
    public void appendList(String msg){
        count ++;
        adapterMsgList.add(msg);
        notifyDataSetChanged();
    }

    public void setMsgList(ArrayList<NewsMessage> msgContent){
        for (int i= 0;i<msgContent.size();i++){
            NewsMessage newsMessage =msgContent.get(i);
            adapterMsgList.add(newsMessage.content);
        }

        count = msgContent.size();
        notifyDataSetChanged();
    }
    public void resume(){

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return count;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.news_list_item, null);
        TextView textView =(TextView) convertView.findViewById(R.id.content);
        if (adapterMsgList !=null){
            textView.setText(adapterMsgList.get(position));
        }
        return convertView;
    }
}
