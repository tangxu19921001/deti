package com.example.deti.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/7/15.
 */
public class HttpRequestUtils {


    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);    //日志记录

    /**
     * httpPost
     *
     * @param url       路径
     * @param jsonParam 参数
     * @return
     */
   /* public static String httpPost(String url, String jsonParam) {
        return httpPost(url, jsonParam, false);
    }*/

    /**
     * post请求
     *
     * @param url            url地址
     * @param paramHashMap      参数
     * @param isNeedResponse 不需要返回结果
     * @return
     */
    public static String httpPost (String url, HashMap<String,String> paramHashMap, boolean isNeedResponse) throws Exception{
        //post请求返回结果
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // 目标地址
        HttpPost httppost = new HttpPost(
                url);
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        // post 参数 传递
        for(HashMap.Entry<String,String>entry:paramHashMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            nvps.add(new BasicNameValuePair(key, value)); // 参数
        }
        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); // 设置参数给Post

        // 执行
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: "
                    + entity.getContentLength());
        }
        // 显示结果
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                entity.getContent(), "UTF-8"));

        String line = null;
        StringBuffer stringBuffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        if (entity != null) {
            entity.consumeContent();
        }
        if (isNeedResponse){
            return stringBuffer.toString();
        }
        return null;

    }


    /**
     * 发送get请求
     *
     * @param url 路径
     * @return
     */
    public static String httpGet(String url) {
        //get请求返回结果
        String jsonResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            //发送get请求
            url = URLDecoder.decode(url, "UTF-8");
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    /**读取服务器返回过来的json字符串数据**/
                    jsonResult = EntityUtils.toString(response.getEntity());
                    /**把json字符串转换成json对象**/

        }else{
            logger.error("get请求提交失败:" + url);
        }
    }

    catch(
    IOException e
    )

    {
        logger.error("get请求提交失败:" + url, e);
    }

    return jsonResult;
}
        }


