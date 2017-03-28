package com.example.buildwifihot;


import java.util.HashSet;


public class WiFiAPObserver implements WiFiAPListener {

    /**
     * the set to save all registed listener
     */
    private HashSet<WiFiAPListener> listenerSet = new HashSet<WiFiAPListener>();

    /**
     * add wiFiAPListener
     * @param wiFiAPListener
     */
    public void addWiFiAPListener(WiFiAPListener wiFiAPListener) {
        if (!listenerSet.contains(wiFiAPListener)) {
            listenerSet.add(wiFiAPListener);
        }
    }

    /**
     * remove the wiFiAPListener
     * @param wiFiAPListener
     */
    public void removeWiFiAPListener(WiFiAPListener wiFiAPListener) {
        if (listenerSet.contains(wiFiAPListener)) {
            listenerSet.remove(wiFiAPListener);
        }
    }

    /**
     * remove all WiFiAPListener
     */
    public void clearWiFiAPListener() {
        listenerSet.clear();
    }

    @Override
    public void stateChanged(int state) {
        //notify all Listener the state changed
        for (WiFiAPListener wiFiAPListener : listenerSet) {
            wiFiAPListener.stateChanged(state);
        }
    }

}
