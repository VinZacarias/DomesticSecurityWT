package com.example.androidsmarthomesimulator;

import android.os.AsyncTask;

import java.io.IOException;

public class SubmitState extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... uri) {
//        try {
//                URL url = new URL(uri[0]);
//                URLConnection urlCon = url.openConnection();
//        } catch (IOException e) {
//
//        }
        return "Success";
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }
}
