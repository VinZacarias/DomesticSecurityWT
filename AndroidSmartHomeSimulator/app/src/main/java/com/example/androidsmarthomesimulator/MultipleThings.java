package com.example.androidsmarthomesimulator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.webthings.webthing.Action;
import io.webthings.webthing.Event;
import io.webthings.webthing.Property;
import io.webthings.webthing.Thing;
import io.webthings.webthing.Value;
import io.webthings.webthing.WebThingServer;
import io.webthings.webthing.errors.PropertyError;
import java.util.Random;

/*
	Uso do exemplo MultipleThings para criação de um sistema de segurança doméstica.
	Fonte do código base <https://github.com/WebThingsIO/webthing-java/blob/master/src/main/java/io/webthings/webthing/example/MultipleThings.java>.
*/

public class MultipleThings {
    public static void main(String[] args) {
        Thing MotionSensor = new MotionSensor();
        Thing Camera = new Camera();
        Thing DoorSensor = new DoorSensor();
        Thing WindowSensor = new WindowSensor();
        Thing Siren = new Siren();

        try {
            List<Thing> things = new ArrayList<>();
            // modelo de adição de things: things.add(sensor);
            things.add(MotionSensor);
            things.add(Camera);
            things.add(DoorSensor);
            things.add(WindowSensor);
            things.add(Siren);

            // If adding more than one thing, use MultipleThings() with a name.
            // In the single thing case, the thing's name will be broadcast.
            WebThingServer server =
                    new WebThingServer(new WebThingServer.MultipleThings(things,
                            "HomeSecuritySystem"),
                            8888);

            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

            server.start(false);
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(1);
        }
    }

    public static class MotionSensor extends Thing {
        //https://github.com/KrzysztofZurek1973/webthing-motion-sensor exemplo de sensor de movimento pela WebThings
        private Value MotionDetected;
        private Value Counter;
        private int i = 0;
        private Siren s;
        public MotionSensor() {
            super("urn:dev:ops:motion-sensor-1",
                    "My Motion Sensor",
                    new JSONArray(Arrays.asList("OnOffSwitch", "MotionDetected")),
                    "A web connected Motion Sensor");

            try{
                JSONObject onDescription = new JSONObject();
                onDescription.put("@type", "OnOffProperty");
                onDescription.put("title", "On/Off");
                onDescription.put("type", "boolean");
                onDescription.put("description", "Whether the sensor is turned on");

                Value<Boolean> on = new Value<>(true,
                        // Here, you could send a signal to
                        // the GPIO that switches the lamp
                        // off
                        v -> System.out.printf(
                                "On-State is now %s\n",
                                v));

                this.addProperty(new Property(this, "on", on, onDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject motionDescription = new JSONObject();
                motionDescription.put("@type", "MotionProperty");
                motionDescription.put("title", "MotionDetection");
                motionDescription.put("type", "boolean");
                motionDescription.put("description", "Registers when the sensor captures an event");

                this.MotionDetected = new Value<>(false);

                this.addProperty(new Property(this, "Status", MotionDetected, motionDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject counterDescription = new JSONObject();
                counterDescription.put("@type", "LevelProperty"); //arrumar para uma propriedade de contagem.
                counterDescription.put("title", "AlarmCounter");
                counterDescription.put("type", "Integer");
                counterDescription.put("description", "Registers how many times the sensor has registered an event");
                counterDescription.put("Read-Only", "true");

                this.Counter = new Value<Integer>(0);
                this.addProperty(new Property(this, "Alarm Counter", Counter, counterDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(30000);
                        boolean currentStatus = this.readFromStatus();
                        if (currentStatus == true) {
                            System.out.printf("setting sensor status to: true\n");
                            this.Counter.notifyOfExternalUpdate(updateValue());
                            System.out.printf("setting sensor counter to: " + i +"\n");
                            s.setAlarm();
                        }
                        else System.out.printf("setting sensor status to: false\n");
                        this.MotionDetected.notifyOfExternalUpdate(currentStatus);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }).start();
        }

        private boolean readFromStatus() {
            double max = 0.8;
            double valor = Math.random();
            if(valor>=max) return true;
            else return false;
        }

        private int updateValue() {
            i++;
            return (Integer.valueOf(i));
        }
    }

    public static class Camera extends Thing {
        public Camera() {
            super("urn:dev:ops:camera-1",
                    "My Camera",
                    new JSONArray(Arrays.asList("OnOffSwitch")),
                    "A web connected Camera");

            try{
                JSONObject onDescription = new JSONObject();
                onDescription.put("@type", "OnOffProperty");
                onDescription.put("title", "On/Off");
                onDescription.put("type", "boolean");
                onDescription.put("description", "Whether the camera is turned on");

                Value<Boolean> on = new Value<>(true,
                        v -> System.out.printf(
                                "On-State is now %s\n",
                                v));

                this.addProperty(new Property(this, "on", on, onDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject imageDescription = new JSONObject();
                imageDescription.put("@type", "ImageProperty");
                imageDescription.put("title", "Image");
                imageDescription.put("type", "null");
                imageDescription.put("description", "Displays the camera's image");
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }
        }
    }

    public static class DoorSensor extends Thing{
        //https://github.com/KrzysztofZurek1973/webthing-door-sensor exemplo de sensor de movimento pela WebThings
        private Value MotionDetected;
        private Value Counter;
        private int i = 0;

        public DoorSensor() {
            super("urn:dev:ops:door-sensor-1",
                    "My Door Sensor",
                    new JSONArray(Arrays.asList("OnOffSwitch", "InteractionDetected")),
                    "A web connected Door Sensor");

            try{
                JSONObject onDescription = new JSONObject();
                onDescription.put("@type", "OnOffProperty");
                onDescription.put("title", "On/Off");
                onDescription.put("type", "boolean");
                onDescription.put("description", "Whether the sensor is turned on");

                Value<Boolean> on = new Value<>(true,
                        v -> System.out.printf(
                                "On-State is now %s\n",
                                v));
                this.addProperty(new Property(this, "on", on, onDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject motionDescription = new JSONObject();
                motionDescription.put("@type", "OpenProperty");
                motionDescription.put("title", "Interaction Detection");
                motionDescription.put("type", "boolean");
                motionDescription.put("false", "closed");
                motionDescription.put("true", "opened");
                motionDescription.put("description", "Registers when the sensor captures an event");

                this.MotionDetected = new Value<>(false);
                this.addProperty(new Property(this, "Status", MotionDetected, motionDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject counterDescription = new JSONObject();
                counterDescription.put("@type", "LevelProperty");
                counterDescription.put("title", "AlarmCounter");
                counterDescription.put("type", "Integer");
                counterDescription.put("description", "Registers how many times the sensor has registered an event");
                counterDescription.put("Read-Only", "true");

                this.Counter = new Value<Integer>(0);
                this.addProperty(new Property(this, "Alarm Counter", Counter, counterDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(60000);
                        boolean currentStatus = this.readFromStatus();
                        if (currentStatus == true) {
                            System.out.printf("setting Door Sensor status to: true\n");
                            this.Counter.notifyOfExternalUpdate(updateValue());
                            System.out.printf("setting sensor counter to: " + i +"\n");
                        }
                        else System.out.printf("setting Door Sensor status to: false\n");
                        this.MotionDetected.notifyOfExternalUpdate(currentStatus);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }).start();
        }

        private boolean readFromStatus() {
            double max = 0.8;
            double valor = Math.random();
            if(valor>=max) return true;
            else return false;
        }

        private int updateValue() {
            i++;
            return (Integer.valueOf(i));
        }
    }

    public static class WindowSensor extends Thing{
        private Value MotionDetected;
        private Value Counter;
        private int i = 0;

        public WindowSensor() {
            super("urn:dev:ops:window-sensor-1",
                    "My Window Sensor",
                    new JSONArray(Arrays.asList("OnOffSwitch", "InteractionDetected")),
                    "A web connected Window Sensor");

            try{
                JSONObject onDescription = new JSONObject();
                onDescription.put("@type", "OnOffProperty");
                onDescription.put("title", "On/Off");
                onDescription.put("type", "boolean");
                onDescription.put("description", "Whether the sensor is turned on");

                Value<Boolean> on = new Value<>(true,
                        v -> System.out.printf(
                                "On-State is now %s\n",
                                v));
                this.addProperty(new Property(this, "on", on, onDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject motionDescription = new JSONObject();
                motionDescription.put("@type", "OpenProperty");
                motionDescription.put("title", "Interaction Detection");
                motionDescription.put("type", "boolean");
                motionDescription.put("false", "closed");
                motionDescription.put("true", "opened");
                motionDescription.put("description", "Registers when the sensor captures an event");

                this.MotionDetected = new Value<>(false);
                this.addProperty(new Property(this, "Status", MotionDetected, motionDescription));

            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject counterDescription = new JSONObject();
                counterDescription.put("@type", "LevelProperty");
                counterDescription.put("title", "AlarmCounter");
                counterDescription.put("type", "Integer");
                counterDescription.put("description", "Registers how many times the sensor has registered an event");

                this.Counter = new Value<Integer>(0);
                this.addProperty(new Property(this, "Alarm Counter", Counter, counterDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(60000);
                        boolean currentStatus = this.readFromStatus();
                        if (currentStatus == true) {
                            System.out.printf("setting Door Sensor status to: true\n");
                            this.Counter.notifyOfExternalUpdate(updateValue());
                            System.out.printf("setting sensor counter to: " + i +"\n");
                        }
                        else System.out.printf("setting Window Sensor status to: false\n");
                        this.MotionDetected.notifyOfExternalUpdate(currentStatus);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }).start();
        }

        private boolean readFromStatus() {
            double max = 0.8;
            double valor = Math.random();
            if(valor>=max) return true;
            else return false;
        }

        private int updateValue() {
            i++;
            return (Integer.valueOf(i));
        }
    }

    public static class Siren extends Thing{
        private Value<Boolean> Triggered;

        public Siren() {
            super("urn:dev:ops:siren-1",
                    "My Siren",
                    new JSONArray(Arrays.asList("OnOffSwitch", "AlarmStatus")),
                    "A web connected Siren");

            try{
                JSONObject onDescription = new JSONObject();
                onDescription.put("@type", "OnOffProperty");
                onDescription.put("title", "On/Off");
                onDescription.put("type", "boolean");
                onDescription.put("description", "Whether the siren is turned on");

                Value<Boolean> on = new Value<>(true,
                        v -> System.out.printf(
                                "On-State is now %s\n",
                                v));
                this.addProperty(new Property(this, "on", on, onDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }

            try{
                JSONObject alarmDescription = new JSONObject();
                alarmDescription.put("@type", "AlarmProperty");
                alarmDescription.put("title", "Alarm Alert");
                alarmDescription.put("type", "boolean");
                alarmDescription.put("active", "true");
                alarmDescription.put("unactive", "false");
                alarmDescription.put("Read-Only", "true");
                alarmDescription.put("description", "Sets of the siren when an alarm is triggered");

                this.Triggered = new Value<>(false);
                this.addProperty(new Property(this, "Status", Triggered, alarmDescription));
            }
            catch (JSONException e) {
                System.out.println(e.toString());
                System.exit(1);
            }
        }

        public void setAlarm() {
            boolean sim = true;
            boolean nao = false;
            this.Triggered.notifyOfExternalUpdate(sim);
            System.out.println("The siren has been activated");
            try{
                Thread.sleep(10000);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            this.Triggered.notifyOfExternalUpdate(nao);
            System.out.println("The siren has been deactivated");
        }
    }
}
