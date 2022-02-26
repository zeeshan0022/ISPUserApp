package com.joinhub.complaintprotaluser.interfaces

import com.joinhub.complaintprotaluser.models.ComplaintModel

interface ComplaintHistoryInterface {

    fun onStarts()
    fun onError(e:String)
    fun onSuccess(list:List<ComplaintModel>)
}