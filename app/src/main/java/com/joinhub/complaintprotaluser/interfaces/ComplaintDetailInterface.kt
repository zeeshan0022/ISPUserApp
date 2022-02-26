package com.joinhub.complaintprotaluser.interfaces

interface ComplaintDetailInterface {
    fun onStarts()
    fun onSuccess(status:String)
    fun onError(e:String)

}