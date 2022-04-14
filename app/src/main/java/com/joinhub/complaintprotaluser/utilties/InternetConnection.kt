package com.joinhub.complaintprotaluser.utilties

import android.content.Context
import android.net.ConnectivityManager

class InternetConnection {

    companion object{
         fun amIConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

}