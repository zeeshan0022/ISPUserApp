package com.joinhub.complaintprotaluser.WebApis

import com.joinhub.complaintprotaluser.utilties.Constants
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.lang.Exception

class LoadSRDetail {
    val SOAP_ACTION: String = "http://tempuri.org/loadSR"
    val OPERATION_NAME = "loadSR"
    fun loadData(userName: Int): SoapObject {
        val request = SoapObject(Constants.WSDL_TARGET_NAMESPACE, OPERATION_NAME)
        val pi = PropertyInfo()
        pi.setName("areaID")
        pi.value = userName
        pi.setType(Int::class.java)
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