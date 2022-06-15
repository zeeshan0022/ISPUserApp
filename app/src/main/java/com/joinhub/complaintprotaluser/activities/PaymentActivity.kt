package com.joinhub.complaintprotaluser.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.joinhub.complaintprotaluser.bottomsheets.PackageUpgradeBottomSheet
import com.joinhub.complaintprotaluser.databinding.ActivityPaymentBinding
import com.joinhub.complaintprotaluser.utilties.Constants
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.Jazz_IntegritySalt
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.Jazz_MerchantID
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.Jazz_Password
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.paymentReturnUrl
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.php_hash_hmac
import com.nouman.jazzcashlib.JazzCash
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    //lateinit var mWebView: WebView
    var postData = ""
    lateinit var price:String

    private val STORE_ID = "ENTER_STORE_ID"
    private val HASH_KEY = "ENTER_HASH_KEY"
    private val POST_BACK_URL1 = "http://localhost/easypay/order_confirm.php"
    private val POST_BACK_URL2 = "http://localhost/easypay/order_complete.php"
    private val TRANSACTION_POST_URL1 = "https://easypay.easypaisa.com.pk/easypay/Index.jsf"
    private val TRANSACTION_POST_URL2 = "https://easypay.easypaisa.com.pk/easypay/Confirm.jsf"
   // private var price = ""
    lateinit var jazzCash: JazzCash
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
       val bundle= intent.extras
       if(bundle!=null){
           price= bundle.getString("charges").toString()
           Toast.makeText(applicationContext,price,Toast.LENGTH_LONG).show()
           if(bundle.getString("type")!! == "JazzCash"){
               try {

                   jazzCash = JazzCash(
                       this,
                       this,
                       ResponseActivity::class.java,
                       binding.activityPaymentWebView,
                       Constants.Jazz_MerchantID,
                       Constants.Jazz_Password, Constants.Jazz_IntegritySalt,
                       Constants.paymentReturnUrl,
                       1.0.toString()
                   )

                   jazzCash.integrateNow()
               } catch (ex: Exception) {
                   Toast.makeText(this, "ConnectWithXML: " + ex.message, Toast.LENGTH_SHORT).show()
               }
           }else{
               try {

                   JazzCashPayment()
                   //    EasyPaisaPayment()
               } catch (ex: Exception) {
                   Toast.makeText(this, "ConnectWithXML: " + ex.message, Toast.LENGTH_SHORT).show()
               }
           }
       }

    }

    fun JazzCashPayment() {
        val webSettings = binding.activityPaymentWebView.settings
        webSettings.javaScriptEnabled = true

        binding.activityPaymentWebView.webViewClient = MyWebViewClient()
        webSettings.domStorageEnabled = true
        binding.activityPaymentWebView.addJavascriptInterface(FormDataInterface(), "FORMOUT")
        println("AhmadLogs: price_before : $price")

        val values = price.split("\\.").toTypedArray()
        price = values[0]
        price = price + "00"
        println("AhmadLogs: price : $price")

        val Date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMddkkmmss")
        val DateString = dateFormat.format(Date)
        println("AhmadLogs: DateString : $DateString")

        // Convert Date to Calendar

        // Convert Date to Calendar
        val c = Calendar.getInstance()
        c.time = Date
        c.add(Calendar.HOUR, 1)

        // Convert calendar back to Date

        // Convert calendar back to Date
        val currentDateHourPlusOne = c.time
        val expiryDateString = dateFormat.format(currentDateHourPlusOne)
        println("AhmadLogs: expiryDateString : $expiryDateString")

        val TransactionIdString = "T$DateString"
        println("AhmadLogs: TransactionIdString : $TransactionIdString")

        val pp_MerchantID: String = Jazz_MerchantID
        val pp_Password: String = Jazz_Password
        val IntegritySalt: String = Jazz_IntegritySalt
        val pp_ReturnURL: String = paymentReturnUrl
        val pp_Amount: String = price
        val pp_Version = "1.1"
        val pp_TxnType = ""
        val pp_Language = "EN"
        val pp_SubMerchantID = ""
        val pp_BankID = "TBANK"
        val pp_ProductID = "RETL"
        val pp_TxnCurrency = "PKR"
        val pp_BillReference = "billRef"
        val pp_Description = "Description of transaction"
        var pp_SecureHash: String
        val pp_mpf_1 = "1"
        val pp_mpf_2 = "2"
        val pp_mpf_3 = "3"
        val pp_mpf_4 = "4"
        val pp_mpf_5 = "5"
        var sortedString = ""

        sortedString += "$IntegritySalt&"
        sortedString += "$pp_Amount&"
        sortedString += "$pp_BankID&"
        sortedString += "$pp_BillReference&"
        sortedString += "$pp_Description&"
        sortedString += "$pp_Language&"
        sortedString += "$pp_MerchantID&"
        sortedString += "$pp_Password&"
        sortedString += "$pp_ProductID&"
        sortedString += "$pp_ReturnURL&"
        //sortedString += pp_SubMerchantID + "&";
        //sortedString += pp_SubMerchantID + "&";
        sortedString += "$pp_TxnCurrency&"
        sortedString += "$DateString&"
        sortedString += "$expiryDateString&"
        //sortedString += pp_TxnType + "&";
        //sortedString += pp_TxnType + "&";
        sortedString += "$TransactionIdString&"
        sortedString += "$pp_Version&"
        sortedString += "$pp_mpf_1&"
        sortedString += "$pp_mpf_2&"
        sortedString += "$pp_mpf_3&"
        sortedString += "$pp_mpf_4&"
        sortedString += pp_mpf_5

        pp_SecureHash = php_hash_hmac(sortedString, IntegritySalt)


        try {
            postData += (URLEncoder.encode("pp_Version", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Version, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnType", "UTF-8")
                    + "=" + pp_TxnType + "&")
            postData += (URLEncoder.encode("pp_Language", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Language, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_MerchantID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_MerchantID, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_SubMerchantID", "UTF-8")
                    + "=" + pp_SubMerchantID + "&")
            postData += (URLEncoder.encode("pp_Password", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Password, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_BankID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BankID, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_ProductID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ProductID, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnRefNo", "UTF-8")
                    + "=" + URLEncoder.encode(TransactionIdString, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_Amount", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Amount, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnCurrency", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnCurrency, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(DateString, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_BillReference", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BillReference, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_Description", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Description, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_TxnExpiryDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(expiryDateString, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_ReturnURL", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ReturnURL, "UTF-8") + "&")
            postData += (URLEncoder.encode("pp_SecureHash", "UTF-8")
                    + "=" + pp_SecureHash + "&")
            postData += (URLEncoder.encode("ppmpf_1", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_1, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_2", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_2, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_3", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_3, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_4", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_4, "UTF-8") + "&")
            postData += (URLEncoder.encode("ppmpf_5", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_5, "UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        System.out.println("AhmadLogs: postData : $postData")

        binding.activityPaymentWebView.postUrl(
            "https://sandbox.jazzcash.com.pk/CustomerPortal/transactionmanagement/merchantform/",
            postData.toByteArray()
        )
    }


    inner class MyWebViewClient : WebViewClient() {
        private val jsCode = "" + "function parseForm(form){" +
                "var values='';" +
                "for(var i=0 ; i< form.elements.length; i++){" +
                "   values+=form.elements[i].name+'='+form.elements[i].value+'&'" +
                "}" +
                "var url=form.action;" +
                "console.log('parse form fired');" +
                "window.FORMOUT.processFormData(url,values);" +
                "   }" +
                "for(var i=0 ; i< document.forms.length ; i++){" +
                "   parseForm(document.forms[i]);" +
                "};"

        //private static final String DEBUG_TAG = "CustomWebClient";
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            if (url == paymentReturnUrl) {
                println("AhmadLogs: return url cancelling")
                view.stopLoading()
                return
            }
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            //Log.d(DEBUG_TAG, "Url: "+url);
            if (url == paymentReturnUrl) {
                return
            }
            view.loadUrl("javascript:(function() { $jsCode})()")
            super.onPageFinished(view, url)
        }
    }
    inner class FormDataInterface {
        @JavascriptInterface
        fun processFormData(url: String, formData: String) {
            val i = Intent(applicationContext, PackageUpgradeBottomSheet::class.java)
            println("AhmadLogs: Url:$url form data $formData")
            if (url == paymentReturnUrl) {
                val values = formData.split("&").toTypedArray()
                for (pair in values) {
                    val nameValue = pair.split("=").toTypedArray()
                    if (nameValue.size == 2) {
                        println("AhmadLogs: Name:" + nameValue[0] + " value:" + nameValue[1])
                        i.putExtra(nameValue[0], nameValue[1])
                    }
                }
                setResult(RESULT_OK, i)
                finish()
                return
            }
        }
    }

}