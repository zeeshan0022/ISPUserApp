package com.joinhub.complaintprotaluser.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.*
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.WebApis.CheckApp
import com.joinhub.complaintprotaluser.models.Chat
import com.joinhub.complaintprotaluser.models.ManageApp
import com.joinhub.complaintprotaluser.receiver.RestartService
import com.joinhub.complaintprotaluser.utilties.Constants
import com.joinhub.complaintprotaluser.utilties.InternetConnection
import java.util.*

class ChatService :Service(){
    private lateinit var model: ManageApp
    var counter = 0
    lateinit var preference: Preference
    override fun onCreate() {
        super.onCreate()
        preference= Preference(this)
        getMessageFromFirebaseUser(preference.getStringpreference("userName",null),
        preference.getStringpreference("serviceUserName",null))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground(message:String) {
        val title =
            "New Message from "+preference.getStringpreference("serviceName",null)
        val text: String = message
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

        return Service.START_STICKY
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

    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getMessageFromFirebaseUser(senderUid: String, receiverUid: String) {
        val room_type_1: String = senderUid + "_" + receiverUid
        val room_type_2: String = receiverUid + "_" + senderUid

        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child(Constants.ARG_CHAT_ROOMS).ref.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                when {
                    dataSnapshot.hasChild(room_type_1) -> {

                        FirebaseDatabase.getInstance()
                            .reference
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(object : ChildEventListener {
                                override fun onChildAdded(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                    val chat = dataSnapshot.getValue(Chat::class.java)
                                   if(chat!!.senderUid==preference.getStringpreference("serviceUserName")){
                                    startMyOwnForeground(chat.message)
                                   }
                                }

                                override fun onChildChanged(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                                override fun onChildMoved(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    //      mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.message)
                                }
                            })
                    }
                    dataSnapshot.hasChild(room_type_2) -> {
                        FirebaseDatabase.getInstance()
                            .reference
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(object : ChildEventListener {
                                override fun onChildAdded(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                    val chat = dataSnapshot.getValue(Chat::class.java)
                                    //               mOnGetMessagesListener.onGetMessagesSuccess(chat)
                                    if(chat!!.senderUid==preference.getStringpreference("serviceUserName")){
                                        startMyOwnForeground(chat.message)
                                    }
                                }

                                override fun onChildChanged(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                                override fun onChildMoved(
                                    dataSnapshot: DataSnapshot,
                                    s: String?
                                ) {
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    //             mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.message)
                                }
                            })
                    }
                    else -> {

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //    mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.message)
            }
        })
    }

}