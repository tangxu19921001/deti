package com.example.deti.myTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deti.R;
import com.example.deti.io.Setting;
import com.example.deti.main.DumpMessage;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;
import com.example.deti.util.Global;
import com.example.deti.util.SysApplication;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by john on 2015/5/13.
 */
public class EvaluateActivity extends Activity implements IMsgBack {
    String msgKey;
    int position;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_layout);
        msgKey = DumpMessage.getInstance().RegistryCallback(EvaluateActivity.this);
        SysApplication.getInstance().addActivity(this);
        Intent intent = getIntent();

        position = intent.getIntExtra("position",0);
        initView();

    }
    private void initView(){
       /* LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.evaluate_layout,null);*/
        final EditText evaluateEditText = (EditText)findViewById(R.id.evaluateText);
        TextView submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String evaluate = evaluateEditText.getText().toString().trim();
                int userId = Setting.getInstance().getUserId();
                if (evaluate !=null && userId!= -1){
                    clickToSendToBack(evaluate,userId);
                }
            }
        });
    }

    private void clickToSendToBack(final String evaluate, final int userId){
        new Thread()
        {


            @Override
            public void run() {
// TODO Auto-generated method stub
                Looper.prepare();
                final String urlPath= Global.SERVICE_URL+"verifyTaskFinished";
                URL url;
                try
                {
                    url = new URL(urlPath);
/*封装子对象*/
                    JSONObject taskObject = new JSONObject();

                    taskObject.put("evaluate",evaluate );
                    taskObject.put("position",position);
                    taskObject.put("userId", userId);

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

                            Toast.makeText(EvaluateActivity.this, "评价成功", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "评价失败", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        DumpMessage.getInstance().UnRegistryCallback(msgKey);
    }
    @Override
    public void onMsg(AppMessage msg) {

    }
}
