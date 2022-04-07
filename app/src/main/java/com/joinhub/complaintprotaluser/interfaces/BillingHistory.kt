package com.joinhub.complaintprotaluser.interfaces

interface BillingHistory<E> {
    fun onStarts()
    fun onSuccess(list:MutableList<E>)
    fun onError(e: String)

}