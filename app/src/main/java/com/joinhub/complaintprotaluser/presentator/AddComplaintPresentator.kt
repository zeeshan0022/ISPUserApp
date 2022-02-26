package com.joinhub.complaintprotaluser.presentator

import android.app.Activity
import android.content.Context
import com.joinhub.complaintprotaluser.WebApis.AddComplaintAPI
import com.joinhub.complaintprotaluser.interfaces.AddComplaintInterface
import com.joinhub.complaintprotaluser.models.ComplaintModel

class AddComplaintPresentator(val interfaces:AddComplaintInterface, context:Context, val activity:Activity) {
    lateinit var api: AddComplaintAPI
    lateinit var response: String
    fun saveToDatabase(model :ComplaintModel){

        interfaces.onStarts()
        Thread{
            api= AddComplaintAPI()
            response=api.saveData(model)
            activity.runOnUiThread {
                if(response=="true")
                {
                    interfaces.onSuccess()
                }else{
                    interfaces.onError(response)
                }
            }
        }.start()
    }

}