package com.example.surface4pro.bakingtime.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

/**
 * Class containing methods regarding the network status.
 */
public final class NetworkStatus {

    /**
     * This method checks if an Internet connection is available.
     *
     * @param context a context
     * @return a boolean returning true if a network connection is available
     */
    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager;
        boolean connected;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo netInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            connected = netInfo != null && netInfo.isAvailable() &&
                    netInfo.isConnected();
            return connected;
        } catch (Exception e) {
            return false;
        }
    }
}
