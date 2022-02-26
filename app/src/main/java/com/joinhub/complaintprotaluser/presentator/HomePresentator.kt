package com.joinhub.complaintprotaluser.presentator

import android.app.Activity
import com.joinhub.complaintprotaluser.WebApis.LoadSRDetail
import com.joinhub.complaintprotaluser.WebApis.LoadUserData
import com.joinhub.complaintprotaluser.interfaces.HomeInterface
import com.joinhub.complaintprotaluser.models.ServiceModel
import com.joinhub.complaintprotaluser.models.UserModel
import org.ksoap2.serialization.SoapPrimitive
import javax.xml.parsers.ParserConfigurationException


class HomePresentator(
    private var loginInterface: HomeInterface,
    private val activity: Activity
) {
    private lateinit var load:LoadUserData
    private lateinit var model: UserModel
    fun loadData(userName:String){
        load = LoadUserData()
        loginInterface.showProgress()
        Thread {
            val m = load.loadData(userName)
            activity.runOnUiThread {
                when {
                    Integer.parseInt(m.getProperty("userID").toString())==0 -> {
                        loginInterface.onError("Error in Loading data")
                    }
                    Integer.parseInt(m.getProperty("userID").toString()) >0 -> {
                        try {
                            model = UserModel(
                                Integer.parseInt(m.getProperty("userID").toString()),
                                m.getProperty("userName").toString(),
                                m.getProperty("userFullName").toString(),
                                m.getProperty("userPass").toString(),
                                m.getProperty("userCNIC").toString(),
                                m.getProperty("userEmail").toString(),
                                m.getProperty("userPhone").toString(),
                                m.getProperty("userAddress").toString(),
                                Integer.parseInt(m.getProperty("pkgID").toString()),
                                Integer.parseInt(m.getProperty("areaID").toString())

                            )
                            loginInterface.onSuccess(model)
                        } catch (e: ParserConfigurationException) {
                            e.printStackTrace()
                        }
                    }
                    else -> {
                        loginInterface.onError(m.toString())
                    }
                }
            }
        }.start()
    }

    fun loadSRData(areaID: Int) {
        val apiSR = LoadSRDetail()
        Thread {
            val obj = apiSR.loadData(areaID)
        activity.runOnUiThread {
            if(Integer.parseInt(obj.getProperty("serviceID").toString())>0){
                loginInterface.onSRLoadSuccess(ServiceModel(
                    Integer.parseInt(obj.getProperty("serviceID").toString()),
                    obj.getProperty("serviceUserName").toString(),
                    obj.getProperty("serviceName").toString(),
                    obj.getProperty("serviceEmail").toString(),
                    "",
                    obj.getProperty("serviceCNIC").toString(),
                    obj.getProperty("servicePhone").toString(),
                    Integer.parseInt(obj.getProperty("areaID").toString())
                ))


            }else{
                loginInterface.onError(obj.toString())
            }
        }
        }.start()
    }


}