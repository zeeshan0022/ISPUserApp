package com.joinhub.complaintprotaluser.WebApis

import com.joinhub.complaintprotaluser.utilties.Constants
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.lang.Exception

class LoadPackageList {

    val SOAP_ACTION: String = "http://tempuri.org/loadPackageDetails"
    val OPERATION_NAME = "loadPackageDetails"
    fun loadData(value:Boolean): SoapObject {
        val request = SoapObject(Constants.WSDL_TARGET_NAMESPACE, OPERATION_NAME)
        val pi = PropertyInfo()
        pi.setName("valye")
        pi.value = value
        pi.setType(Boolean::class.java)
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
        return response as SoapObject


    }
}