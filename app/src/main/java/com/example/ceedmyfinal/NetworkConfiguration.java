package com.example.ceedmyfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConfiguration
{
    private static Context context;

    NetworkConfiguration(Context context)
    {
        this.context = context;
    }

    public boolean isConnected()
    {
        boolean connected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
        }

        return connected;
    }

}
