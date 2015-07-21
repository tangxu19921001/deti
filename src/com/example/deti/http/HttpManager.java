package com.example.deti.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator on 2015/7/15.
 */
public class HttpManager {
    private Object mLock = new Object();
    private Queue<ServerTask>mHttpQueue = null;
    private static HttpManager instance;
    private List<HttpLoop> mHttpTaskThreadList = null;
    public  static HttpManager getInstance(){
        if (instance==null){
            instance = new HttpManager();
        }
        return  instance;
    }
    private HttpManager(){
        mHttpQueue = new LinkedBlockingDeque<ServerTask>();
        mHttpTaskThreadList = new ArrayList<HttpLoop>();
    }
   /* public  ServerTask login(String account,String pwd){
        ServerTask task = new ServerTask(TaskType.HTTP_LOGIN);
        task.setStrHparam(account);
        task.setStrLparam(pwd);
    //TODO    if (addTask(task))
    }*/

  /*  public boolean addTask(ServerTask task){
        synchronized (mLock){
          //TODO  if (!mHttpQueue)
        }
    }*/
    /*private ServerTask getTask(){
        ServerTask task = null;
        synchronized (mLock) {
            while (mHttpQueue.size() ==0){
                try{
                    mLock.wait();
                }catch (InterruptedException e){
                    return  task;
                }
            }
        }
    }*/
    class HttpLoop extends  Thread{
        private boolean mbStop = false;
        public  void run(){
            while (!mbStop){
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
              /*  ServerTask task = getTask();
                if (task == null){
                    continue;
                }*/
            }
        }
    }
}
