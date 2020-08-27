package com.app.wooker.SQLLignt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLightHelper extends SQLiteOpenHelper {

    public SQLightHelper(Context context){
        super(context,"users",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("OnCreate");
        db.execSQL("CREATE TABLE login (id INTEGER PRIMARY KEY AUTOINCREMENT,email VARCHAR(100),password VARCHAR(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("OnUpgrade");
    }
}
