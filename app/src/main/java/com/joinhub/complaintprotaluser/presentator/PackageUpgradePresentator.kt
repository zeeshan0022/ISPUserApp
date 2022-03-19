package com.joinhub.complaintprotaluser.presentator

import android.app.Activity
import com.joinhub.complaintprotaluser.WebApis.UpgradePackageDetails
import com.joinhub.complaintprotaluser.interfaces.PackageUpgradeInterface
import com.joinhub.complaintprotaluser.utilties.Constants
import java.text.SimpleDateFormat
import java.util.*

class PackageUpgradePresentatorval(val interfaces: PackageUpgradeInterface, private val activity: Activity) {


    fun upgradePackage(userID:Int , pkgID:Int, method:String, charges:Double){
        interfaces.onStarts()
        val api = UpgradePackageDetails()
        Thread{
            val result= api.saveData(pkgID, method,Constants.getDate() ,"Paid", userID, charges,
            Constants.getMonth(), Constants.getYear())

            activity.runOnUiThread {
                if(result=="true"){
                    interfaces.onSuccess("Package Upgrade Successfully. Please Restart Router")
                }else {
                    interfaces.onSuccess(result)
                }
            }
        }.start()

    }
}