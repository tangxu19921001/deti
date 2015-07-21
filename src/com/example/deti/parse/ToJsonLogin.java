package com.example.deti.parse;

import com.example.deti.myTask.TaskDetail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by john on 2015/5/13.
 */
public class ToJsonLogin {
    private String msg;
    private int a[] = new int[4];
    ArrayList<TaskDetail>taskDetails = new ArrayList<TaskDetail>();
    public ToJsonLogin(String msg){
        this.msg = msg;
    };
    //
    public ArrayList<TaskDetail> parseTaskDetail(){
        try {

            JSONObject jsonObject = new JSONObject(msg);
            JSONArray jsonArray = jsonObject.getJSONArray("json_array");
           for(int i =0;i<jsonArray.length();i++){
               JSONObject taskDetailObject = jsonArray.getJSONObject(i);
               int taskId = taskDetailObject.getInt("taskId");
               String taskTime = taskDetailObject.getString("taskTime");
               String taskContent = taskDetailObject.getString("taskContent");
               String taskName =  taskDetailObject.getString("taskName");
               int taskMoney = taskDetailObject.getInt("taskMoney");
               boolean isReceived = taskDetailObject.getBoolean("isReceived");
               TaskDetail taskDetail = new TaskDetail();
               taskDetail.setTaskId(taskId);
               taskDetail.setTaskTime(taskTime);
               taskDetail.setTaskContent(taskContent);
               taskDetail.setTaskName(taskName);
               taskDetail.setTaskMoney(taskMoney);
               taskDetail.setIsReceived(isReceived);

               taskDetails.add(taskDetail);
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  taskDetails;
    }
    public int[] parseMoney(){

        try {
            JSONObject jsonObject = new JSONObject(msg);
            int isSuccess = jsonObject.getInt("isSuccess");
            a[0] = isSuccess;
            int money = jsonObject.getInt("money");
            int achievementPoint = jsonObject.getInt("achievement_point");
            int userId = jsonObject.getInt("userId");

            a[1]= money;
            a[2] = achievementPoint;
            a[3] = userId;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return a;
    }

    public ArrayList<TaskDetail>  getUnFinishedTask(){
        ArrayList<TaskDetail> taskDetailArrayList = new ArrayList<TaskDetail>();
        try {
            JSONArray jsonArray = new JSONArray(msg);
            for (int i =0;i<jsonArray.length();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                String taskContent = jsonObject.getString("taskContent");
                String taskName =  jsonObject.getString("taskName");
                int taskId = jsonObject.getInt("taskId");
                TaskDetail taskDetail = new TaskDetail();
                taskDetail.setTaskContent(taskContent);
                taskDetail.setTaskName(taskName);
                taskDetail.setTaskId(taskId);

                taskDetailArrayList.add(taskDetail);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  taskDetailArrayList;
    }
}
