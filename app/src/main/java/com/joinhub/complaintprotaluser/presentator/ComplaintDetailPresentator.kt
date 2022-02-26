package com.joinhub.complaintprotaluser.presentator

import android.app.Activity
import android.content.Context
import com.joinhub.complaintprotaluser.WebApis.ChangeComplaintStatusAPI
import com.joinhub.complaintprotaluser.interfaces.ComplaintDetailInterface

class ComplaintDetailPresentator(val complaintDetailInterface:ComplaintDetailInterface,
                                 context: Context, val activity: Activity) {

    fun saveData(id: Int){
        var result:String
        complaintDetailInterface.onStarts()
        Thread{
            val ap = ChangeComplaintStatusAPI()
            result= ap.saveData(id)
            activity.runOnUiThread {
                if(result== "true"){
                    complaintDetailInterface.onSuccess("Complaint Cancelled Successfully!!")
                }else{
                    complaintDetailInterface.onError(result)
                }
            }
        }.start()
    }
}