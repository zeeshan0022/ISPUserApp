package com.joinhub.complaintprotaluser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.joinhub.complaintprotaluser.MainActivity
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.databinding.ActivitySettingsBinding
import com.joinhub.complaintprotaluser.databinding.FragmentProfileBinding
import com.joinhub.complaintprotaluser.utilties.Constants
import com.joinhub.complaintprotaluser.viewmodels.ThemeViewModel

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    lateinit var viewTheme: ThemeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backImage.setOnClickListener{finish()}
        binding.themeSwitch.setOnCheckedChangeListener{ _, isChecked->
            viewTheme= ViewModelProvider(this)[ThemeViewModel::class.java]
            if(isChecked){
                viewTheme.saveToDataStore(0)
                changeTheme(0)
            }else{
                viewTheme.saveToDataStore(1)
                changeTheme(1)
            }
        }
        init()

    }

    private fun changeTheme(i: Int) {
        if(i==0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Constants.darkThemeStyle(this)
            MainActivity.themeBool =true
        }else{
            MainActivity.themeBool =false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Constants.lightThemeStyle(this)
        }
    }

    private fun init(){
        if(MainActivity.themeBool){
            Constants.darkThemeStyle(this)
            binding.themeSwitch.isChecked= true

        }else{
            Constants.lightThemeStyle(this)
            binding.themeSwitch.isChecked= false
        }
    }


}