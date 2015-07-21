package com.example.deti.entity;

import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class DesignerList {
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<Designer> getUserList() {
        return userList;
    }

    public void setUserList(List<Designer> userList) {
        this.userList = userList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private String message;
    private int pageCount;
    private boolean result;
    private List<Designer> userList;
    private int rowCount;

}
