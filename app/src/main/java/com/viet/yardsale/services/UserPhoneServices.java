package com.viet.yardsale.services;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Viet on 6/24/2015.
 */
public class UserPhoneServices {

    public static boolean isInternetConnectionOn(Activity activity){
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);//Check Internet service
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null);
    }

    public static boolean isAllGPSservicesOn(Activity activity){
        LocationManager manager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);//Check Satellite GPS
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return statusOfGPS;
    }

    public static void turnGPSon(Activity activity){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");// turn on wireless network location source
        intent.putExtra("enabled", true);
        activity.sendBroadcast(intent);
    }

    public static void turnGPSoff(Activity activity){
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        activity.sendBroadcast(intent);
    }
}
