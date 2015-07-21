package com.example.deti.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.deti.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/21.
 */
public class LabelGridAdapter extends BaseAdapter {
    private Context context;
    private int index;
    private int pageItemCount;
    private int totalCount;
    private String[] label;
    private ArrayList<Integer> checkedPositionList = new ArrayList<Integer>();
    private Boolean[] isCheck;

    /**
     * @param context
     * @param position      the viewpager current index
     * @param pageItemCount the count of every page
     * @param label         the String need to be showed
     */
    public LabelGridAdapter(Context context, int position, int pageItemCount, String[] label) {
        this.context = context;
        this.index = position;
        this.pageItemCount = pageItemCount;
        this.label = label;
        isCheck = new Boolean[label.length];
        for (int j = 0; j < label.length; j++) {
            isCheck[j] = false;
        }
    }

    @Override
    public int getCount() {
        int size = label.length / pageItemCount + 1;
        //特殊，如果标签总数能被每页整除，肯定每页的数目都为固定count
        if (label.length % pageItemCount == 0) {
            return pageItemCount;
        } else {
            //不能被整除，看Index，要么是每页固定，要么是最后一页剩余的数目
            if (index == size - 1) {
                return label.length - pageItemCount * index;
            } else {
                return pageItemCount;
            }
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        TextView textView = new TextView(context);
//        textView.setText(label[position + pageItemCount * index]);
//        textView.setTextColor(R.color.black);
//        textView.setGravity(Gravity.CENTER);
//        textView.setBackgroundResource(R.drawable.label_bg);
//
//        return textView;

        GridItem gridItem;
        if (convertView == null) {
            gridItem = new GridItem();
            convertView = LayoutInflater.from(context).inflate(R.layout.label_grid_item, null);
            gridItem.labelText = (TextView) convertView.findViewById(R.id.grid_text_view);
            gridItem.checkedImage = (ImageView) convertView.findViewById(R.id.checked_image);
            convertView.setTag(gridItem);
        } else {
            gridItem = (GridItem) convertView.getTag();
        }
        if (gridItem == null) {
            return convertView;
        }
//对该viewPager的存储checkitem的位置遍历一遍，如果与当前刷新的view相同，则进行处理
        if (checkedPositionList != null) {
            if (checkedPositionList.size() == 0) {
                gridItem.checkedImage.setVisibility(View.GONE);
                isCheck[position] = false;
            } else if (checkedPositionList.size() != 0) {
                for (int j = 0; j < checkedPositionList.size(); j++) {
                    if (j == 0 && checkedPositionList.get(j) == position) {
                        isCheck[position] = false;
                    }
                    if (checkedPositionList.get(j) == position && isCheck[position] == false) {
//对list每一项遍历，属性为点上了，而且轮到该位置时候，图片才可见
                        gridItem.checkedImage.setVisibility(View.VISIBLE);
                        isCheck[position] = true;
                        break;
                    } else {
                        gridItem.checkedImage.setVisibility(View.GONE);
                        isCheck[position] = false;
                    }
                }
            }
        }
        gridItem.labelText.setText(label[position + pageItemCount * index]);

        return convertView;
    }

    class GridItem {
        TextView labelText;
        ImageView checkedImage;
    }
    //供外部调用，给每一个gridItem设置点击了图片打上勾.取消的图片去掉勾

    /**
     * @param position of the gridView
     */
    public void gridItemCheck(int position) {
        if (checkedPositionList.contains((Object) position)) {
            isCheck[position] = true;
            checkedPositionList.remove((Object) position);
        } else if (!checkedPositionList.contains(position)) {
            checkedPositionList.add(position);

        }
        notifyDataSetChanged();

    }
}
