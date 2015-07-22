package com.example.deti.myTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.example.deti.R;
import com.example.deti.io.Setting;
import com.example.deti.main.DumpMessage;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;
import com.example.deti.util.Global;
import com.example.deti.util.SysApplication;
import com.example.deti.util.Util;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2015/1/23.
 */
public class TaskDetailActivity extends Activity implements IMsgBack {
    private String Tag = TaskDetailActivity.class.getSimpleName();
    private String msgKey;
    private EditText postContent;
    private TextView clickToSend;
    private EditText clickTaskName;
    private  EditText clickTaskMoney;

    private int snoCount = 0;
    int askMoney;
    private  String contentText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_task_out);
        msgKey = DumpMessage.getInstance().RegistryCallback(TaskDetailActivity.this);
        SysApplication.getInstance().addActivity(this);
        initView();


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
        clickTaskName = (EditText)findViewById(R.id.click_task_name);
        postContent = (EditText) findViewById(R.id.post_content);
        clickTaskMoney = (EditText)findViewById(R.id.click_task_money);

        clickToSend = (TextView) findViewById(R.id.click_to_send);
        clickToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentText = postContent.getText().toString().trim();
                try {
                    askMoney = Integer.parseInt(clickTaskMoney.getText().toString().trim());

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (askMoney > Setting.getInstance().getMoney()) {
                    Util.showToast(R.string.no_money, TaskDetailActivity.this);
                }
                if (contentText != null && askMoney <= Setting.getInstance().getMoney()) {
                    clickToSendToBack();
                    new Thread() {
                        @Override
                        public void run() {
// start 推送
                            JPushClient jPushClient = new JPushClient(Global.APP_MAIN_PASSWORD, Global.KEY_VALUE);
                            PushPayload payload = buildPushObject();

                            try {
                                PushResult result = jPushClient.sendPush(payload);
                                Log.i(Tag, "get Resulet-->" + result);
                            } catch (APIConnectionException e) {
                                e.printStackTrace();
                                Log.i(Tag, "should retry later-->" + e);
                            } catch (APIRequestException e) {

                                //Log.e("Should review the error, and fix the request", "" + e);
                                Log.i(Tag, "HTTP Status: " + e.getStatus());
                                Log.i(Tag, "Error Code: " + e.getErrorCode());
                                Log.i(Tag, "Error Message: " + e.getErrorMessage());
                            }
                            //TODO封装Json  &msg_content={"n_content":"你好"}
                           /* snoCount++;
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(Global.JPUSH_TITLE).append(Global.APP_KEY).append("=").append(Global.KEY_VALUE).append("&").append(Global.SENDNO).append("=").append(snoCount).append("&").
                                    append(Global.RECEIVER_TYPE).append("=").append(4).append("&").append(Global.VERIFICATION).append("=").append(getVerification()).append("&").append(Global.MSG_TYPE).
                                    append("=").append(1).append("&").append(Global.PLATFORM).append("=").append("android");
                            String uriAPI = stringBuffer.toString();
                            uriAPI = uriAPI.replaceAll("&", "%26");
                            uriAPI = uriAPI.replaceAll(" ", "%20");
    *//*建立HTTP Post连线*//*
                            Log.w(Tag,"uri--->" + uriAPI);

                            HttpPost httpRequest = new HttpPost(uriAPI);
                            //Post运作传送变数必须用NameValuePair[]阵列储存
                            //传参数 服务端获取的方法为request.getParameter("name")
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("name", "this is post"));
                            try {


                                //发出HTTP request
                                httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                                //取得HTTP response
                                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                                Log.i(Tag, "返回码--->" + httpResponse.getStatusLine().getStatusCode());
                                //若状态码为200 ok
                                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                    //取出回应字串
                                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                                    Log.i(Tag, "返回结果--->" + strResult);
                                } else {
                                    Log.i(Tag, "返回结果--->" + httpResponse.getStatusLine().toString());
                                    //  postContent.setText("Error Response"+httpResponse.getStatusLine().toString());
                                }
                            } catch (ClientProtocolException e) {
                                Log.i(Tag, "返回结果--->" + e.getMessage().toString());
                                //     postContent.setText(e.getMessage().toString());
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                Log.i(Tag, "返回结果--->" + e.getMessage().toString());
                                //    postContent.setText(e.getMessage().toString());
                                e.printStackTrace();
                            } catch (IOException e) {
                                Log.i(Tag, "返回结果--->" + e.getMessage().toString());
                                //  postContent.setText(e.getMessage().toString());
                                e.printStackTrace();
                            }*/
                        }
                    }.start();

                }
            }

        });
    }

    private PushPayload buildPushObject(){

        return  PushPayload.alertAll(contentText);
    }

    private void clickToSendToBack(){
        new Thread()
        {


            @Override
            public void run() {
// TODO Auto-generated method stub
                Looper.prepare();
                final String urlPath=Global.SERVICE_URL+"taskPublish";
                URL url;
                try
                {
                    url = new URL(urlPath);
/*封装子对象*/
                    JSONObject taskObject = new JSONObject();
                   taskObject.put("taskName", clickTaskName.getText().toString().trim());
                    taskObject.put("taskContent",postContent.getText().toString().trim() );
                    taskObject.put("taskMoney",clickTaskMoney.getText().toString().trim() );
                    taskObject.put("userId", Setting.getInstance().getUserId());

/*封装Person数组*/

/*把JSON数据转换成String类型使用输出流向服务器写*/
                    String content = String.valueOf(taskObject);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);//设置允许输出
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "application/json");


                    OutputStream os = conn.getOutputStream();
                    os.write(content.getBytes());
                    os.close();
/*服务器返回的响应码*/
                    int code = conn.getResponseCode();
                    if(code == 200)
                    {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String retData = null;
                        String responseData = "";
                        while((retData = in.readLine()) != null)
                        {
                            responseData += retData;
                        }
//System.out.println(result);
                        in.close();
//System.out.println(success);
                        if (responseData.equals("OK"))
                        Toast.makeText(TaskDetailActivity.this, "发送任务成功", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "数据提交失败", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
// TODO: handle exception
                    throw new RuntimeException(e);
                }
                Looper.loop();
            }

        }.start();
    }
    private String getVerification() {
        int sendno = snoCount;
        int receiverType = 4;
        String receiverValue = "";
        String masterSecret = Global.APP_MAIN_PASSWORD; //极光推送portal 上分配的 appKey 的验证串(masterSecret)

        String input = String.valueOf(sendno) + receiverType + receiverValue + masterSecret;
        String verificationCode = toMD5(input);
        return verificationCode;
    }

    public static String toMD5(String source) {
        if (null == source || "".equals(source)) return null;
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(source.getBytes());
            return toHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
    public static String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toHex(byte[] buf) {
        if (buf == null) return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    /**
     * 大小写
     * */
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DumpMessage.getInstance().UnRegistryCallback(msgKey);
    }

    @Override
    public void onMsg(AppMessage appMessage) {

    }
}
