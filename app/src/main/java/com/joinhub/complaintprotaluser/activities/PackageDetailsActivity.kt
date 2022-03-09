package com.joinhub.complaintprotaluser.activities

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.MainActivity
import com.joinhub.complaintprotaluser.databinding.ActivityPackageDetailsBinding
import com.joinhub.complaintprotaluser.fragments.AllPackagesFragment
import com.joinhub.complaintprotaluser.utilties.Constants


class PackageDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityPackageDetailsBinding
    lateinit var preference: Preference
    lateinit var bundle: Bundle
    lateinit var byteArray: ByteArray
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPackageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initt()
        preference= Preference(this)
        bundle = intent.extras!!
        if(bundle.getBoolean("value")){
            binding.btnActivate.visibility= View.VISIBLE
            val pos= bundle.getInt("pos")
            setData(pos, bundle.getBoolean("un"))

        }else{
            binding.btnActivate.visibility= View.GONE
            binding.txtName.text= preference.getStringpreference("pkgName",null)
            binding.txtDesc.text= preference.getStringpreference("pkgDesc",null)
            binding.txtRate.text="Rs. "+ preference.getStringpreference("pkgRate",null)+"\\month"
            binding.txtSpeed.text= "Speed: "+ preference.getStringpreference("pkgSpeed",null)
            binding.txtSpeed.text= "Volume: "+ preference.getStringpreference("pkgVolume",null)
            if(preference.getStringpreference("pkgBanner",null)!=null){
                byteArray= preference.getStringpreference("pkgBanner",null)!!.toByteArray()
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
               binding.imgBanner.setImageBitmap(bitmap)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setData(pos: Int, type:Boolean) {
        if(type){
            binding.txtName.text=AllPackagesFragment.listPack[pos].pkgName
            binding.txtDesc.text= AllPackagesFragment.listPack[pos].pkgDesc
            binding.txtRate.text="Rs. "+ AllPackagesFragment.listPack[pos].pkgRate+"\\month"
            binding.txtSpeed.text= "Speed: "+ AllPackagesFragment.listPack[pos].pkgSpeed
            binding.txtSpeed.text= "Volume: "+ AllPackagesFragment.listPack[pos].pkgVolume
          if( AllPackagesFragment.listPack[pos].pkgBanner.toString().equals(null)){
                byteArray= AllPackagesFragment.listPack[pos].pkgBanner
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                binding.imgBanner.setImageBitmap(bitmap)
            }

        }else{
            binding.txtName.text=AllPackagesFragment.listPack[pos].pkgName
            binding.txtDesc.text= AllPackagesFragment.listPack[pos].pkgDesc
            binding.txtRate.text="Rs. "+ AllPackagesFragment.listPack[pos].pkgRate+"\\month"
            binding.txtSpeed.text= "Speed: "+ AllPackagesFragment.listPack[pos].pkgSpeed
            binding.txtSpeed.text= "Volume: "+ AllPackagesFragment.listPack[pos].pkgVolume
            if( AllPackagesFragment.listPack[pos].pkgBanner.toString().equals(null)){
                byteArray= AllPackagesFragment.listPack[pos].pkgBanner
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                binding.imgBanner.setImageBitmap(bitmap)
            }
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