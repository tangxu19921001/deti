package com.example.deti.util;

import android.content.Context;
import com.example.deti.main.DumpMessage;

import java.util.HashMap;

/**
 * create time 2015/1/15
 * Created by Administrator on 2015/1/17.
 */
public class HttpTask extends AbstractTask {
    private String urlString;
    //json����
    private HashMap<String,String> paramHashMap;
    private Context context;
    private int msgType;

    /***
     * @param urlString  post�����ַ
     * @param msgType    ͨ��ģʽ��Ҫ֪������Ϣ���ͣ�
     * @param paramHashMap �ڵ�ǰ�����ģ�post��ʽ�����json����
     */
    public HttpTask(String urlString, int msgType, Context context, HashMap<String,String> paramHashMap) {
        this.urlString = urlString;
        this.context = context;
        this.msgType = msgType;
        this.paramHashMap = paramHashMap;
    }



    @Override
    public void doInBackground() {
        String returnJson = null;
        try {
            returnJson = HttpRequestUtils.httpPost(urlString, paramHashMap, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (returnJson!=null){
            DumpMessage.getInstance().dispatch(new AppMessage(returnJson, msgType));
        }

// TODO Auto-generated method stub
//                URL url;
//                try
//                {
//                    url = new URL(urlString);
///*��JSON����ת����String����ʹ��������������д*/
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setConnectTimeout(5000);
//                    conn.setDoOutput(true);//�����������
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("User-Agent", "Fiddler");
//                    conn.setRequestProperty("Content-Type", "application/json");
//
////��json����д���������
//                    OutputStream os = conn.getOutputStream();
//                    os.write(content.getBytes());
//                    os.close();
///*���������ص���Ӧ��*/
//                    int code = conn.getResponseCode();
//                    if(code == 200)
//                    {
//                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                        String line;
//                        StringBuffer stringBuffer = new StringBuffer();
//                        while ((line = in.readLine()) != null) {
//                            stringBuffer.append(line);
//                        }
////System.out.println(result);
//                        in.close();
////System.out.println(success);
//                        Log.i("HttpTask",stringBuffer.toString());
//                        DumpMessage.getInstance().dispatch(new AppMessage(stringBuffer.toString(),msgType));
//                    }
//                    else
//                    {
//                        Toast.makeText(context, "����������", Toast.LENGTH_SHORT).show();
//                    }
//                }//end try
//                catch (Exception e)
//                {
//// TODO: handle exception
//                    Toast.makeText(context, "����������", Toast.LENGTH_SHORT).show();
//                    throw new RuntimeException(e);
//
//                }
//            }//end doInback
    }

}


