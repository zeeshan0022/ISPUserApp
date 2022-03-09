package com.joinhub.complaintprotaluser.interfaces

import com.joinhub.complaintprotaluser.models.PackageDetails

interface PackageInterface {
    fun onStarts()
    fun onSuccess(list:MutableList<PackageDetails>)
    fun onError(e:String)
}