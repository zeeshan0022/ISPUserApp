package com.joinhub.complaintprotaluser.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.huawei.hms.iap.entity.OrderStatusCode
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.FullProfileActivity
import com.joinhub.complaintprotaluser.MainActivity
import com.joinhub.complaintprotaluser.R

import com.joinhub.complaintprotaluser.databinding.ActivityDashBoardBinding
import com.joinhub.complaintprotaluser.huaweiIAPLab.SubscriptionUtils
import com.joinhub.complaintprotaluser.utilties.Constants

class DashBoardActivity : AppCompatActivity() {

    lateinit var  preference: Preference
    private lateinit var binding: ActivityDashBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        preference= Preference(this)
        val navView: BottomNavigationView = binding.navView
        binding.imageProfile.setOnClickListener {
            startActivity(Intent(applicationContext, FullProfileActivity::class.java))
        }
        val navController = findNavController(R.id.nav_host_fragment_activity_dash_board)
        AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_packages,R.id.navigation_billing,R.id.navigation_profile
            )
        )

        navView.setupWithNavController(navController)
    }
    private fun init(){
        if(MainActivity.themeBool){
            Constants.darkThemeStyle(this)
        }else{
            Constants.lightThemeStyle(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if(preference.getStringpreference("userImage",null).isNotBlank()){
            binding.profile.setImageBitmap(Constants.decodeBase64(preference.getStringpreference("userImage")))
        }else{
            binding.profile.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.male_avatar))
        }
    }
}