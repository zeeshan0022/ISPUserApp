package com.joinhub.complaintprotaluser.interfaces

import com.joinhub.complaintprotaluser.models.PackageDetails

interface PackageUpgradeInterface {

    fun onStarts()
    fun onSuccess(message:String)
    fun onError(e:String)
}