package com.joinhub.complaintprotaluser.presentator

import android.content.Context
import android.widget.Toast
import com.joinhub.complaintprotaluser.DatabaseHandler.ConnectionHelper
import com.joinhub.complaintprotaluser.WebApis.LoginApi
import com.joinhub.complaintprotaluser.activities.SigninActivity
import com.joinhub.complaintprotaluser.interfaces.LoginInterface
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types


class LoginPresentator(var loginInterface:LoginInterface, var context: Context,var activity: SigninActivity) {
    lateinit var resultSet: ResultSet
    lateinit var s:String
   fun LoginCredential(id:String, password:String) {
       //var value: Int? =null

       // val con= ConnectionHelper()
       loginInterface.onStarts()
       val loginApi = LoginApi()
       Thread {
           try {

               s = loginApi.LoginUser(id, password)

           } catch (e: Exception) {
               loginInterface.onError(e.message.toString())
           }

           activity.runOnUiThread {
               if (s == "true") {
                   loginInterface.onSuccess()
               } else {
                   loginInterface.onError(s!!)
               }
           }
       }.start()
   }
}


