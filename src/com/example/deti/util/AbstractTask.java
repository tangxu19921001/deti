package com.example.deti.util;

/**
 * create time 2015/1/17
 */
public abstract class AbstractTask {
    /**
     * it will be used in TaskThread,in method run(),it will run in other Thread,not main Thread,and it will return nothing, you can use interface get param.
     */
    public void doInBackground() {
    }

    ;

}
