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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.deti.R;
import com.example.deti.io.Setting;
import com.example.deti.user.IMsgBack;
import com.example.deti.user.Register;
import com.example.deti.util.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFragment extends Fragment implements IMsgBack {
    private static final String Tag = UserFragment.class.getSimpleName();

    //用来保存登录用 输入的用户名和密码
    private EditText userNameEdit;
    private EditText passwordEdit;
    private TextView loginSuccessName;
    private RelativeLayout loginRegisterLayout;
    private Button loginOut;
    private TextView moneyTextView;
    private TextView achievementTextView;

    private TaskThread taskThread = new TaskThread();
    private Handler handler;
    private Context con;
    private Context context;
    //登录成功
    private static final int LOGIN_SUCCESS_MSG = 10;
    private static final int LOGIN_WRONG_MSG = 11;

    //fragment以来的activity注册
    private String msgKey;
    private String cellphone;

    /**
     * Author TX
     * create time 2015/1/15
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.user_setting_main, null);
        con = this.getActivity();
        context = this.getActivity();
        msgKey = DumpMessage.getInstance().RegistryCallback(UserFragment.this);
        initView(inflateView);
        initHandler();
        return inflateView;
    }

    private void initView(View inflateView) {

        userNameEdit = (EditText) inflateView.findViewById(R.id.user_name_edit);
        passwordEdit = (EditText) inflateView.findViewById(R.id.usr_password_edit);
        Button loginButton = (Button) inflateView.findViewById(R.id.login);
        loginSuccessName = (TextView) inflateView.findViewById(R.id.login_success_name);
        loginRegisterLayout = (RelativeLayout) inflateView.findViewById(R.id.register_login_layout);
        loginOut = (Button) inflateView.findViewById(R.id.login_out);
        moneyTextView = (TextView)inflateView.findViewById(R.id.money);
        achievementTextView = (TextView)inflateView.findViewById((R.id.achievement));
        Button registerButton = (Button) inflateView.findViewById(R.id.register);
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOut();
            }
        });
        //暂时TODO
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 login(getUserName(), getPassword());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentRegister();
            }
        });
        File file= new File(Global.SHAREDPREFERENCE_URL);
        String userNameFromS = Setting.getInstance().getUserName();
        int loginMoney = Setting.getInstance().getMoney();
        int loginAchievement = Setting.getInstance().getAchievement();
        if(file.exists() && userNameFromS!=null){
            loginRegisterLayout.setVisibility(View.INVISIBLE);
            loginSuccessName.setVisibility(View.VISIBLE);
            loginOut.setVisibility(View.VISIBLE);
            moneyTextView.setText(""+loginMoney);
            achievementTextView.setText(""+loginAchievement);

            loginSuccessName.setText(userNameFromS);
            closeSoftkeyBoard(userNameEdit);
        }

        }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case LOGIN_SUCCESS_MSG:
                        Util.showToast(R.string.login_success, con);
                        login();
                        showMoneyAchievement(msg.arg1,msg.arg2);
                        Setting.getInstance().setUserName(getUserName());
                        Setting.getInstance().setPassword(getPassword());
                        Setting.getInstance().setUserMoney(msg.arg1);
                        Setting.getInstance().setUserAchievement(msg.arg2);
                        break;
                    case LOGIN_WRONG_MSG:
                        Util.showToast(R.string.wrong_count_password, con);

                }
            }
        };
    }
    //跳转到注册页面

    private void showMoneyAchievement(int money,int achievement){
         moneyTextView.setText(money + "");
        achievementTextView.setText(achievement + "");
    }

    private void intentRegister() {
        Intent intent = new Intent(con, Register.class);
        intent.putExtra("fragment", 3);
        startActivity(intent);
    }

    //登出，有些组件重新显示，有些消失
    private void loginOut() {
        loginRegisterLayout.setVisibility(View.VISIBLE);
        loginSuccessName.setVisibility(View.GONE);
        loginOut.setVisibility(View.GONE);
        closeSoftkeyBoard(userNameEdit);
        Setting.getInstance().logOutUser();
    }

    private boolean closeSoftkeyBoard(EditText et) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive() && et != null && imm.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //登录，发送用户名和密码给后台
    private void login() {
        loginRegisterLayout.setVisibility(View.INVISIBLE);
        loginSuccessName.setVisibility(View.VISIBLE);
        loginOut.setVisibility(View.VISIBLE);
        loginSuccessName.setText(getUserName());
        closeSoftkeyBoard(userNameEdit);

    }

    private void loginAlaways() {

    }

    private void login(String count, String password) {
        if (count.isEmpty() || password.isEmpty()) {
            Util.showToast(R.string.no_empty, con);
            return;
        }

        if (count.length() < 3 || count.length() > 16) {
            Util.showToast(R.string.count_rule_tip_msg, con);
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            Util.showToast(R.string.password_rule_tip_msg, con);
            return;
        }

        //判断是否由空格
        if (count.indexOf(" ") != -1 || password.indexOf(" ") != -1) {
            Util.showToast(R.string.no_space, con);
            return;
        }
        String compileString = getCompiledString(count);
        if (!compileString.isEmpty()) {
            Util.showToast(R.string.character_rule_tip_msg, con);
            return;
        }
        if (isNumberAll(password)) {
            Util.showToast(R.string.password_tip_no_all_number, con);
            return;
        }

        String urlStr = Global.SERVICE_URL + "login?";
        StringBuffer buffer = new StringBuffer(urlStr);
        try {
            buffer.append("name=").append(URLEncoder.encode(count, "utf-8")).append("&password=").append(password);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
       }
        urlStr = buffer.toString();
      //  taskThread.addTask(new HttpTask(urlStr, Global.MSG_LOGIN,this));*/
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
        //
        return matcher.replaceAll("").trim();
    }

    //取得用户输入的用户名
    private String getUserName() {
        return userNameEdit.getText().toString();
    }

    //取得用户输入的密码
    private String getPassword() {
        return passwordEdit.getText().toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (taskThread != null) {
            taskThread.threadDestroy();
        }
        if (msgKey !=null){
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag,appMessage.toString());
        int  msgType = appMessage.getMsgType();
        //start 注册
       /* if (msgType == Global.MSG_LOGIN){
            ToJsonLogin toJsonLogin = new ToJsonLogin(appMessage.getMsg());
            int []a = toJsonLogin.parseMoney();
            Message successMsg = handler.obtainMessage();
            //第三个数据返回1，表示登录成功，返回0，表示登录失败
            if (a[0] == 1){
                successMsg.what = LOGIN_SUCCESS_MSG;
                successMsg.arg1 = a[1];
                successMsg.arg2 = a[2];
                Setting.getInstance().setUserId(a[3]);
                handler.sendMessage(successMsg);
            }else if (a[0]==0){
                successMsg.what = LOGIN_WRONG_MSG;
                handler.sendMessage(successMsg);
            }




           *//* String msg = appMessage.getMsg();
            if (msg == null)
                return;
            Message successMsg = handler.obtainMessage();
            if ( !msg.equals(getString(R.string.wrong_count_password)) && msg.contains(getString(R.string.login_success))) {
                successMsg.what = LOGIN_SUCCESS_MSG;
                handler.sendMessage(successMsg);
            } else if (msg.equals(getString(R.string.wrong_count_password))) {
                successMsg.what = LOGIN_WRONG_MSG;
                handler.sendMessage(successMsg);
            } else {
                successMsg.what = LOGIN_WRONG_MSG;
                handler.sendMessage(successMsg);
            }*//*
        }*/
        //end   注册

    }
}
