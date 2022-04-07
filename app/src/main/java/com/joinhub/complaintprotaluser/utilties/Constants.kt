package com.joinhub.complaintprotaluser.utilties

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Base64
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.joinhub.complaintprotaluser.R
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


class Constants {

    companion object {
        const val WSDL_TARGET_NAMESPACE = "http://tempuri.org/"

         const val SOAP_ADDRESS = "http://192.168.0.103:2020/WebService1.asmx"
         const val Jazz_MerchantID = "MC204650"
         const val Jazz_Password = "vv949110gw"
         const val Jazz_IntegritySalt = "433zzx94v0"
         const val paymentReturnUrl = "http://localhost/order.php"


        fun checkGoogleAPI(activity: Activity): Boolean {
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
            if (status != ConnectionResult.SUCCESS) {

                return false
            }
            return true
        }

        fun darkThemeStyle(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window: Window = activity.window
                val decorView: View = window.decorView
                val wic = WindowInsetsControllerCompat(window, decorView)
                wic.isAppearanceLightStatusBars = false // true or false as desired.

                // And then you can set any background color to the status bar.
                window.statusBarColor = activity.getColor(R.color.backDark)
                window.navigationBarColor = activity.getColor(R.color.backDark)

            }
        }

        fun lightThemeStyle(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window: Window = activity.window
                val decorView: View = window.decorView
                val wic = WindowInsetsControllerCompat(window, decorView)
                wic.isAppearanceLightStatusBars = true // true or false as desired.

                // And then you can set any background color to the status bar.
                window.statusBarColor = activity.getColor(R.color.back)
                window.navigationBarColor = activity.getColor(R.color.back)
            }
        }


        fun getWifiSSDID(context: Context): String {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                return "ID"
            } else {
                val wifiManager: WifiManager =
                    context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifiManager.connectionInfo
                return info.ssid
            }


        }

        fun get_hash(data: String, key: String): String {
            var hashString = ""
            try {
                val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
                val secretKey = SecretKeySpec(key.toByteArray(), "AES")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                val encryptedValue: ByteArray = cipher.doFinal(data.toByteArray())
                hashString = Base64.encodeToString(encryptedValue, Base64.DEFAULT)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            }
            return hashString
        }

        fun php_hash_hmac(data: String, secret: String): String {
            var returnString: String = ""
            try {
                val sha256_HMAC: Mac = Mac.getInstance("HmacSHA256")
                val secret_key = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
                sha256_HMAC.init(secret_key)
                val res: ByteArray = sha256_HMAC.doFinal(data.toByteArray())
                returnString = bytesToHex(res)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return returnString
        }
        fun getDate(): String {
            val c = Calendar.getInstance().time
            println("Current time => $c")

            val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return df.format(c)
        }
        fun getYear():String{
            val calendar: Calendar = Calendar.getInstance()
            val year: Int = calendar.get(Calendar.YEAR)
        return  year.toString()
        }
        fun getMonth():String{
            val calendar: Calendar = Calendar.getInstance()
            val i= calendar.get(Calendar.MONTH)+1
            return i.toString()

        }
        fun bytesToHex(bytes: ByteArray): String {
            val hexArray = "0123456789abcdef".toCharArray()
            val hexChars = CharArray(bytes.size * 2)
            var j = 0
            var v: Int
            while (j < bytes.size) {
                v = (bytes[j] and 0xFF.toByte()).toInt()
                hexChars[j * 2] = hexArray[v ushr 4]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
                j++
            }
            return String(hexChars)
        }

        fun getDate1(): String {
            val calendar: Calendar = Calendar.getInstance()

            return calendar.get(Calendar.DATE).toString()
        }

        //
    }



}