package com.bespokesoftbd.mamun_rex.general_knowledge.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NoInternet {

    public static boolean checkConnection(Context context){
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null){
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return true;
            }else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;
    }

}
