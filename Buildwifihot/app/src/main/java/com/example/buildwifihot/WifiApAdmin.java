package com.example.buildwifihot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import WifiHotManage.ClientScanResult;
import WifiHotManage.FinishScanListener;
import WifiHotManage.WifiApManager;

public class WifiApAdmin {
    private WifiManager wifiManager = null;
    private String TAG = "WifiApAdmin";
    private String mSSID = "";
    private String mPasswd = "wen911021";
    private Context context;

    public WifiApAdmin(Context context) {

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

    }

    public WifiConfiguration getCustomeWifiConfiguration(String ssid,
                                                         String passwd, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = ssid;
        if (type == 1) // NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 2) // WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = passwd;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 3) // WPA
        {
            config.preSharedKey = passwd;
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;

    }

    public void Wifistart(String ssid, String psd, int type) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        Method method1 = null;
        try {
            method1 = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = getCustomeWifiConfiguration(ssid,
                    psd, type);

            method1.invoke(wifiManager, netConfig, true);

        } catch (Exception e) {
            e.printStackTrace();

        }

    MyTimerCheck timerCheck = new MyTimerCheck() {

        public void doTimerCheckWork() {

            if (isWifiApEnabled(wifiManager)) {

                Log.v(TAG, "Wifi enabled success!");
                //scan();
                this.exit();

            } else {

                Log.v(TAG, "Wifi enabled failed!");
                mSSID = setSSID();
                Wifistart(mSSID, mPasswd, 3);
                }

            }


        public void doTimeOutWork() {


            this.exit();

        }

    };
    timerCheck.start(15,1000);


    }
    WifiApManager wifiApManager;
    private void scan() {
        wifiApManager.getClientList(false, new FinishScanListener() {

            public void onFinishScan(final ArrayList<ClientScanResult> clients) {

               // textView1.setText("WifiApState: " + wifiApManager.getWifiApState() + "\n\n");
                //textView1.append("Clients: \n");
                for (ClientScanResult clientScanResult : clients) {
                    Toast.makeText(context,clientScanResult.getHWAddr(),Toast.LENGTH_SHORT).show();
                    /*textView1.append("####################\n");
                    textView1.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
                    textView1.append("Device: " + clientScanResult.getDevice() + "\n");
                    textView1.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
                    textView1.append("isReachable: " + clientScanResult.isReachable() + "\n");*/

                }
            }
        });
    }

    public  static String setSSID() {

        Date date=new Date();
        String a=String.format("%te", date);

        String b=String.format("%tY", date);

        String c=String.format("%tj", date);
        String d=String.format("%tm", date);

        String e=String.format("%td", date);
        String f=String.format("%ty", date);
        String h=String.format("%tH", date);

        String ssid;
        return ssid=a+b+c+d+e+f+h;
    }
    public void maintain(){

    }



    public static void isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取状态
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        //判断wifi已连接的条件
        if (wifi == NetworkInfo.State.CONNECTED) {
            Toast.makeText(context, "有连接", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "无连接", Toast.LENGTH_SHORT).show();
        }
    }
    public void closeWifiAp(WifiManager wifiManager) {
        if (isWifiApEnabled(wifiManager)) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);

                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private static boolean isWifiApEnabled(WifiManager wifiManager) {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);

        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}


