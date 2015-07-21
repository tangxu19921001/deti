package com.example.deti.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;
import com.example.deti.R;
import com.example.deti.main.DumpMessage;
import com.example.deti.util.*;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author TX
 * create time 2015/1/15
 * Created by Administrator on 2015/1/14.
 */
public class Register extends Activity implements IMsgBack {

    private View indexView;
    //屏幕宽度，分辨率
    private int width;
    private float density;
    private int indexPaddingLeft;
    private RelativeLayout phoneNavigationLayout;
    private RelativeLayout userNameNavigationLayout;
    private EditText userNameEdit;
    private EditText passwordEdit;
    private TaskThread taskThread = new TaskThread();
    private Handler handler;
    private int backfragment;
    private String key;

    //index下标转换
    private static final int INDEX_NAVIGATION_PHONE = 0;
    private static final int INDEX_NAVIGATION_USER = 1;
    private int indexNavigation = INDEX_NAVIGATION_PHONE;
    //注册成功失败消息
    private static final int REGISTER_SUCCESS_MSG = 10;
    private static final int REGISTER_SANME_NAME_MSG = 11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        SysApplication.getInstance().addActivity(this);
        key = DumpMessage.getInstance().RegistryCallback(Register.this);
        initView();
        initHandler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {
        Context con = Register.this;
        DisplayMetrics metric = con.getResources().getDisplayMetrics();
        width = metric.widthPixels;     // 屏幕宽度（像素）
        density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        indexView = findViewById(R.id.index_view);
        TextView back = (TextView) findViewById(R.id.back);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(R.string.register_title);
        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        TextView users = (TextView) findViewById(R.id.userName);
        TextView register = (TextView) findViewById(R.id.users_register);
        userNameEdit = (EditText) findViewById(R.id.user_name_edit);
        passwordEdit = (EditText) findViewById(R.id.usr_password_edit);
        phoneNavigationLayout = (RelativeLayout) findViewById(R.id.phone_number_layout);
        userNameNavigationLayout = (RelativeLayout) findViewById(R.id.users_name_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNavigate();
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNavigate();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onRegisterPre();
            }
        });
        initIndex();
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case REGISTER_SUCCESS_MSG:
                        Util.showToast(R.string.register_success, Register.this);
                        Intent intent = new Intent(Register.this, LabelActivity.class);
                        backfragment = getIntent().getIntExtra("fragment", 3);
                        intent.putExtra("fragment", backfragment);
                        startActivity(intent);
                        break;
                    case REGISTER_SANME_NAME_MSG:
                        Util.showToast(R.string.user_exist, Register.this);
                        break;
                }
            }
        };
    }

    //准备注册
    private void onRegisterPre() {
        String count = userNameEdit.getText().toString().trim().toLowerCase(Locale.US);
        String password = passwordEdit.getText().toString().trim().toLowerCase(Locale.US);

        if (count.isEmpty() || password.isEmpty()) {
            Util.showToast(R.string.no_empty, this);
            return;
        }
        if (count.length() < 3 || count.length() > 16) {
            Util.showToast(R.string.count_rule_tip_msg, this);
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            Util.showToast(R.string.password_rule_tip_msg, this);
            return;
        }

        //判断是否由空格
        if (count.indexOf(" ") != -1 || password.indexOf(" ") != -1) {
            Util.showToast(R.string.no_space, this);
            return;
        }
        String compileString = getCompiledString(count);
        if (!compileString.isEmpty()) {
            Util.showToast(R.string.character_rule_tip_msg, this);
            return;
        }
        if (isNumberAll(password)) {
            Util.showToast(R.string.password_tip_no_all_number, this);
            return;
        }
        register(count, password);
    }

    private void register(String count, String password) {
        String urlStr = Global.SERVICE_URL + "register?";
        StringBuffer buffer = new StringBuffer(urlStr);
        try {
            buffer.append("name=").append(URLEncoder.encode(count, "utf-8")).append("&password=").append(password);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        urlStr = buffer.toString();
       // taskThread.addTask(new HttpTask(urlStr,Global.MSG_REGISTER,this));

    }



    //判断密码是否全是数字
    private boolean isNumberAll(String compileText) {
        String numberString = "[0-9]";
        Pattern pattern = Pattern.compile(numberString);
        Matcher matcher = pattern.matcher(compileText);
        String emptyIsRight = matcher.replaceAll("").trim();
        return emptyIsRight.isEmpty();
    }

    //将字符串规范化
    private String getCompiledString(String model) {
        //匹配中文:[\u4e00-\u9fa5]英文字母:[a-zA-Z]数字:[0-9]匹配中文，英文字母和数字及_:
        //  ^[\u4e00-\u9fa5_a-zA-Z0-9]+$
        String stringPre = "[//^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$]";
        Pattern pattern = Pattern.compile(stringPre);
        Matcher matcher = pattern.matcher(model);
        return matcher.replaceAll("").trim();
    }

    //用化名切换
    private void userNavigate() {
        phoneNavigationLayout.setVisibility(View.GONE);
        userNameNavigationLayout.setVisibility(View.VISIBLE);
        indexNavigation = INDEX_NAVIGATION_USER;
        initIndex();
    }

    //手机号切换
    private void phoneNavigate() {
        phoneNavigationLayout.setVisibility(View.VISIBLE);
        userNameNavigationLayout.setVisibility(View.GONE);
        indexNavigation = INDEX_NAVIGATION_PHONE;
        initIndex();
    }

    private void initIndex() {
        if (indexNavigation == INDEX_NAVIGATION_PHONE) {
            indexPaddingLeft = (int) (width / 2 - 100 * density) / 2;
        } else if (indexNavigation == INDEX_NAVIGATION_USER) {
            indexPaddingLeft = (int) ((width / 2 * 3 - 100 * density) / 2);
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) indexView.getLayoutParams();
        params.setMargins(indexPaddingLeft, 0, 0, 0);
        indexView.setLayoutParams(params);
    }

    @Override
    public void onDestroy(){
        if (taskThread != null){
            taskThread.threadDestroy();
        }
        if (key != null) {
            DumpMessage.getInstance().UnRegistryCallback(key);
        }
        super.onDestroy();
    }
    @Override
    public void onMsg(AppMessage appMessage) {
        int type = appMessage.getMsgType();
        String msg = appMessage.getMsg();
        if ( type == Global.MSG_REGISTER){
            Message successMsg = handler.obtainMessage();

            if (msg != null && msg.equals(getString(R.string.register_success))) {
                successMsg.what = REGISTER_SUCCESS_MSG;
                handler.sendMessage(successMsg);
            }else
            {
                successMsg.what = REGISTER_SANME_NAME_MSG;
                handler.sendMessage(successMsg);
            }
        }

    }
}
