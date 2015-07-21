package com.example.deti.util;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * create time 2015/1/17
 * Created by Administrator on 2015/1/17.
 */
public class TaskThread extends Thread {

    private Object lock = new Object();
    private Queue<AbstractTask> taskQueue = new LinkedBlockingQueue<AbstractTask>();
    private boolean isStop = false;

    public TaskThread() {
        start();
    }

    private AbstractTask getTask() {
        //列表获取任务时加锁
        synchronized (lock) {
            if (taskQueue != null) {
                return taskQueue.peek();
            }
            lock.notifyAll();
        }
        return null;
    }

    /**
     * used in override method onDestroy
     */
    public void threadDestroy() {
        if (taskQueue != null) {
            taskQueue = null;
        }
        isStop = true;
    }

    /**
     * can be often used ,you want run http Task in other Thread,you can use this.
     *
     * @param abstractTask object extends AbstractTask
     */
    public void addTask(AbstractTask abstractTask) {
        //列表加任务时候加锁
        synchronized (lock) {
            if (abstractTask == null)
                return;
            if (taskQueue.contains(abstractTask)) {
                return;
            }
            taskQueue.add(abstractTask);
        }
    }

    @Override
    public void run() {
        while (!isStop) {
            AbstractTask abstractTask = getTask();
            try {
                if (abstractTask == null) {
                    continue;
                }
                abstractTask.doInBackground();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //列表删除任务时加锁
            synchronized (lock) {
                taskQueue.remove(abstractTask);
            }
        }
    }
}
