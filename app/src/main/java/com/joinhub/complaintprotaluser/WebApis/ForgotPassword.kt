package com.joinhub.complaintprotaluser.WebApis

import com.joinhub.complaintprotaluser.utilties.Constants
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.lang.Exception

class ForgotPassword {
    val SOAP_ACTION: String = "http://tempuri.org/forgotPassword"
    val OPERATION_NAME = "forgotPassword"
    fun saveData(user:String, cnic:String, password:String, value:Int): String {
        val request = SoapObject(Constants.WSDL_TARGET_NAMESPACE, OPERATION_NAME)
        var pi = PropertyInfo()
        pi.setName("user")
        pi.value = user
        pi.setType(String::class.java)
        request.addProperty(pi)


        pi = PropertyInfo()
        pi.setName("cnic")
        pi.value = cnic
        pi.setType(String::class.java)
        request.addProperty(pi)


        pi = PropertyInfo()
        pi.setName("password")
        pi.value = password
        pi.setType(String::class.java)
        request.addProperty(pi)
        pi = PropertyInfo()
        pi.setName("value")
        pi.value = value
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
        return response.toString()
    }
}