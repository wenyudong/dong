package com.example.buildwifihot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;


public  class MyService extends Service {

    private static int reachableTimeout=300;
    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate(){
        super.onCreate();
    }
    public int onStartCommand(Intent intent,int flags,int startId){
        ArrayList<String> connectedIP = getConnectedIP();

        StringBuilder resultList = new StringBuilder();
        for (String ip : connectedIP) {
            resultList.append(ip);
            resultList.append("\n");

            Toast.makeText(context,resultList,Toast.LENGTH_SHORT).show();
        }
        return  super.onStartCommand(intent,flags,startId);
    }
    public static ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    String mac = splitted[3];
                    boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(reachableTimeout);
                    StringBuilder sb=new StringBuilder();
                    sb.append(ip+"\n");
                    sb.append(mac+"\n");
                    sb.append(isReachable);
                    connectedIP.add(String.valueOf(sb));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }
    public void onDestroy(){
        super.onDestroy();
    }
}
