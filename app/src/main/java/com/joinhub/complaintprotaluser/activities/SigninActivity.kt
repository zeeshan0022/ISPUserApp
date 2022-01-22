package com.joinhub.complaintprotaluser.activities

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.MainActivity.Companion.themeBool
import com.joinhub.complaintprotaluser.databinding.ActivitySigninBinding
import com.joinhub.complaintprotaluser.utilties.Constants
import com.joinhub.complaintprotaluser.interfaces.LoginInterface
import com.joinhub.complaintprotaluser.presentator.LoginPresentator


class SigninActivity : AppCompatActivity(), LoginInterface {
    private lateinit var binding:ActivitySigninBinding
   private lateinit var loginPresentator: LoginPresentator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.btnlogin.setOnClickListener{
            checkData(binding.userIDeditText.text.toString().trim(),
                       binding.passwordeditText.text.toString().trim())
        }
        binding.txtForgotPassword.setOnClickListener{
            startActivity(Intent(applicationContext, ForgotActivity::class.java))
        }

    }
    private fun checkData(id:String, password:String){
        if(id.isEmpty()|| password.isEmpty() ){
            if(id.isEmpty()){
                binding.userIdLayout.error= "Please Enter User ID"
            }else{

                binding.userIdLayout.error= null
            }

            if(password.isEmpty()){
                binding.passwordLayout.error= "Please Enter Password"
            }else{

                binding.passwordLayout.error= null
            }
        }else{
            binding.userIdLayout.error= null
            binding.passwordLayout.error= null
            binding.progressBar.visibility= View.VISIBLE

            loginPresentator.LoginCredential(id,password)

        }
    }
   private fun init(){
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        loginPresentator= LoginPresentator(this, baseContext,this)
        StrictMode.setThreadPolicy(policy)
        if(themeBool){
            Constants.darkThemeStyle(this)
        }else{
            Constants.lightThemeStyle(this)
        }
    }

    override fun onError(e: String) {
        binding.progressBar.visibility= View.GONE
       showToast(e)
    }

    override fun onSuccess() {
        binding.progressBar.visibility= View.GONE
            showToast("Login Successful")
            // Work in the UI thread
        val preference=Preference(baseContext)
        preference.setBooleanpreference("userID",true)
        preference.setStringpreference("userIDS", binding.userIDeditText.text.toString())
        startActivity(Intent(baseContext,DashBoardActivity::class.java))
        finish()
    }

    override fun onStarts() {
        binding.progressBar.visibility= View.VISIBLE
    }

    override fun showProgress() {   binding.progressBar.visibility= View.VISIBLE }

    override fun hideProgress() {
        binding.progressBar.visibility= View.GONE
    }

   private fun showToast(message :String){
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
        }
    }
}