package com.joinhub.complaintprotaluser.presentator

import android.app.Activity
import com.joinhub.complaintprotaluser.WebApis.LoadBillingHistory
import com.joinhub.complaintprotaluser.interfaces.BillingHistory
import com.joinhub.complaintprotaluser.models.BillingModel
import com.joinhub.complaintprotaluser.models.ComplaintModel
import org.ksoap2.serialization.SoapObject

class BillingPresentator(private val inter:BillingHistory<BillingModel>, val activity: Activity) {
    lateinit var api: LoadBillingHistory
    lateinit var list: MutableList<BillingModel>
    fun loadHistory(userID: Int){
        list= mutableListOf()
        api= LoadBillingHistory()
        inter.onStarts()
        Thread{
            val root= api.loadData(userID)
            activity.runOnUiThread {
                for ( index in 0 until root.propertyCount){
                    val childObj: SoapObject= root.getProperty(index) as SoapObject
                    list.add(
                        BillingModel(Integer.parseInt(childObj.getProperty("billingID").toString()),
                        Integer.parseInt(childObj.getProperty("userID").toString()),
                            childObj.getProperty("billingMethod").toString() ,
                        childObj.getProperty("billingDate").toString(),Integer.parseInt(childObj.getPropertyAsString("pkgID")),
                        childObj.getPropertyAsString("pkgName"),childObj.getPropertyAsString("charges").toDouble(),
                        childObj.getPropertyAsString("status"),childObj.getPropertyAsString("month"),childObj.getPropertyAsString("year")))
                }
                if(list.isEmpty()){
                    inter.onError("No Data")
                }else{

                    inter.onSuccess(list)
                }
            }
        }.start()
    }
}