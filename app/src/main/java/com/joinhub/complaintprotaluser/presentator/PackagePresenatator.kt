package com.joinhub.complaintprotaluser.presentator

import android.app.Activity
import com.joinhub.complaintprotaluser.WebApis.LoadPackageList
import com.joinhub.complaintprotaluser.interfaces.PackageInterface
import com.joinhub.complaintprotaluser.models.PackageDetails
import org.ksoap2.serialization.SoapObject

class PackagePresenatator(val interfaces: PackageInterface, private val activity:Activity) {

    lateinit var list:MutableList<PackageDetails>
    lateinit var loadPackageList:LoadPackageList
    fun loadAllPack(value:Boolean){
        interfaces.onStarts()
        list= mutableListOf()
        Thread{
            loadPackageList= LoadPackageList()
            val root= loadPackageList.loadData(value)
            activity.runOnUiThread {
                for ( index in 0 until root.propertyCount){
                    val childObj: SoapObject= root.getProperty(index) as SoapObject
                    list.add(
                        PackageDetails(Integer.parseInt(childObj.getProperty("pkgID").toString()),
                            childObj.getPropertyAsString("pkgName"),
                            childObj.getPropertyAsString("pkgDesc"),
                            childObj.getPropertyAsString("pkgSpeed"),
                            childObj.getPropertyAsString("pkgVolume"),
                            childObj.getPropertyAsString("pkgRate").toDouble(),
                            childObj.getPropertyAsString("pkgBouns_Speed"),
                            childObj.getPropertyAsString("pkgVolume").toByteArray()
                        )
                    )
                }
                if(list.isEmpty()){
                    interfaces.onError(root.toString())
                }else{
                    interfaces.onSuccess(list)
                }
            }
        }.start()
    }
}