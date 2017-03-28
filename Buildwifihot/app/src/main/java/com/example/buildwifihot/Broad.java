package com.example.buildwifihot;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dong on 2016/9/5.
 */
public class Broad {
    private static int reachableTimeout=300;
    private String TAG = "Broad";
    private static Context context = null;
    private String mSSID = "";
    private String mPasswd = "";

    public static void registerNetStateReceiver() {

        ConnectionChangeReceiver mConnectivityReceiver = new ConnectionChangeReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(mConnectivityReceiver, filter);

    }

    private static void registerReceiver(ConnectionChangeReceiver mConnectivityReceiver, IntentFilter filter) {
    }

    public static class ConnectionChangeReceiver extends BroadcastReceiver {


        @Override

        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.
                    getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "有网络连接", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public  class MyService extends Service{

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        public void onCreate(){
            super.onCreate();
        }
        public int onStartCommand(Intent intent,int flags,int startId){
           // ArrayList<String> connectedIP = getConnectedIP();

                StringBuilder resultList = new StringBuilder();
                for (String ip : connectedIP) {
                    resultList.append(ip);
                    resultList.append("\n");

                Toast.makeText(context,resultList,Toast.LENGTH_SHORT).show();
            }
            return  super.onStartCommand(intent,flags,startId);
        }
        public void onDestroy(){
            super.onDestroy();
        }
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
                     // boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(reachableTimeout);
                        StringBuilder sb=new StringBuilder();
                        sb.append(ip+"\n"+mac);
                        connectedIP.add(String.valueOf(sb));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return connectedIP;
        }

        static ArrayList<String> connectedIP = getConnectedIP();

        public static void getLinkList() {
            StringBuilder resultList = new StringBuilder();
            for (String ip : connectedIP) {
                resultList.append(ip);
                resultList.append("\n");
            }
           Toast.makeText(context,resultList,Toast.LENGTH_SHORT).show();
        }

        public void WifiCanScan() {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            List<ScanResult> scanResultList = wifiManager.getScanResults();
            if (scanResultList != null && scanResultList.size() > 0) {
                for (int i = 0; i < scanResultList.size(); i++) {
                    ScanResult scanResult = scanResultList.get(i);

                    StringBuffer str = new StringBuffer()
                            .append("SSID: " + scanResult.SSID).append("\n");

                    Log.i(TAG, str.toString());

                    if (scanResult.SSID.equals(mSSID)) {
                        Toast.makeText(context, "有相同的SSID", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }




        }

