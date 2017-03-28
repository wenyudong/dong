package com.example.buildwifihot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import WifiHotManage.ClientScanResult;
import WifiHotManage.FinishScanListener;
import WifiHotManage.WifiApManager;


public class MainActivity extends Activity {
    private Button button;
    private String mSSID = "";
    private String mPasswd = "wen911021";
    private Context context = null;
    private static String TAG = "MainActivity";
    private boolean isWifiOpen = false;
    private StringBuilder resultList;
    TextView textView1;
    private WifiApManager wifiApManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.textView1);
        WiFiAPService.startService(this);
        context = this;
        button = (Button) findViewById(R.id.start);
        button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                WifiApAdmin wifiAp = new WifiApAdmin(context);
                mSSID = wifiAp.setSSID();
                wifiAp.Wifistart(mSSID, mPasswd, 3);

            }
        });
        Broad.registerNetStateReceiver();
        WiFiAPService.addWiFiAPListener(new WiFiAPListener() {

            @Override
            public void stateChanged(int state) {
                Log.i(TAG, "state= " + state);
                switch (state) {
                    case WiFiAPListener.WIFI_AP_OPEN_SUCCESS:
                        isWifiOpen = true;
                        Toast.makeText(MainActivity.this, "WiFi热点已开启！",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case WiFiAPListener.WIFI_AP_CLOSE_SUCCESS:
                        isWifiOpen = false;
                        Toast.makeText(MainActivity.this, "WiFi已关闭！",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

      // scan();
    }
        private void scan() {
            wifiApManager.getClientList(false, new FinishScanListener() {

                @Override
                public void onFinishScan(final ArrayList<ClientScanResult> clients) {

                    //textView1.setText("WifiApState: " + wifiApManager.getWifiApState() + "\n\n");
                    textView1.append("Clients: \n");
                    for (ClientScanResult clientScanResult : clients) {
                        textView1.append("####################\n");
                        textView1.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
                        textView1.append("Device: " + clientScanResult.getDevice() + "\n");
                        textView1.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
                        textView1.append("isReachable: " + clientScanResult.isReachable() + "\n");
                    }
                }
            });
        }



    public void onDestroy(){
        super.onDestroy();
        WiFiAPService.stopService(this);

    }


}










