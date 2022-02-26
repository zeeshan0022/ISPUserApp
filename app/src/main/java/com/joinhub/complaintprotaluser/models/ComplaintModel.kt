package com.joinhub.complaintprotaluser.models

data class ComplaintModel(val complaintID:Int, val complaintTicketNo:String, val complaintName:String,
                          val complaintPhone:String, val complaintEmail:String,
                          val complaintLong:String, val complaintLatn:String,
                          val complaintIssue:String, val complaintDesc:String, var complaintStatus:String,
                          val serviceID:Int,
                          val userID:Int, val date:String)
