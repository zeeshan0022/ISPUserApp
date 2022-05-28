package com.joinhub.complaintprotaluser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.WebApis.ForgotPassword
import com.joinhub.complaintprotaluser.databinding.ActivityForgotBinding

class ForgotActivity : AppCompatActivity() {
    lateinit var binding:ActivityForgotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setOnClickListener { finish() }
        binding.btnForgot.setOnClickListener {

            checkData(binding.userIDeditText.text.toString().trim(), binding.CNICeditText.text.toString().trim(),
            binding.NewPasseditText.text.toString().trim(), binding.ConfimPasseditText.text.toString().trim())
        }
    init()
    }

    private fun checkData(userID:String, cnic:String, newPass:String, confirmPassword:String) {
        if(userID==""|| cnic==""|| newPass==""|| confirmPassword==""){

        }else{
            binding.progressBar.visibility= View.VISIBLE
            Thread{
                val api= ForgotPassword()
              val result=  api.saveData(userID, cnic,newPass, 0)
                runOnUiThread {
                    if(result=="true"){
                        binding.progressBar.visibility= View.GONE
                        Toast.makeText(applicationContext, "Password Reset Successfully",Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        binding.progressBar.visibility= View.GONE
                        if(result=="false"){
                            Toast.makeText(applicationContext, "Incorrect UserName or CNIC",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext, result,Toast.LENGTH_LONG).show()

                        }

                    }
                }
            }.start()
        }
    }

    private fun init(){

    }
}