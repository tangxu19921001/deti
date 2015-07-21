package com.example.deti.user;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.deti.R;
import com.example.deti.util.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/20.
 */
public class LabelLayout extends LinearLayout {

    private Context context;
    private ViewPager viewPager;
    private LinearLayout dotLinearLayout;
    private ImageView[] dots;
    //ViewPager页数
    private int viewPager_size;
    //默认一页12个item
    private int pageItemCount = 12;
    //每个页面的gridView视图
    private ArrayList<View> list_Views;
    //设置当前页
    private int currentIndex;

    private String[] label;
    //记录被选择的label的 position,传送给后台
    /**
     * TODO后台
     */

    private ArrayList<Integer> checkedPositionList = new ArrayList<Integer>();

    public LabelLayout(Context context) {
        super(context);
        this.context = context;
        initView();
        initDots();
        setAdapter();
    }

    public LabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initDots();
        setAdapter();
    }

    public LabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
        initDots();
        setAdapter();
    }

    /**
     * TODO后台
     */

    public ArrayList<Integer> getCheckedLabelList() {
        return checkedPositionList;
    }

    private void setAdapter() {
        list_Views = new ArrayList<View>();
        for (int i = 0; i < viewPager_size; i++) {
            list_Views.add(getViewPagerItem(i));
        }
        viewPager.setAdapter(new LabelViewPagerAdapter(list_Views));
    }

    private void initView() {
        label = context.getResources().getStringArray(R.array.label_array);
        View view = LayoutInflater.from(context).inflate(R.layout.grid_view_view_pager, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        dotLinearLayout = (LinearLayout) view.findViewById(R.id.dots);
        addView(view);
    }

    private void initDots() {
        //根据屏幕宽度高度计算pageItemCount
        int width = Util.getWindowWidth(context);
        int height = Util.getWindowHeight(context);
        int col = (width / 160) > 2 ? (width / 160) : 3;
        int row = (height / 200) > 4 ? (height / 200) : 4;

        pageItemCount = col * row;
        viewPager_size = label.length / pageItemCount + 1;

        if (0 < viewPager_size) {
            dotLinearLayout.removeAllViews();
            if (1 == viewPager_size) {
                dotLinearLayout.setVisibility(GONE);
            } else if (1 < viewPager_size) {
                for (int j = 0; j < viewPager_size; j++) {
                    ImageView image = new ImageView(context);
                    LinearLayout.LayoutParams params = new LayoutParams(10, 10);
                    params.setMargins(3, 0, 3, 0);
                    image.setImageResource(R.drawable.pager_dot);
                    dotLinearLayout.addView(image, params);

                }
            }
            if (viewPager_size != 1) {
                dots = new ImageView[viewPager_size];
                for (int i = 0; i < viewPager_size; i++) {
                    dots[i] = (ImageView) dotLinearLayout.getChildAt(i);
                    dots[i].setEnabled(true);
                    dots[i].setTag(i);

                }
                currentIndex = 0;
                dots[currentIndex].setEnabled(false);
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                        setCurDot(arg0);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        }

    }

    private void setCurDot(int position) {
        if (position < 0 || position > viewPager_size - 1 || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    //利用gridview实现viewpager的详情页
    private View getViewPagerItem(final int pagerIndex) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pager_detail, null);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        int width = Util.getWindowWidth(context);
        int col = (width / 160) > 2 ? (width / 160) : 3;
        gridView.setNumColumns(col);
        //上下文,第几页，总数，每页的item多少
        final LabelGridAdapter labelGridAdapter = new LabelGridAdapter(context, pagerIndex, pageItemCount, label);
        gridView.setAdapter(labelGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //添加被选中的选项,如果添加了，再点击，说明是取消
                int totalPosition = position + pageItemCount * pagerIndex;
                if (checkedPositionList != null && !checkedPositionList.contains(totalPosition)) {
                    checkedPositionList.add(totalPosition);
                } else {

                    if (checkedPositionList != null) {
                        if (checkedPositionList.contains(totalPosition)) {
                            checkedPositionList.remove(totalPosition);
                        }
                    }
                }
                labelGridAdapter.gridItemCheck(position);
            }
        });
        return gridView;

    }


}
