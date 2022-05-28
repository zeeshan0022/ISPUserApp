package com.joinhub.complaintprotaluser

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.WebApis.CheckApp
import com.joinhub.complaintprotaluser.WebApis.LoadPackageDetail
import com.joinhub.complaintprotaluser.activities.DashBoardActivity
import com.joinhub.complaintprotaluser.activities.SigninActivity
import com.joinhub.complaintprotaluser.databinding.ActivityMainBinding
import com.joinhub.complaintprotaluser.models.ManageApp
import com.joinhub.complaintprotaluser.models.PackageDetails
import com.joinhub.complaintprotaluser.services.ComplaintService
import com.joinhub.complaintprotaluser.utilties.Constants
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.darkThemeStyle
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.lightThemeStyle
import com.joinhub.complaintprotaluser.viewmodels.ThemeViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewTheme:ThemeViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var model: ManageApp
    lateinit var preference: Preference
    companion object{
        var themeBool:Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference= Preference(this)
        init()

        val preference= Preference(baseContext)
        if(preference.isBooleenPreference("user")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startService(Intent(this@MainActivity, ComplaintService::class.java))
            }
            checkApp()

        }else{

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(baseContext,SigninActivity::class.java))
                finish()
            },3000)
        }

    }

    private fun checkApp() {
        val api= CheckApp()
        Thread{
            val obj= api.loadData(preference.getIntpreference("areaID"))
            runOnUiThread {
                if (obj != null) {
                    if (obj.getProperty("id") != null) {
                        model = ManageApp(
                            Integer.parseInt(obj.getProperty("id").toString()),
                            Integer.parseInt(obj.getProperty("areaID").toString()),
                            obj.getPropertyAsString("status"),
                            obj.getPropertyAsString("date"),
                            obj.getPropertyAsString("city"),
                            obj.getPropertyAsString("startFrom"),
                            obj.getPropertyAsString("toEnd")
                        )

                        if (model.id == 0) {
                            nextScreen()
                        } else {
                            when {
                                model.startFrom > Constants.getDate() -> {
                                    startingDialog(model.status)
                                }
                                model.startFrom <= Constants.getDate() && model.toEnd >= Constants.getDate() -> {
                                    startedDialog(model.status)

                                }
                                model.toEnd < Constants.getDate() -> {
                                    nextScreen()
                                }

                            }
                        }
                    } else {
                        nextScreen()
                    }

                }
            }
        }.start()
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

    fun nextScreen(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(baseContext,DashBoardActivity::class.java))
            finish()
        },3000)
    }

    fun startingDialog(status :String){
        var message =""
        message = if(status=="Active"){
            "The Friends Internet going to start Maintenance Date: ${model.startFrom}" +
                    "will End on Date: ${model.toEnd}\n" +
                    "In between Application is unable to use all feature will be disabled until maintenance"

        }else{
            "The Friends Internet going to start Maintenance Date: ${model.startFrom}" +
                    "will End on Date: ${model.toEnd}\n" +
                    "In between Application is still available to use"
        }
        MaterialAlertDialogBuilder(this,
            R.style.CutShapeTheme)
            .setCancelable(false)
            .setMessage(message)

            .setPositiveButton("OK") { dialog, which ->
                nextScreen()
                dialog.dismiss()
            }
            .show()
    }

    fun startedDialog(status: String){
        var message =""
        message = if(status=="Active"){
            "The Friends Internet going to started Maintenance Date: ${model.startFrom}" +
                    "will End on Date: ${model.toEnd}\n" +
                    "In between Application is unable to use all feature will be disabled until maintenance"

        }else{
            "The Friends Internet going to started Maintenance Date: ${model.startFrom}" +
                    "will End on Date: ${model.toEnd}\n" +
                    "In between Application is still available to use"
        }
        MaterialAlertDialogBuilder(this,
            R.style.CutShapeTheme)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, which ->
               if(status=="Active"){
                   finish()
               }else{
                   nextScreen()
               }
                dialog.dismiss()

            }
            .show()
    }
}