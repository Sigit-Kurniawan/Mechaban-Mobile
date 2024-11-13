package com.sigit.mechaban.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

public class Connection {
    private final Context context;

    public Connection(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                Network network = manager.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
                    return capabilities != null &&
                            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
                }
            }
            return false;
        } catch (NullPointerException e) {
            Log.e("Connection", e.toString());
            return false;
        }
    }
}
