package com.joinhub.complaintprotaluser.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

import android.widget.Toast
import com.joinhub.complaintprotaluser.services.ComplaintService


class RestartService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(Intent(context, ComplaintService::class.java))
        } else {
            context!!.startService(Intent(context, ComplaintService::class.java))
        }
    }
}