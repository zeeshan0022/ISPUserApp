package com.joinhub.complaintprotaluser.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.WebApis.CheckApp
import com.joinhub.complaintprotaluser.models.ManageApp
import com.joinhub.complaintprotaluser.receiver.RestartService
import com.joinhub.complaintprotaluser.utilties.InternetConnection
import java.util.*


class ComplaintService: Service() {
    private lateinit var model: ManageApp
    var counter = 0
    lateinit var preference:Preference
    override fun onCreate() {
        super.onCreate()
        startTimer()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val title =
           "Maintenance Alert"
        val text: String = "Hello"
        val channel_id = "HEAD_UP_NOTIFICATION"
        var channel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                channel_id,
                " Friends Internet",
                NotificationManager.IMPORTANCE_HIGH
            )
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(
            channel!!
        )
        val builder: Notification.Builder = Notification.Builder(this, channel_id)
            .setContentText(text)
            .setContentTitle(title).setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher)
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        val broadcastIntent = Intent()
        broadcastIntent.action = "RestartService"
        broadcastIntent.setClass(this, RestartService::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    fun startTimer() {
        preference= Preference(this)
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {


                if(InternetConnection.amIConnected(applicationContext)){
                    checkMaintain()
                }
            }
        }
        timer!!.schedule(timerTask, 100000, 1000000) //
    }


    private fun checkMaintain() {
        if(preference.getIntpreference("areaID")>0) {
            Log.d("Notice:", "1")
            Thread {
                val api: CheckApp = CheckApp()
                val obj = api.loadData(preference.getIntpreference("areaID"))
                if (obj != null) {
                    if (obj!!.getProperty("id") != null) {
                        model = ManageApp(
                            Integer.parseInt(obj.getProperty("id").toString()),
                            Integer.parseInt(obj.getProperty("areaID").toString()),
                            obj.getPropertyAsString("status"),
                            obj.getPropertyAsString("date"),
                            obj.getPropertyAsString("city"),
                            obj.getPropertyAsString("startFrom"),
                            obj.getPropertyAsString("toEnd")
                        )

                        if (model.id > 0) {
                            if (preference.getIntpreference("id") != model.id) {
                                preference.setIntpreference("id", model.id)
                                Log.d("Notice:", "2")
                                startMyOwnForeground()

                            }
                        }

                    }
                }
            }.start()
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}