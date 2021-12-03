package com.example.androidsmarthomesimulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class BD {
    private final SQLiteDatabase bd;

    public BD(Context ctx){
        BDCore auxbd = new BDCore(ctx);
        bd = auxbd.getWritableDatabase();
    }
    /*
    public void insert(Sensor sensor){
        ContentValues valores = new ContentValues();
        //valores.put("exemplo", exemplo.getNome());

        bd.insert("sensor", null, valores);
    }

    public void update(Sensor sensor){
        ContentValues valores = new ContentValues();
        //valores.put("exemplo", exemplo.getNome());

        //bd.update("sensor", valores, "_id = ?", new String[](""+sensor.getId()));
    }

    public void delete(Sensor sensor){
        //bd.delete("sensor", "_id = "+sensor.getId(), null);
    }

    public List<Sensor> search(){
        List<Sensor> list = new ArrayList<Sensor()>();
        String[] colunas = new String[]{"elemento1", "elemento2", "etc"};
        Cursor cursor = bd.query("sensor", colunas, null, null, null, null, "_id ASC");

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                Sensor s = new Sensor();
                s.setId(cursor.getLong(0));
                s.setExemplo(cursor.getString(0));
                list.add(s);
            }while(cursor.moveToNext());
        }
    }*/
}
