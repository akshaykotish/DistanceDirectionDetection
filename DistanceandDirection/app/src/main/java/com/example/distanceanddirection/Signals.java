package com.example.distanceanddirection;

import android.app.Activity;
import android.content.Context;
import android.content.*;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

public class Signals extends Activity {
    public void Strength()
    {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


// Level of a Scan Result
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (ScanResult scanResult : wifiList) {
            int level = WifiManager.calculateSignalLevel(scanResult.level, 5);
            System.out.println("Level is " + level + " out of 5");
        }

// Level of current connection
        int rssi = wifiManager.getConnectionInfo().getRssi();
        int level = WifiManager.calculateSignalLevel(rssi, 5);
        System.out.println("Level is " + level + " out of 5");
    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }
}
