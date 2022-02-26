package com.joinhub.complaintprotaluser.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.databinding.FragmentHomeBinding
import com.joinhub.complaintprotaluser.interfaces.HomeInterface
import com.joinhub.complaintprotaluser.models.ServiceModel
import com.joinhub.complaintprotaluser.models.UserModel
import com.joinhub.complaintprotaluser.presentator.HomePresentator
import com.joinhub.complaintprotaluser.utilties.Constants


class HomeFragment : Fragment(), HomeInterface {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var  preference:Preference
    lateinit var  model: UserModel
    lateinit var  presenatator:HomePresentator
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        presenatator= HomePresentator(this@HomeFragment, requireActivity())
        preference= Preference(requireContext())
        if(preference.getIntpreference("userID")==0){
            presenatator.loadData(preference.getStringpreference("userName").toString())
        }else{
            binding.txtPhone.text=preference.getStringpreference("userPhone",null)
            binding.txtUserName.text= preference.getStringpreference("userFullName",null)
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
}