package com.example.androidsmarthomesimulator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDCore extends SQLiteOpenHelper {
    private static final String Name_bd = "sensors";
    private static final int version = 1;

    public BDCore(Context ctx){
        super(ctx, Name_bd, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("Create table sensor (_id integer primary key autoincrement, type text not null, state boolean not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL("drop table sensor");
        onCreate(bd);
    }
}
