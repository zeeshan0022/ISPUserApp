package com.joinhub.complaintprotaluser.WebApis

import com.joinhub.complaintprotaluser.models.ComplaintModel
import com.joinhub.complaintprotaluser.models.PackageDetails
import com.joinhub.complaintprotaluser.utilties.Constants
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.lang.Exception
import java.time.Year
import java.util.*

class UpgradePackageDetails {
    val SOAP_ACTION: String = "http://tempuri.org/savePackage"
    val OPERATION_NAME = "savePackage"
    fun saveData(pkgID: Int,method:String, date:String,status:String,userID:Int, charges:String,
                       month: String, year: String, isUp:Boolean, name:String): String {
        val request = SoapObject(Constants.WSDL_TARGET_NAMESPACE, OPERATION_NAME)
        var pi = PropertyInfo()

        pi.setName("pkgID")
        pi.value =pkgID
        pi.setType(Int::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("userID")
        pi.value = userID
        pi.setType(Int::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("upgrade")
        pi.value = isUp
        pi.setType(Boolean::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("method")
        pi.value =method
        pi.setType(String::class.java)
        request.addProperty(pi)
        //

        //
        pi = PropertyInfo()
        pi.setName("date")
        pi.value =date
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("status")
        pi.value = status
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("charges")
        pi.value = charges
        pi.setType(String::class.java)
        request.addProperty(pi)
//
        pi = PropertyInfo()
        pi.setName("month")
        pi.value = month
        pi.setType(String::class.java)
        request.addProperty(pi)

        pi = PropertyInfo()
        pi.setName("year")
        pi.value = year
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("pkgname")
        pi.value =name
        pi.setType(String::class.java)
        request.addProperty(pi)


        val envelope = SoapSerializationEnvelope(
            SoapEnvelope.VER11
        )
        envelope.dotNet = true

        envelope.setOutputSoapObject(request)

        val httpTransport = HttpTransportSE(Constants.SOAP_ADDRESS)
        val response: Any? = try {
            httpTransport.call(SOAP_ACTION, envelope)
            envelope.response
        } catch (exception: Exception) {
            exception.toString()
        }

        return  response.toString()


    }
}