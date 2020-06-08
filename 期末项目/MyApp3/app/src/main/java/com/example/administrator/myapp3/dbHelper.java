package com.example.administrator.myapp3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2020/3/26.
 */

public class dbHelper extends SQLiteOpenHelper {
    String TB_Name = "userInfo";
    String TB_Name2 = "lessonInfo";
    String TB_Name3 = "xuankeInfo";
    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists "+TB_Name
                +"(uid integer primary key autoincrement,"
                +"uname varchar,"
                +"pwd varcher,"
                +"age integer"
                +")");
        sqLiteDatabase.execSQL("create table if not exists "+TB_Name2
                +"(lid integer primary key autoincrement,"
                +"lessonname varchar"
                +")");
        sqLiteDatabase.execSQL("create table if not exists "+TB_Name3
                +"(xid integer primary key autoincrement,"
                +"userid integer,"
                +"lessonid integer"
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
         sqLiteDatabase.execSQL("drop table if exists "+TB_Name);
         sqLiteDatabase.execSQL("drop table if exists "+TB_Name2);
        sqLiteDatabase.execSQL("drop table if exists "+TB_Name3);
        onCreate(sqLiteDatabase);
    }
}
