package com.joinhub.complaintprotaluser.models

import java.sql.Timestamp

data class ChatList(var chatRoomID:String="", var name:String="", var userName:String="",
                    var lastMessage:String="", var timestamp:Long=0, var senderId:String="", var isRead:Boolean=false)
