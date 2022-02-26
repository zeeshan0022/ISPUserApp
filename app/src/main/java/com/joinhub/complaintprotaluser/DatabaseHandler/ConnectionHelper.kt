package com.joinhub.complaintprotaluser.DatabaseHandler

import android.annotation.SuppressLint
import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager

class ConnectionHelper {
    lateinit var connection: Connection
    lateinit var dbName:String
    lateinit var userName:String
    lateinit var host:String
    lateinit var password:String
    lateinit var port:String

    @SuppressLint("NewApi")
    fun connectionIni():Connection{
        host= "192.168.0.103"
        port="1433"
        dbName="ISPDatabase"
        userName="zeeshu11"
        password="zeeshu11"

     val policy :  StrictMode.ThreadPolicy =StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
     try {
         Class.forName("net.sourceforge.jtds.jdbc.Driver")
         val ConnectionURL =
             "jdbc:jtds:sqlserver://$host:$port/$dbName"
         connection= DriverManager.getConnection(ConnectionURL,userName,password)

         return connection
     }catch (e :Exception){
         Log.e("Connection Database: ", e.message.toString())
     }

        return connection
    }
}