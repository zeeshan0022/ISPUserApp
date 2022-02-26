package com.joinhub.complaintprotaluser.interfaces

import com.joinhub.complaintprotaluser.models.ServiceModel
import com.joinhub.complaintprotaluser.models.UserModel

interface HomeInterface {
    fun onError(e:String)
    fun onSuccess(model:UserModel)
    fun onStarts()
    fun showProgress()
    fun hideProgress()
    fun onSRLoadSuccess(model:ServiceModel)
}