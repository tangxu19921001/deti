package com.example.deti.http;

/**
 * Created by Administrator on 2015/7/15.
 */
public class ServerTask {
    private int hparam;
    private int lparam;

    public int getHparam() {
        return hparam;
    }

    public void setHparam(int hparam) {
        this.hparam = hparam;
    }

    public String getStrHparam() {
        return strHparam;
    }

    public void setStrHparam(String strHparam) {
        this.strHparam = strHparam;
    }

    public String getStrLparam() {
        return strLparam;
    }

    public void setStrLparam(String strLparam) {
        this.strLparam = strLparam;
    }

    private String strHparam;
    private String strLparam;
    private int taskType;
    public int getTaskType() {
        return taskType;
    }
    public void setLparam(int lparam) {
        this.lparam = lparam;
    }
    public ServerTask(int taskType)
    {
        this.taskType = taskType;
    }

}
