package com.joinhub.complaintprotaluser.WebApis

import com.joinhub.complaintprotaluser.models.ComplaintModel
import com.joinhub.complaintprotaluser.utilties.Constants
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.lang.Exception

class AddComplaintAPI {
    val SOAP_ACTION: String = "http://tempuri.org/saveComplaint"
    val OPERATION_NAME = "saveComplaint"
    fun saveData(model:ComplaintModel): String {
        val request = SoapObject(Constants.WSDL_TARGET_NAMESPACE, OPERATION_NAME)
        var pi = PropertyInfo()
        pi.setName("complaintTicketNo")
        pi.value = model.complaintTicketNo
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("complaintName")
        pi.value = model.complaintName
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("complaintPhone")
        pi.value = model.complaintPhone
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("complaintEmail")
        pi.value = model.complaintEmail
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("complaintLong")
        pi.value = model.complaintLong
        pi.setType(String::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("complaintLatn")
        pi.value = model.complaintLatn
        pi.setType(String::class.java)
        request.addProperty(pi)
//
        pi = PropertyInfo()
        pi.setName("complaintIssue")
        pi.value = model.complaintIssue
        pi.setType(String::class.java)
        request.addProperty(pi)

        pi = PropertyInfo()
        pi.setName("complaintDesc")
        pi.value = model.complaintDesc
        pi.setType(String::class.java)
        request.addProperty(pi)

        pi = PropertyInfo()
        pi.setName("complaintStatus")
        pi.value = model.complaintStatus
        pi.setType(String::class.java)
        request.addProperty(pi)

        //
        pi = PropertyInfo()
        pi.setName("serviceID")
        pi.value = model.serviceID
        pi.setType(Int::class.java)
        request.addProperty(pi)
        //
        pi = PropertyInfo()
        pi.setName("userID")
        pi.value = model.userID
        pi.setType(Int::class.java)
        request.addProperty(pi)

        pi = PropertyInfo()
        pi.setName("date")
        pi.value = model.date
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

        return  response.toString()


    }
}