package com.example.deti.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.deti.R;
import com.example.deti.entity.Person;
import com.example.deti.io.Setting;
import com.example.deti.myTask.TaskDetailActivity;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2015/7/15.
 */
public class LoginActivity extends Activity implements IMsgBack{

    private static final String Tag = LoginActivity.class.getSimpleName();

    //用来保存登录用 输入的用户名和密码
    private EditText userNameEdit;
    private EditText passwordEdit;
    private int fromTab;


    private TaskThread taskThread = new TaskThread();
    private Handler handler;

    //登录成功
    private static final int LOGIN_SUCCESS_MSG = 10;
    private static final int LOGIN_WRONG_MSG = 11;

    //fragment以来的activity注册
    private String msgKey;
    /**
     * Author TX
     * create time 2015/1/15
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.user_layout);
        msgKey = DumpMessage.getInstance().RegistryCallback(LoginActivity.this);
        fromTab = this.getIntent().getIntExtra("fragment", 0);
        initView();
        initHandler();
    }


    private void initView() {

        userNameEdit = (EditText) findViewById(R.id.login_user_name_edit);
        passwordEdit = (EditText) findViewById(R.id.login_usr_password_edit);
        TextView loginText = (TextView) findViewById(R.id.login_text);
        TextView registerText = (TextView) findViewById(R.id.register_text);
        TextView lookForPassword = (TextView) findViewById(R.id.look_for_password);

//        loginOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginOut();
//            }
//        });
        //暂时TODO
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(getUserName(), getPassword());
            }
        });

        //  File file= new File(Global.SHAREDPREFERENCE_URL);
    }

   /* @Override
    public void onBackPressed(){

    }*/
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case LOGIN_SUCCESS_MSG:
                        Util.showToast(R.string.login_success, LoginActivity.this);
                        Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);
                        intent.putExtra("fragment", fromTab);
                        Setting.getInstance().setUserPhone(getUserName());
                        Setting.getInstance().setFragmentId(fromTab);
                        startActivity(intent);


                    /*    Setting.getInstance().setUserName(getUserName());
                        Setting.getInstance().setPassword(getPassword());
                        Setting.getInstance().setUserMoney(msg.arg1);
                        Setting.getInstance().setUserAchievement(msg.arg2);*/
                        break;
                    case LOGIN_WRONG_MSG:

                        Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();

                }
            }
        };
    }
    //跳转到注册页面



   /* private void intentRegister() {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("fragment", 3);
        startActivity(intent);
    }*/

    /*//登出，有些组件重新显示，有些消失
    private void loginOut() {
        loginRegisterLayout.setVisibility(View.VISIBLE);
        loginSuccessName.setVisibility(View.GONE);
        loginOut.setVisibility(View.GONE);
        closeSoftkeyBoard(userNameEdit);
        Setting.getInstance().logOutUser();
    }*/

    private boolean closeSoftkeyBoard(EditText et) {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive() && et != null && imm.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void login(String count, String password) {
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
        //密码需要加密32位小写传输
        String Md5Password = TaskDetailActivity.getMd5Value(password);

       //传递Map参数即接口参数进去。
        HashMap<String,String>  paramHashMap = new HashMap<String, String>();
        paramHashMap.put("cellphone",count);
        paramHashMap.put("password",Md5Password);
        taskThread.addTask(new HttpTask(Global.LOGIN_URL, Global.MSG_LOGIN, this, paramHashMap));
//client/customer/login

       /* String urlStr = Global.SERVICE_URL + "login?";
        StringBuffer buffer = new StringBuffer(urlStr);
        try {
            buffer.append("name=").append(URLEncoder.encode(count, "utf-8")).append("&password=").append(password);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        urlStr = buffer.toString();
        taskThread.addTask(new HttpTask(urlStr, Global.MSG_FLOGIN,this));*/
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int  msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        //start 注册
        if (msgType == Global.MSG_LOGIN){
            JsonParse jsonParse = new JsonParse();
            Person person1 = jsonParse.getPerson(appMessage.getMsg(),Person.class);
            if (person1.getResult()==true){
                successMsg.what = LOGIN_SUCCESS_MSG;
                handler.sendMessage(successMsg);
            }else{
                successMsg.what = LOGIN_WRONG_MSG;
                successMsg.obj = person1.getMessage();
                handler.sendMessage(successMsg);
            }



        }
        //end   注册

    }
}
