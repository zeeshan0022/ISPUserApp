package com.joinhub.alphavpn.utility

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class Preference (private val context:Context){
    var appSharedPrefs: SharedPreferences? = context.getSharedPreferences("SpeedVPN_pref", Activity.MODE_PRIVATE)
    var prefsEditor: SharedPreferences.Editor? =appSharedPrefs!!.edit();

    var isSync = false


    fun checkPreferenceSet(key_value: String?): Boolean {
        return appSharedPrefs!!.contains(key_value)
    }

    fun isBooleenPreference(key_value: String?): Boolean {
        return appSharedPrefs!!.getBoolean(key_value, false)
    }

    fun setBooleanpreference(key_value: String?, defult_value: Boolean) {
        prefsEditor!!.putBoolean(key_value, defult_value).commit()
    }

    fun getIntpreference(key_value: String?): Int {
        return appSharedPrefs!!.getInt(key_value, 0)
    }

    fun setIntpreference(key_value: String?, defult_value: Int) {
        prefsEditor!!.putInt(key_value, defult_value).commit()
    }


    fun getStringpreference(key_value: String?, default_value: String?): String? {
        return appSharedPrefs!!.getString(key_value, default_value)
    }

    fun getStringpreference(key_value: String?): String? {
        return appSharedPrefs!!.getString(key_value, "")
    }


    fun setStringpreference(key_value: String?, defult_value: String?) {
        prefsEditor!!.putString(key_value, defult_value).commit()
    }

    fun getLongpreference(key_value: String?): Long {
        return appSharedPrefs!!.getLong(key_value, -1)
    }


    fun setLongpreference(key_value: String?, defult_value: Long?) {
        prefsEditor!!.putLong(key_value, defult_value!!).commit()
    }
}