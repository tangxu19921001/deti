package com.example.deti.user;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/21.
 */
public class LabelViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> labelPagerList;

    public LabelViewPagerAdapter(ArrayList<View> labelPagerList) {
        this.labelPagerList = labelPagerList;

    }

    @Override
    public int getCount() {
        return labelPagerList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = labelPagerList.get(position);
        ViewGroup viewParent = (ViewGroup) view.getParent();
        if (viewParent != null) {
            viewParent.removeAllViews();
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
