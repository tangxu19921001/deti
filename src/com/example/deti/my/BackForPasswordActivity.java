package com.example.deti.my;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deti.R;
import com.example.deti.entity.Person;
import com.example.deti.main.DumpMessage;
import com.example.deti.main.LoginActivity;
import com.example.deti.myTask.TaskDetailActivity;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/7/21.
 */
public class BackForPasswordActivity extends Activity  implements IMsgBack{
    private String Tag = BackForPasswordActivity.class.getSimpleName();
    private String msgKey;
    private EditText phoneEdit;
    private TextView getVerifyText;
    private TaskThread taskThread;
    private Handler handler;
    private EditText codeEdit;
    private EditText setPasswordEdit;
    private EditText verifyPasswordEdit;
    private TextView submitText;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.look_back_for_password);
        msgKey = DumpMessage.getInstance().RegistryCallback(BackForPasswordActivity.this);

        initView();
        initHandler();
    }
    private void initView(){
        if (taskThread==null){
            taskThread = new TaskThread();
        }
        TextView titleText = (TextView)findViewById(R.id.title);
        titleText.setText(R.string.look_for_password);
        ImageView backImage = (ImageView)findViewById(R.id.title_image_back);
        codeEdit = (EditText)findViewById(R.id.input_code_edit);
        setPasswordEdit = (EditText)findViewById(R.id.set_password_edit);
        verifyPasswordEdit = (EditText)findViewById(R.id.verify_password);
        submitText = (TextView)findViewById(R.id.finish_text);
        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVerifyCode();
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        phoneEdit = (EditText)findViewById(R.id.phone_edit_look_for);
        getVerifyText = (TextView)findViewById(R.id.get_verify);
        getVerifyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerify(getPhone());
            }
        });

    }
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case Global.MSG_GET_VERIFY:
                        Util.showToast(R.string.verify_code_already, BackForPasswordActivity.this);
                        break;
                    case Global.MSG_VERIFY_CODE:
                        submitPassword();

                        break;
                    case Global.MSG_USER_FORGET_PASSWORD:
                        Util.startActivityAnim(BackForPasswordActivity.this, LoginActivity.class);
                        Util.showToast(R.string.update_password_success, BackForPasswordActivity.this);
                        finish();
                        break;
                    case Global.MSG_REQUEST_WRONG:
                        Toast.makeText(BackForPasswordActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                        break;

                }
            }
        };
    }
    private void submitVerifyCode(){

        if (codeEdit.getText().toString().isEmpty()){
            Util.showToast(R.string.verifycation_code_no_empty,BackForPasswordActivity.this);
            return;
        }
        if (!isMobileNO(getPhone())||getPhone().isEmpty()){
            Util.showToast(R.string.no_empty_or_no_phone, this);
            return;
        }
        if (!setPasswordEdit.getText().toString().equals(verifyPasswordEdit.getText().toString())){
            Util.showToast(R.string.password_not_same, this);
            return;
        }

        //����Map�������ӿڲ�����ȥ��

        HashMap<String,String> paramHashMap = new HashMap<String, String>();
        paramHashMap.put("cellphone",getPhone());
        paramHashMap.put("verificationcode",codeEdit.getText().toString());
        taskThread.addTask(new HttpTask(Global.VERIFY_CODE_URL, Global.MSG_VERIFY_CODE, this, paramHashMap));
    }
    private void submitPassword(){
        if (isPasswordMotify(setPasswordEdit.getText().toString())){
            //����Map�������ӿڲ�����ȥ��
            //密码需要加密32位小写传输
            String Md5Password = TaskDetailActivity.getMd5Value(setPasswordEdit.getText().toString());
            HashMap<String,String> paramHashMap = new HashMap<String, String>();
            paramHashMap.put("cellphone",getPhone());
            paramHashMap.put("password",Md5Password);
            taskThread.addTask(new HttpTask(Global.USER_FROGET_PASSWORD, Global.MSG_USER_FORGET_PASSWORD, this, paramHashMap));
        }
    }
    private boolean isPasswordMotify(String password){
        if (password.isEmpty()) {
            Util.showToast(R.string.password_no_empty, this);
            return false;
        }
        if (password.length() < 8 || password.length() > 32) {
            Util.showToast(R.string.count_rule_tip_msg, this);
            return false;
        }
        //�ж��Ƿ��ɿո�
        if (password.indexOf(" ") != -1 ) {
            Util.showToast(R.string.no_space, this);
            return false;
        }
        String compileString = getCompiledString(password);
        if (!compileString.isEmpty()) {
            Util.showToast(R.string.character_rule_tip_msg, this);
            return false;
        }
        return true;
    }
    private void getVerify(String phone){
        if (!isMobileNO(phone)||phone.isEmpty()){
            Util.showToast(R.string.no_empty_or_no_phone, this);
            return;
        }
        //����Map�������ӿڲ�����ȥ��
        HashMap<String,String> paramHashMap = new HashMap<String, String>();
        paramHashMap.put("cellphone",phone);
        paramHashMap.put("area","+86");
        taskThread.addTask(new HttpTask(Global.GET_VERIFY_URL, Global.MSG_GET_VERIFY, this, paramHashMap));
    }
    public static boolean isMobileNO(String mobiles) {
        if (mobiles.length() ==11){
            return true;
        }
        else {
            return false;
        }

    }

    private String getPhone(){
        return phoneEdit.getText().toString();
    }
    //���ַ����淶��
    private String getCompiledString(String model) {
        //ƥ������:[\u4e00-\u9fa5]Ӣ����ĸ:[a-zA-Z]����:[0-9]ƥ�����ģ�Ӣ����ĸ�����ּ�_:
        //  ^[\u4e00-\u9fa5_a-zA-Z0-9]+$
        String stringPre = "[//^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$]";
        Pattern pattern = Pattern.compile(stringPre);
        Matcher matcher = pattern.matcher(model);
        //
        return matcher.replaceAll("").trim();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (msgKey!=null){
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
        if (taskThread!=null){
            taskThread.threadDestroy();
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }
    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int  msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        JsonParse jsonParse = new JsonParse();
        switch (msgType){
            case Global.MSG_GET_VERIFY:

                Person person1 = jsonParse.getPerson(appMessage.getMsg(),Person.class);
                if (person1.getResult()==true){
                    successMsg.what = Global.MSG_GET_VERIFY;
                    successMsg.obj = person1.getMessage();
                    handler.sendMessage(successMsg);
                    break;
        }else{
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = person1.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;
            case Global.MSG_VERIFY_CODE:
                Person verifyPerson = jsonParse.getPerson(appMessage.getMsg(),Person.class);
                if (verifyPerson.getResult()==true){
                    successMsg.what = Global.MSG_VERIFY_CODE;
                    successMsg.obj = verifyPerson.getMessage();
                    handler.sendMessage(successMsg);
                    break;
                }else{
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = verifyPerson.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;
            case Global.MSG_USER_FORGET_PASSWORD:
                Person forgetPerson = jsonParse.getPerson(appMessage.getMsg(),Person.class);
                if (forgetPerson.getResult()==true){
                    successMsg.what = Global.MSG_USER_FORGET_PASSWORD;
                    successMsg.obj = forgetPerson.getMessage();
                    handler.sendMessage(successMsg);
                    break;
                }else{
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = forgetPerson.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;

        }
    }
}
