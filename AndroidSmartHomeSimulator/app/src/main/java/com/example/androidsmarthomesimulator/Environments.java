package com.example.androidsmarthomesimulator;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

public class Environments {
    MainActivity ma;
    private RelativeLayout rl;
    Environments(RelativeLayout rl, MainActivity ma){
        this.rl = rl;
        this.ma = ma;
        int n_views = rl.getChildCount();
        for (int i = 0; i < n_views; i++){
            View v = rl.getChildAt(i);
            if (v instanceof ImageView) {
                try {
                    Status st = Status.valueOf(v.getTag().toString());
                    if (sensors.containsKey(st))
                        sensors.get(st).add((ImageView) v);
                    else{
                        ArrayList<ImageView> list = new ArrayList<ImageView>();
                        list.add((ImageView)v);
                        sensors.put(st, list);
                    }
                } catch (IllegalArgumentException e) {

                }
            }
        }
    }

    private static Hashtable<Status, ArrayList<ImageView>> sensors = new Hashtable();

    public enum Envs{
        KITCHEN("KITCHEN"), LIVINGROOM("LIVINGROOM"), FRONTYARD("FRONTYARD"),
        BATHROOM("BATHROOM"), CORRIDOR("CORRIDOR"), BEDROOM1("BEDROOM1"),
        BEDROOM2("BEDROOM2");
        private String envs;

        Envs(String envs){
            this.envs = envs;
        }
    }

    private enum Status {
        KITCHEN_ON("KITCHEN_ON"), LIVINGROOM_ON("LIVINGROOM_ON"), FRONTYARD_ON("FRONTYARD_ON"),
        BATHROOM_ON("BATHROOM_ON"), CORRIDOR_ON("CORRIDOR_ON"), BEDROOM1_ON("BEDROOM1_ON"),
        BEDROOM2_ON("BEDROOM2_ON"), KITCHEN_OFF("KITCHEN_OFF"), LIVINGROOM_OFF("LIVINGROOM_OFF"),
        FRONTYARD_OFF("FRONTYARD_OFF"), BATHROOM_OFF("BATHROOM_OFF"), CORRIDOR_OFF("CORRIDOR_OFF"),
        BEDROOM1_OFF("BEDROOM1_OFF"), BEDROOM2_OFF("BEDROOM2_OFF");
        private String status;

        Status(String status){
            this.status = status;
        }
    }

    public void changeVisibility(Envs env){
        Status ston = Status.valueOf(env.toString()+"_ON");
        Status stoff = Status.valueOf(env.toString()+"_OFF");
        ArrayList<ImageView> viewsOn = sensors.get(ston);
        ArrayList<ImageView> viewsOff = sensors.get(stoff);
        ImageView firstelem = viewsOn.get(0);
        if (firstelem.getVisibility()==View.INVISIBLE){
            for (ImageView iv : viewsOn) {
                iv.setVisibility(View.VISIBLE);
                //new SubmitState().execute("http://");
            }
            for (ImageView iv : viewsOff) {
                iv.setVisibility(View.INVISIBLE);
                //new SubmitState().execute("http://");

            }
        } else{
            for (ImageView iv : viewsOff) {
                iv.setVisibility(View.VISIBLE);
                //new SubmitState().execute("http://");

            }
            for (ImageView iv : viewsOn) {
                iv.setVisibility(View.INVISIBLE);
                //new SubmitState().execute("http://");

            }
        }
    }
}
