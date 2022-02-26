package com.joinhub.complaintprotaluser.models

data class ServiceModel(val serviceID:Int, val serviceUserName:String, val serviceName:String,
                        val serviceEmail:String, val servicePass:String="",
                        val serviceCNIC:String, val servicePhone:String, val areaID:Int)
