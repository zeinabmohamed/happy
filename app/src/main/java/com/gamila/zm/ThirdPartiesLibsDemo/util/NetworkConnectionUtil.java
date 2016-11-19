package com.gamila.zm.ThirdPartiesLibsDemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by zeinab on 4/30/2016.
 */
public class NetworkConnectionUtil {


    private static final String TAG = NetworkConnectionUtil.class.getName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        Log.d(TAG, "isNetworkAvailable() returned: " + (activeNetworkInfo != null && activeNetworkInfo.isConnected()));
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
