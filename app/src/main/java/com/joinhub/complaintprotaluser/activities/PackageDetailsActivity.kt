package com.joinhub.complaintprotaluser.activities

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.MainActivity
import com.joinhub.complaintprotaluser.bottomsheets.PackageUpgradeBottomSheet
import com.joinhub.complaintprotaluser.databinding.ActivityPackageDetailsBinding
import com.joinhub.complaintprotaluser.fragments.AllPackagesFragment
import com.joinhub.complaintprotaluser.fragments.UnlimitedPackagesFragment
import com.joinhub.complaintprotaluser.models.PackageDetails
import com.joinhub.complaintprotaluser.utilties.Constants
import kotlin.properties.Delegates


class PackageDetailsActivity : AppCompatActivity(){
    lateinit var binding:ActivityPackageDetailsBinding
    lateinit var preference: Preference
    lateinit var model:PackageDetails
    lateinit var bottomSheet: PackageUpgradeBottomSheet
    companion object {
        var pos by Delegates.notNull<Int>()
        var unLimit by Delegates.notNull<Boolean>()
    }
    lateinit var byteArray: ByteArray
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPackageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initt()
        binding.back.setOnClickListener { finish() }
        preference= Preference(this)
       val bundle = intent.extras
        model= PackageDetails()
        if(bundle!=null){
            model= bundle.getParcelable("model")!!
            val value= bundle.getBoolean("value")
            if(value){
                binding.btnActivate.visibility=View.GONE
            }
            setData()
        }
        binding.txtTOC.setOnClickListener {  }
        binding.btnActivate.setOnClickListener {
            bottomSheet= PackageUpgradeBottomSheet()
            bottomSheet =
                PackageUpgradeBottomSheet().newInstance()
            bottomSheet.show(supportFragmentManager,
                PackageUpgradeBottomSheet.TAG)

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        binding.txtName.text=model.pkgName
        binding.txtDesc.text= model.pkgDesc
        binding.txtRate.text="Rs. "+ model.pkgRate+"\\month"
        binding.txtSpeed.text= "Speed: "+ model.pkgSpeed
        binding.txtVolume.text= "Volume: "+ model.pkgVolume
        if(! model.pkgBanner.equals(null)){
            byteArray= model.pkgBanner
            val decodedByte: ByteArray = Base64.decode(model.pkgBanner.toString(), 0)
            binding.imgBanner.setImageBitmap(  BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size))
        }
    }


    private fun initt() {
        if(MainActivity.themeBool){
            Constants.darkThemeStyle(this)
        }else{
            Constants.lightThemeStyle(this)
        }
    }
}