package com.example.androidsmarthomesimulator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import io.webthings.webthing.Thing;
import io.webthings.webthing.WebThingServer;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private float imgResolutionx = 0;
    private float imgResolutiony = 0;
    private int xpixel = 0;
    private int ypixel = 0;
    Environments environments;
    private static MainActivity instance;
    private WebThingServer server;
    private boolean MSstatus = false;
    private boolean DSstatus = false;
    private boolean WSstatus = false;


    //1080x2340 pixels
    //1221x2211
//    private int LIMITX = 804; //dp
//    private int LIMITy = 444; //dp
    private final int LIMITX = 804; //dp
    private final int LIMITy = 444; //dp


    private float factor = 0;
    private Log log;
    Thing MotionSensor = new MultipleThings.MotionSensor();
    Thing Camera = new MultipleThings.Camera();
    Thing DoorSensor = new MultipleThings.DoorSensor();
    Thing WindowSensor = new MultipleThings.WindowSensor();
    Thing Siren = new MultipleThings.Siren();

    @SuppressLint("WrongThread")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.rlmain);
        environments = new Environments(layout, this);
        full_screen();
        getDIsplayInfo();
        setViews();
        addClickBehavior();
        instance = this;
    }

    /**
     *  Start web thing server when app is resumed.
     */
    public void onResume() {
        super.onResume();
        try {
             List<Thing> things = new ArrayList<>();
             // modelo de adição de things: things.add(sensor);
             things.add(MotionSensor);
             things.add(Camera);
             things.add(DoorSensor);
             things.add(WindowSensor);
             things.add(Siren);
             WebThingServer server =
                new WebThingServer(new WebThingServer.MultipleThings(things, "HomeSecuritySystem"), 8080);
             Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
             server.start();
        } catch (IOException e) {
            log.d("my tag","Failed to instantiate Web Thing Server");
        }
    }

    /**
     *  Stop web thing server when app is paused.
     */
    public void onPause() {
        super.onPause();
        if(server != null) {
            server.stop();
        }
    }

    /**
     *  Stop web thing server when app is destroyed.
     */
    public void onDestroy() {
        super.onDestroy();
        if(server != null) {
            server.stop();
        }
    }

    private void full_screen(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * It brings info about device display
     * @return A Point object that represents information about device display.
     */
    private Point getDIsplayInfo(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        xpixel = size.x;
        ypixel = size.y;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float value = getResources().getDisplayMetrics().density;
        imgResolutionx = LIMITX*value;
        imgResolutiony = LIMITy*value;
        factor = (ypixel/imgResolutiony > xpixel/imgResolutionx)?xpixel/imgResolutionx:ypixel/imgResolutiony;
        return size;
    }

    private void setViews(){
        int n_views = layout.getChildCount();
        for (int i = 0; i < n_views; i++){
            View v = layout.getChildAt(i);
            if (v instanceof TextView || v instanceof ImageView){
                v.getLayoutParams().width = (int)(v.getLayoutParams().width*0.95*factor);
                v.getLayoutParams().height = (int)(v.getLayoutParams().height*0.95*factor);
            }
        }
    }

    private void addClickBehavior(){
        findViewById(R.id.imgBATH).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.BATHROOM);
                if(WSstatus == true){
                    WSstatus = false;
                }
                else{
                    WSstatus = true;
                }
            }
        });
        findViewById(R.id.imgLIVINGROOM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.LIVINGROOM);
                if(MSstatus == true){
                    MSstatus = false;
                }
                else{
                    MSstatus = true;
                }
            }
        });
        findViewById(R.id.imgKITCHEN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.KITCHEN);
                if(WSstatus == true){
                    WSstatus = false;
                }
                else{
                    WSstatus = true;
                }
            }
        });
        findViewById(R.id.imgFRONT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.FRONTYARD);
            }
        });
        findViewById(R.id.imgCORRIDOR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.CORRIDOR);
            }
        });
        findViewById(R.id.imgBED1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.BEDROOM1);
                if(DSstatus == true){
                    DSstatus = false;
                }
                else{
                    DSstatus = true;
                }
            }
        });
        findViewById(R.id.imgBED2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.BEDROOM2);
                if(DSstatus == true){
                    DSstatus = false;
                }
                else{
                    DSstatus = true;
                }
            }
        });
    }

    public boolean getSensorStatus(int i){
        if(i == 0) return MSstatus;
        if(i == 1) return DSstatus;
        else return WSstatus;
    }

    public static MainActivity getInstance() {
        return instance;
    }
}