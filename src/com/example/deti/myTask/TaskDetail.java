package com.example.deti.myTask;

/**
 * 任务实体
 * Created by john on 2015/5/13.
 */
public class TaskDetail {

    private int taskId;
    private String taskTime;
    private String taskContent;
    private String taskName;
    private int taskMoney;
    private boolean isReceived;
    private boolean isFinished;

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }



    public int getTaskMoney() {
        return taskMoney;
    }

    public void setTaskMoney(int taskMoney) {
        this.taskMoney = taskMoney;
    }



    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }


}
