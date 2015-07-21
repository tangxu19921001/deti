package com.example.deti.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
/**
 * Created by Administrator on 2015/2/2.
 */
public class DBHelper extends SQLiteOpenHelper {
    private  static final String DATABASE_NAME = "message.db";
    private static final int DATABASE_VERSION = 1;
   public DBHelper(Context context){
       super(context,DATABASE_NAME,null,DATABASE_VERSION);

   }
    @Override
    public void onCreate(SQLiteDatabase db) {
                 db.execSQL("CREATE TABLE IF NOT EXISTS message"  +"(_id INTEGER PRIMARY KEY AUTOINCREMENT, content NUMERIC)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE message ADD COLUMN other STRING");
    }
}
