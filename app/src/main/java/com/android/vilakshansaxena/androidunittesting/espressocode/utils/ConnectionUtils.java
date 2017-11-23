package com.android.vilakshansaxena.androidunittesting.espressocode.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {

    private Context mContext;

    public ConnectionUtils(Context context) {
        mContext = context;
    }

    public boolean isNetConnected() {
        if (mContext != null) {
            final ConnectivityManager cm =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return !(networkInfo == null || !networkInfo.isConnectedOrConnecting());
        } else {
            return false;
        }
    }
}
