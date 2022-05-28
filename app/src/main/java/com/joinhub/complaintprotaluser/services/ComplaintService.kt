package com.joinhub.complaintprotaluser.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.joinhub.alphavpn.utility.Preference
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
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
//            1,
//            Notification()
//        )
        startTimer()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)

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
        timer!!.schedule(timerTask, 1000000, 1000000) //
    }

    fun startNotificationListener() {
        //start's a new thread
        Thread { //fetching notifications from server
            buildNotice()
        }.start()
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

                                startNotificationListener()
                            }
                        }

                    }
                }
            }.start()
        }
    }

    private fun buildNotice() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification = NotificationCompat.Builder(baseContext, "notification_id")
            .setSmallIcon(android.R.drawable.ic_notification_clear_all)
            .setContentTitle("title")
            .setContentText("content")
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .build()
        notificationManager.notify(0, notification)
    }

    fun stoptimertask() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}