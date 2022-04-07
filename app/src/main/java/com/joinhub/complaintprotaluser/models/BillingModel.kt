package com.joinhub.complaintprotaluser.models

import java.net.IDN

data class BillingModel(val billingID:Int, val userID:Int, val billingMethod:String,
                        val billingDate:String, val pkgID:Int, val pkgName:String, val charges:Double, val status:String,
                        val month:String,
                        val year:String)
