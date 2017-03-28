package com.example.buildwifihot;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;


public class WiFiAPService extends Service {

    private static String TAG = "WiFiAPService";

    public static String ACTION_START_SERVICE = "action_start_service";
    public static String ACTION_STOP_SERVICE = "action_stop_service";
    private static WiFiAPObserver wiFiAPObserver = new WiFiAPObserver();
    private static boolean isWifiOpen = false;
    private static Context context;

    /**
     * static method to start service
     * @param context
     */
    public static void startService(Context context) {
        Intent intent = new Intent(context, WiFiAPService.class);
        intent.setAction(ACTION_START_SERVICE);
        context.startService(intent);
    }

    /**
     * static method to stop service
     * @param context
     */
    public static void stopService(Context context) {
        Intent intent = new Intent(context, WiFiAPService.class);
        intent.setAction(ACTION_STOP_SERVICE);
        context.startService(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "intent.getAction()="+intent.getAction());
        //重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        registerReceiver(wifiReceiver,new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED"));
        super.onCreate();
    }



    @Override
    public void onDestroy() {
        unregisterReceiver(wifiReceiver);
        wiFiAPObserver.clearWiFiAPListener();
        super.onDestroy();
    }

    /**
     * register wiFiAPListener
     * @param wiFiAPListener
     */
    public static void addWiFiAPListener(WiFiAPListener wiFiAPListener) {
        wiFiAPObserver.addWiFiAPListener(wiFiAPListener);

    }

    /**
     * remove wiFiAPListener
     * @param wiFiAPListener
     */
    public static void removeWiFiAPListener(WiFiAPListener wiFiAPListener) {
        wiFiAPObserver.removeWiFiAPListener(wiFiAPListener);
    }


    private BroadcastReceiver wifiReceiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                //便携式热点的状态为：10---正在关闭；11---已关闭；12---正在开启；13---已开启
                int state = intent.getIntExtra("wifi_state",  0);
                Log.i(TAG, "state= "+state);
                wiFiAPObserver.stateChanged(state);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}