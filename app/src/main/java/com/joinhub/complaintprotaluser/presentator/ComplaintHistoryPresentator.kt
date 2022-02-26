package com.joinhub.complaintprotaluser.presentator

import android.app.Activity
import android.content.Context
import com.joinhub.complaintprotaluser.WebApis.LoadComplaintHistory
import com.joinhub.complaintprotaluser.interfaces.ComplaintHistoryInterface
import com.joinhub.complaintprotaluser.models.ComplaintModel
import org.ksoap2.serialization.SoapObject

class ComplaintHistoryPresentator(val complaintHistoryInterface: ComplaintHistoryInterface, context: Context, val activity: Activity) {

     lateinit var list: MutableList<ComplaintModel>
    fun loadComplaint(id:Int){
        list= mutableListOf()
       complaintHistoryInterface.onStarts()
        val api = LoadComplaintHistory()
        var rootObj: SoapObject
        Thread{
            rootObj= api.loadData(id)
            activity.runOnUiThread {
                for ( index in 0 until rootObj.propertyCount){
                    val childObj: SoapObject= rootObj.getProperty(index) as SoapObject
                    list.add(ComplaintModel(Integer.parseInt(childObj.getProperty("complaintID").toString()),
                        childObj.getProperty("complaintTicketNo").toString(),childObj.getProperty("complaintName").toString() ,
                        childObj.getProperty("complaintPhone").toString(),childObj.getProperty("complaintEmail").toString(),
                        childObj.getProperty("complaintLong").toString(), childObj.getProperty("complaintLatn").toString(),
                        childObj.getProperty("complaintIssue").toString(),childObj.getProperty("complaintDesc").toString(),
                        childObj.getProperty("complaintStatus").toString(),Integer.parseInt(childObj.getProperty("serviceID").toString()),
                        Integer.parseInt(childObj.getProperty("userID").toString()), childObj.getProperty("date").toString()))
                }
                if(list.isEmpty()){

                    complaintHistoryInterface.onError(rootObj.toString())
                }else{

                    complaintHistoryInterface.onSuccess(list)
                }
            }
        }.start()

    }
}