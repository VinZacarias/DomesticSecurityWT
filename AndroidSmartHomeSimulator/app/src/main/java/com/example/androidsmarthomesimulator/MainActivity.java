package com.example.androidsmarthomesimulator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActionBar;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private float imgResolutionx = 0;
    private float imgResolutiony = 0;
    private int xpixel = 0;
    private int ypixel = 0;
    Environments environments;


    //1080x2340 pixels
    //1221x2211
//    private int LIMITX = 804; //dp
//    private int LIMITy = 444; //dp
    private int LIMITX = 804; //dp
    private int LIMITy = 444; //dp


    private float factor = 0;



    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout)findViewById(R.id.rlmain);
        environments = new Environments(layout, this);
        full_screen();
        getDIsplayInfo();
        setViews();
        addClickBehavior();
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
        ((View)findViewById(R.id.imgBATH)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.BATHROOM);
            }
        });
        ((View)findViewById(R.id.imgLIVINGROOM)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.LIVINGROOM);
            }
        });
        ((View)findViewById(R.id.imgKITCHEN)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.KITCHEN);
            }
        });
        ((View)findViewById(R.id.imgFRONT)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.FRONTYARD);
            }
        });
        ((View)findViewById(R.id.imgCORRIDOR)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.CORRIDOR);
            }
        });
        ((View)findViewById(R.id.imgBED1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.BEDROOM1);
            }
        });
        ((View)findViewById(R.id.imgBED2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                environments.changeVisibility(Environments.Envs.BEDROOM2);
            }
        });
    }
}