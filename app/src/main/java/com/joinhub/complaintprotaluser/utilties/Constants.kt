package com.joinhub.complaintprotaluser.utilties

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.joinhub.complaintprotaluser.R


class Constants {

    companion object {
        const val WSDL_TARGET_NAMESPACE = "http://tempuri.org/"

        const val SOAP_ADDRESS = "http://192.168.0.102:2020/WebService1.asmx"


        fun checkGoogleAPI(activity: Activity): Boolean{
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
            if (status != ConnectionResult.SUCCESS) {

                return false
            }
            return true
        }
        fun darkThemeStyle(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window: Window = activity.window
                val decorView: View = window.decorView
                val wic = WindowInsetsControllerCompat(window, decorView)
                wic.isAppearanceLightStatusBars = false // true or false as desired.

                // And then you can set any background color to the status bar.
                window.statusBarColor = activity.getColor(R.color.backDark)
                window.navigationBarColor = activity.getColor(R.color.backDark)

            }
        }

        fun lightThemeStyle(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window: Window = activity.window
                val decorView: View = window.decorView
                val wic = WindowInsetsControllerCompat(window, decorView)
                wic.isAppearanceLightStatusBars = true // true or false as desired.

                // And then you can set any background color to the status bar.
                window.statusBarColor = activity.getColor(R.color.back)
                window.navigationBarColor = activity.getColor(R.color.back)
            }
        }


        fun getWifiSSDID(context: Context): String {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                return "ID"
            } else {
                val wifiManager: WifiManager =
                    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifiManager.connectionInfo
                return info.ssid
            }


        }
    }
}