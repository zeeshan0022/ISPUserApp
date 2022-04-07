package com.joinhub.complaintprotaluser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.activities.DashBoardActivity
import com.joinhub.complaintprotaluser.activities.SigninActivity
import com.joinhub.complaintprotaluser.databinding.ActivityMainBinding
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.darkThemeStyle
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.lightThemeStyle
import com.joinhub.complaintprotaluser.viewmodels.ThemeViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewTheme:ThemeViewModel
    lateinit var binding: ActivityMainBinding
    companion object{
        var themeBool:Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        val preference= Preference(baseContext)
        if(preference.isBooleenPreference("user")){
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(baseContext,DashBoardActivity::class.java))
                finish()
            },3000)
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(baseContext,SigninActivity::class.java))
                finish()
            },3000)
        }

    }


    private fun init(){
        viewTheme= ViewModelProvider(this).get(ThemeViewModel::class.java)
        checkTheme()
    }

    private fun checkTheme(){

        viewTheme.readFromDataStore.observe(this) {
            if (it.equals("1")) {
                themeBool = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                lightThemeStyle(this)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                darkThemeStyle(this)
                themeBool = true
            }
        }


    }


}