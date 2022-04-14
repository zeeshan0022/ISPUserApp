package com.joinhub.complaintprotaluser.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.Adapters.HomePackageAdapter
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.activities.PackageDetailsActivity
import com.joinhub.complaintprotaluser.databinding.FragmentHomeBinding
import com.joinhub.complaintprotaluser.interfaces.HomeInterface
import com.joinhub.complaintprotaluser.models.AreaModel
import com.joinhub.complaintprotaluser.models.PackageDetails
import com.joinhub.complaintprotaluser.models.ServiceModel
import com.joinhub.complaintprotaluser.models.UserModel
import com.joinhub.complaintprotaluser.presentator.HomePresentator
import com.joinhub.complaintprotaluser.utilties.Constants


class HomeFragment : Fragment(), HomeInterface {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var  preference:Preference
    lateinit var  presenatator:HomePresentator
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()

        binding.btnPackageDetail.setOnClickListener {
         val  i= Intent(requireContext(), PackageDetailsActivity::class.java)
            i.putExtra("value",false)
            startActivity(i)
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        presenatator= HomePresentator(this@HomeFragment, requireActivity())
        preference= Preference(requireContext())
        presenatator.loadPackages()
        if(preference.getIntpreference("userID")==0){
            presenatator.loadData(preference.getStringpreference("userName"))
        }else{
            //
            binding.txtPhone.text=preference.getStringpreference("userPhone",null)
            binding.txtUserName.text= preference.getStringpreference("userFullName",null)
            //
            binding.txtPackName.text= preference.getStringpreference("pkgName",null)
            binding.txtSpeed1.text= preference.getStringpreference("pkgSpeed",null)
            binding.txtVol.text= preference.getStringpreference("pkgVolume",null)
         //   binding.txtPackName.text= preference.getStringpreference("pkgVolume",null)
        }

        binding.txtSSID.text= Constants.getWifiSSDID(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onError(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(model: UserModel) {
        binding.txtPhone.text="0"+model.userPhone
        binding.txtUserName.text= model.userFullName
        savePerference(model)
    }

    private fun savePerference(model: UserModel) {
        preference.setIntpreference("userID",model.userID)
        preference.setIntpreference("areaID",model.areaID)
        preference.setIntpreference("pkgID",model.pkgID)
        preference.setStringpreference("userPhone",model.userPhone)
        preference.setStringpreference("userName",model.userName)
        preference.setStringpreference("userFullName",model.userFullName)
        preference.setStringpreference("userCNIC",model.userCNIC)
        preference.setStringpreference("userEmail",model.userEmail)
        preference.setStringpreference("userAddress",model.userAddress)
        preference.setStringpreference("userPass",model.userPass)
        presenatator.loadSRData(model.areaID)
        presenatator.loadArea(model.areaID)
        presenatator.loadPackage(model.pkgID)
    }

    override fun onStarts() {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onSRLoadSuccess(model:ServiceModel) {
        preference.setIntpreference("serviceID",model.serviceID)
        Toast.makeText(requireContext(),""+ model.serviceID,Toast.LENGTH_LONG).show()
        preference.setStringpreference("serviceName",model.serviceName)
        preference.setStringpreference("serviceUserName",model.serviceUserName)
        preference.setStringpreference("serviceEmail",model.serviceEmail)
        preference.setStringpreference("servicePhone",model.servicePhone)
    }

    override fun onAreaLoad(model: AreaModel) {
        preference.setStringpreference("areaCode",model.areaCode)
        preference.setStringpreference("city",model.city)
        preference.setStringpreference("areaName",model.areaName)
        preference.setStringpreference("areaSubName",model.areaSubName)
    }

    override fun onPackageLoad(model: PackageDetails) {
        binding.txtPackName.text= model.pkgName
        binding.txtSpeed1.text= model.pkgSpeed
        binding.txtVol.text= model.pkgVolume
      //  packageDetailsModel= model
        preference.setStringpreference("pkgName",model.pkgName)
        preference.setStringpreference("pkgDesc",model.pkgDesc)
        preference.setStringpreference("pkgSpeed",model.pkgSpeed)
        preference.setStringpreference("pkgVolume",model.pkgVolume)
        preference.setStringpreference("pkgRate",model.pkgRate.toString())
        preference.setStringpreference("pkgBouns_Speed",model.pkgBouns_Speed)
        preference.setStringpreference("pkgBanner",model.pkgBanner.toString())


    }

    override fun onPackageSuccess(list: MutableList<PackageDetails>) {
        setLastestRec(list)
        setTopRec(list)
    }

    private fun setTopRec(list: MutableList<PackageDetails>) {
        list.shuffle()
        val adapter=HomePackageAdapter(list,requireActivity())
        binding.recTopRatedPack.layoutManager=LinearLayoutManager(requireContext(),
                                              RecyclerView.HORIZONTAL,false)
        binding.recTopRatedPack.adapter=adapter
    }

    private fun setLastestRec(list: MutableList<PackageDetails>) {
        val adapter=HomePackageAdapter(list,requireActivity())
        binding.recLastestPackages.layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.recLastestPackages.adapter=adapter
    }

    override fun onResume() {
        super.onResume()
        if(preference.getStringpreference("userImage",null).isNotBlank()){
            binding.profile.setImageBitmap(Constants.decodeBase64(preference.getStringpreference("userImage")))
        }else{
            binding.profile.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.male_avatar))
        }
    }
}