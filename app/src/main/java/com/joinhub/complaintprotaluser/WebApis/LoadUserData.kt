package com.joinhub.complaintprotaluser.WebApis

import com.joinhub.complaintprotaluser.models.UserModel
import com.joinhub.complaintprotaluser.utilties.Constants
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import org.w3c.dom.Document
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.Exception
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class LoadUserData {
    val SOAP_ACTION: String = "http://tempuri.org/getSingleUserDetail"
    val OPERATION_NAME = "getSingleUserDetail"
    fun loadData(userName:String): SoapObject {


        val request = SoapObject(Constants.WSDL_TARGET_NAMESPACE, OPERATION_NAME)
        var pi = PropertyInfo()
        pi.setName("userName")
        pi.value = userName
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
        val soap: SoapObject = response as SoapObject
        return  soap


    }
}