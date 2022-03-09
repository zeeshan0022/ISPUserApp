package com.joinhub.complaintprotaluser.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.MainActivity
import com.joinhub.complaintprotaluser.databinding.ActivityComplaintDetailBinding
import com.joinhub.complaintprotaluser.fragments.ComplaintHistory
import com.joinhub.complaintprotaluser.interfaces.ComplaintDetailInterface
import com.joinhub.complaintprotaluser.presentator.ComplaintDetailPresentator
import com.joinhub.complaintprotaluser.utilties.Constants


class ComplaintDetail: AppCompatActivity() ,ComplaintDetailInterface {

    lateinit var binding:ActivityComplaintDetailBinding
    lateinit var  bundle: Bundle
    private var pos:Int=0
    lateinit var preference: Preference
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityComplaintDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ini()
        bundle= intent.extras!!
        if(!bundle.isEmpty) {
            pos = bundle.getInt("pos")
            binding.txtName.text = "Name: " + ComplaintHistory.complaintList[pos].complaintName
            binding.txtPhone.text = "Phone: " + ComplaintHistory.complaintList[pos].complaintPhone
            binding.txtEmail.text = "Email: " + ComplaintHistory.complaintList[pos].complaintEmail
            binding.txtAddress.text =
                "Address: " + ComplaintHistory.complaintList[pos].complaintEmail
            binding.txtSName.text = "Name: " + preference.getStringpreference("serviceName", null)
            binding.txtSPhone.text =
                "Phone: " + preference.getStringpreference("servicePhone", null)
            binding.txtSEmail.text =
                "Email: " + preference.getStringpreference("serviceEmail", null)
            binding.txtIssue.text =
                "Issue Type: " + ComplaintHistory.complaintList[pos].complaintIssue
            binding.txtDesc.text =
                "Description: \n            " + ComplaintHistory.complaintList[pos].complaintDesc

            binding.txtLocation.text = "Location \n"+
                    "Long: "+ComplaintHistory.complaintList[pos].complaintLong+
                    "Latn: "+ComplaintHistory.complaintList[pos].complaintLatn
            binding.txtTicketNo.text="Ticket No: " +ComplaintHistory.complaintList[pos].complaintTicketNo
            binding.txtStatus.text="Current Status: "+ComplaintHistory.complaintList[pos].complaintStatus

        }
        binding.iconWhatsapp.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=92"+ preference.getStringpreference("servicePhone",null)?.removeRange(0,0)))
            startActivity(browserIntent)
        }

        binding.iconSMS.setOnClickListener {
            val smsIntent = Intent(Intent.ACTION_VIEW)
            smsIntent.type = "vnd.android-dir/mms-sms"
            smsIntent.putExtra("address",  preference.getStringpreference("servicePhone",null))
            startActivity(smsIntent)
        }
        binding.iconCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+ preference.getStringpreference("servicePhone",null))
            startActivity(intent)
        }

        binding.btnCancel.isEnabled = ComplaintHistory.complaintList[pos].complaintStatus == "Active"
        binding.btnCancel.setOnClickListener {
         val presentator = ComplaintDetailPresentator(this,
                               applicationContext,this@ComplaintDetail)
        presentator.saveData(ComplaintHistory.complaintList[pos].complaintID)
        }
    }

    private fun ini() {
        preference= Preference(this);
        if(MainActivity.themeBool){
            Constants.darkThemeStyle(this)
        }else{
            Constants.lightThemeStyle(this)
        }
    }

    override fun onStarts() {
       binding.progressBar.visibility= View.VISIBLE

    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(status: String) {
        binding.progressBar.visibility= View.GONE
        binding.btnCancel.isEnabled= false
        ComplaintHistory.complaintList[pos].complaintStatus="Cancelled"
        binding.txtStatus.text="Current Status: "+ComplaintHistory.complaintList[pos].complaintStatus

    }

    override fun onError(e: String) {
        binding.progressBar.visibility= View.GONE
        binding.btnCancel.isEnabled= true
    }

}