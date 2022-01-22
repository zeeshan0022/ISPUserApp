package com.joinhub.complaintprotaluser.interfaces

interface LoginInterface {

    fun onError(e:String)
    fun onSuccess()
    fun onStarts()
    fun showProgress()
    fun hideProgress()
}