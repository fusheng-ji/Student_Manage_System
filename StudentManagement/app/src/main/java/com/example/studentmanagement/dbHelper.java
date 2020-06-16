package com.example.studentmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    public dbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists userinfo"
                +"(uid integer primary key autoincrement,"
                +"uname varchar,"
                +"pwd varchar,"
                +"age integer,"
                +"imagePath varchar"
                +")");

        sqLiteDatabase.execSQL("create table if not exists class" +
                "(id integer primary key autoincrement," +
                "name varchar," +
                "teach varchar," +
                "info varchar" +
                ")");
        sqLiteDatabase.execSQL("create table if not exists sclass" +
                "(id integer primary key autoincrement," +
                "uid integer," +
                "cid integer" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists userinfo");
        sqLiteDatabase.execSQL("drop table if exists class");
        sqLiteDatabase.execSQL("drop table if exists sclass");
        onCreate(sqLiteDatabase);
    }
}
