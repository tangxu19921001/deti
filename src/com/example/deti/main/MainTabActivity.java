package com.example.deti.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.example.deti.R;
import com.example.deti.SlidingMenu;
import com.example.deti.db.DBManager;
import com.example.deti.db.NewsMessage;
import com.example.deti.io.Setting;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;
import com.example.deti.util.Global;
import com.example.deti.util.SysApplication;
import com.example.deti.util.Util;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * main activity
 * Created by Administrator on 2015/1/20.
 */
public class MainTabActivity extends FragmentActivity implements IMsgBack {
    private String Tag = "MainTabActivity";

    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;
    private TabWidget mTabWidget;
    TextView newsNumber;
    Handler handler;
    private int count = 0;
    private String msgKey;

    //定义一个布局
    private LayoutInflater layoutInflater;
    private DBManager dbManager;
    //首页标题栏
    private ImageView fragmentImage;
    private TextView homeTitle;
    private LinearLayout homeSex;
    private TextView womenText;
    private TextView manText;

    private ImageView homeSetting;
    //侧滑布局
    private SlidingMenu slidingLayout;
    //点击事件
    private ImageView menuImage;



    //
    private static final int FLAG_HANDLE_MSG = 1;
    private int returnFragment = -1;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class, WomanFragment.class, MenFragment.class, ShoppingCarFragment.class, SettingFragment.class,};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_btn, R.drawable.tab_demand_btn, R.drawable.tab_men_btn, R.drawable.tab_shopping_car_btn,
            R.drawable.user_tab_btn};

    //Tab选项卡的文字
    private String textViewArray[] = {"首页", "女人", "男人", "购物车", "用户"};
    // public static final String MESSAGE_RECEIVED_ACTION = "com.example.userSetting.MESSAGE_RECEIVED_ACTION";
    // public static final String KEY_MESSAGE = "message";
    //  public static final String KEY_EXTRAS = "extras";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab);
        Setting.init(MainTabActivity.this);
        // JPushInterface.init(this);
        //  JPushInterface.setDebugMode(true);

        msgKey = DumpMessage.getInstance().RegistryCallback(this);
        SysApplication.getInstance().addActivity(this);
        dbManager = new DBManager(this);
        ArrayList<NewsMessage> newsContentList = new ArrayList<NewsMessage>();
        newsContentList = dbManager.query();
        count = newsContentList.size();

        initView();
        initHandler();
       /* initFilter();*/
        Message handleFlag = handler.obtainMessage();
        handleFlag.what = FLAG_HANDLE_MSG;
        handler.sendMessage(handleFlag);
    }

    /*private void initFilter(){
        MessageReceiver messageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_RECEIVED_ACTION);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(messageReceiver,intentFilter);
    }

 public class MessageReceiver extends BroadcastReceiver{

     @Override
     public void onReceive(Context context, Intent intent) {
         Log.i(Tag,intent.getAction().toString());
         if ( MESSAGE_RECEIVED_ACTION == intent.getAction()){
             String message = intent.getStringExtra(KEY_MESSAGE);
             String extra = intent.getStringExtra(KEY_EXTRAS);
             String Jmessage = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
             Log.i("....","message:"+ message + "extra"+extra+"Jmessage"+Jmessage);

         }
     }
 }*/


    //TODO 做完这个深度理解activity的生命周期
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        returnFragment= Setting.getInstance().getFragmentId();
        Log.i(Tag,returnFragment+"");
        mTabHost.setCurrentTab(returnFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        returnFragment= Setting.getInstance().getFragmentId();
        Log.i(Tag, returnFragment + "");
        mTabHost.setCurrentTab(returnFragment);
    }

    View.OnClickListener menuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            slidingLayout.toggle();
        }
    };

    /**
     * 初始化组件
     */
    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabWidget = mTabHost.getTabWidget();
        //TODO
        fragmentImage = (ImageView) findViewById(R.id.home_back);
        homeTitle = (TextView) findViewById(R.id.home_title);
        homeSex = (LinearLayout) findViewById(R.id.home_sex_choice);
        womenText = (TextView) findViewById(R.id.women);
        manText = (TextView) findViewById(R.id.man);
        homeSetting = (ImageView)findViewById(R.id.home_setting);
        homeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.startActivityAnim(MainTabActivity.this,MySettingActivity.class);

            }
        });
        homeSex.setVisibility(View.VISIBLE);
        homeTitle.setVisibility(View.GONE);
        womenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                womenText.setTextColor(R.color.pink);
                manText.setTextColor(R.color.black);
            }
        });
        manText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manText.setTextColor(R.color.pink);
                womenText.setTextColor(R.color.black);
            }
        });
        fragmentImage.setOnClickListener(menuClick);
        menuImage= (ImageView)findViewById(R.id.menu_back);
        menuImage.setOnClickListener(menuClick);
        //得到fragment的个数
        slidingLayout = (SlidingMenu)findViewById(R.id.id_menu);
        final int fragmentCount = fragmentArray.length;
        for (int i = 0; i < fragmentCount; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec;
            tabSpec = mTabHost.newTabSpec(textViewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
           /* //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);*/
            if (returnFragment != -1) {
                mTabHost.setCurrentTab(returnFragment);
            } else {
                mTabHost.setCurrentTab(0);
            }

        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String id) {
                Log.i(Tag, id);
                if (id.equals("首页")) {
                    if (homeTitle.getVisibility() == View.VISIBLE) {
                        homeTitle.setVisibility(View.GONE);
                    }
                    if (homeSex.getVisibility() == View.GONE) {
                        homeSex.setVisibility(View.VISIBLE);
                    }
                    if (fragmentImage.getVisibility() == View.GONE) {
                        fragmentImage.setVisibility(View.VISIBLE);
                    }
                    if (homeSetting.getVisibility() == View.VISIBLE) {
                        homeSetting.setVisibility(View.GONE);
                    }
                    Setting.getInstance().setFragmentId(0);
                }
                if(id.equals("女人")){
                    if (homeTitle.getVisibility() == View.GONE){
                                homeTitle.setVisibility(View.VISIBLE);
                            }
                            if (homeSex.getVisibility() ==View.VISIBLE){
                                homeSex.setVisibility(View.GONE);
                            }
                            if (fragmentImage.getVisibility() == View.GONE){
                                fragmentImage.setVisibility(View.VISIBLE);
                            }
                            if(homeSetting.getVisibility() == View.VISIBLE){
                                homeSetting.setVisibility(View.GONE);
                            }
                            homeTitle.setText(R.string.deti_women);
                    Setting.getInstance().setFragmentId(1);
                }
                if(id.equals("男人")){
                    if (homeTitle.getVisibility() == View.GONE){
                                homeTitle.setVisibility(View.VISIBLE);
                            }
                            if (homeSex.getVisibility() ==View.VISIBLE){
                                homeSex.setVisibility(View.GONE);
                            }
                            if (fragmentImage.getVisibility() == View.GONE){
                                fragmentImage.setVisibility(View.VISIBLE);
                            }
                            if(homeSetting.getVisibility() == View.VISIBLE){
                                homeSetting.setVisibility(View.GONE);
                            }
                            homeTitle.setText(R.string.deti_man);
                    Setting.getInstance().setFragmentId(2);
                }
                if (id.equals("购物车")){
                    //判断是否登录，登录后进入该页面。
                    if (Setting.getInstance().getUserPhone()==null){
                        if (homeTitle.getVisibility() == View.GONE){
                            homeTitle.setVisibility(View.VISIBLE);
                        }
                        if (homeSex.getVisibility() ==View.VISIBLE){
                            homeSex.setVisibility(View.GONE);
                        }
                        if (fragmentImage.getVisibility() == View.GONE){
                            fragmentImage.setVisibility(View.VISIBLE);
                        }
                        if(homeSetting.getVisibility() == View.VISIBLE){
                            homeSetting.setVisibility(View.GONE);
                        }
                        homeTitle.setText(R.string.shopping_car);
                        Intent intent =new Intent(MainTabActivity.this,LoginActivity.class);
                        intent.putExtra("fragment",3);
                        startActivity(intent);
                        MainTabActivity.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }else{
                        if (homeTitle.getVisibility() == View.GONE){
                            homeTitle.setVisibility(View.VISIBLE);
                        }
                        if (homeSex.getVisibility() ==View.VISIBLE){
                            homeSex.setVisibility(View.GONE);
                        }
                        if (fragmentImage.getVisibility() == View.GONE){
                            fragmentImage.setVisibility(View.VISIBLE);
                        }
                        if(homeSetting.getVisibility() == View.VISIBLE){
                            homeSetting.setVisibility(View.GONE);
                        }
                        homeTitle.setText(R.string.shopping_car);
                        Setting.getInstance().setFragmentId(3);
                    }

                }
                if (id.equals("用户")){
                    if (Setting.getInstance().getUserPhone()==null) {
                        if (homeTitle.getVisibility() == View.GONE){
                            homeTitle.setVisibility(View.VISIBLE);
                        }
                        if (homeSex.getVisibility() ==View.VISIBLE){
                            homeSex.setVisibility(View.GONE);
                        }
                        if (fragmentImage.getVisibility() == View.VISIBLE){
                            fragmentImage.setVisibility(View.GONE);
                        }
                        if(homeSetting.getVisibility() == View.GONE){
                            homeSetting.setVisibility(View.VISIBLE);
                        }
                        homeTitle.setText(R.string.my_home_page);
                        Intent intent = new Intent(MainTabActivity.this, LoginActivity.class);
                        intent.putExtra("fragment",4);
                        startActivity(intent);
                        MainTabActivity.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }else {
                        if (homeTitle.getVisibility() == View.GONE){
                            homeTitle.setVisibility(View.VISIBLE);
                        }
                        if (homeSex.getVisibility() ==View.VISIBLE){
                            homeSex.setVisibility(View.GONE);
                        }
                        if (fragmentImage.getVisibility() == View.VISIBLE){
                            fragmentImage.setVisibility(View.GONE);
                        }
                        if(homeSetting.getVisibility() == View.GONE){
                            homeSetting.setVisibility(View.VISIBLE);
                        }
                        homeTitle.setText(R.string.my_home_page);
                        Setting.getInstance().setFragmentId(4);
                    }

                }

            }

        });

    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case FLAG_HANDLE_MSG:

                        break;
                }
            }
        };
    }

    private void setFlag() {
        if (count == 0) {
            newsNumber.setVisibility(View.GONE);
        }
        newsNumber.setText(String.valueOf(count));
    }


    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgKey != null) {
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        SysApplication.getInstance().exit();
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        //TODO
        int msgType = appMessage.getMsgType();
//        if (msgType == Global.MSG_COUNT_UPDATE) {
//            count++;
//            Message handleFlag = handler.obtainMessage();
//            handleFlag.what = FLAG_HANDLE_MSG;
//            handler.sendMessage(handleFlag);
//        }

    }
}

