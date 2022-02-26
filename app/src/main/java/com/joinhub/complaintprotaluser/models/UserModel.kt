package com.joinhub.complaintprotaluser.models

data class UserModel(val userID:Int, val userName:String, val userFullName:String,
                     val userPass:String, val userCNIC:String, val userEmail:String,
                     val userPhone:String, val userAddress:String, val pkgID :Int, val areaID: Int)
