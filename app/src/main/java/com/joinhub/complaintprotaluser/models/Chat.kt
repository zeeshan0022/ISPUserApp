package com.joinhub.complaintprotaluser.models

import java.sql.Timestamp

data class Chat(var sender:String="", var receiver:String="", var senderUid:String="", var  receiverUid: String="",
                var message:String="", var timestamp: Long=0)
