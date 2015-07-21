package com.example.deti.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/2/2.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private Context context;
    public  DBManager(Context context){
        this.context= context;
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

//*
// add message
// */
    public void add (NewsMessage newsMessage){
        db.beginTransaction(); // 开始事务
        try{
          /*  db.execSQL("INSERT INTO message VALUES(null," + newsMessage.content+")");*/

            db.execSQL("INSERT INTO message VALUES(null,?)",new Object[]{newsMessage.content});
       db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public ArrayList<NewsMessage> query(){
        ArrayList<NewsMessage > newsMessages =  new ArrayList<NewsMessage>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()){
           NewsMessage newsMessage = new  NewsMessage();
            newsMessage._id = c.getInt(c.getColumnIndex("_id"));
            newsMessage.content = c.getString(c.getColumnIndex("content"));
            newsMessages.add(newsMessage);
        }
        c.close();
        return newsMessages;
    }

    public void closeDB(){
        db.close();
    }

    private Cursor queryTheCursor(){
        Cursor c = db.rawQuery("SELECT * FROM message",null);
        return c;

    }
}
