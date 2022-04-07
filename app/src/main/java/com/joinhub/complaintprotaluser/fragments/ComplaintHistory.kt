package com.joinhub.complaintprotaluser.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.Adapters.ComplaintHistoryAdapter
import com.joinhub.complaintprotaluser.databinding.FragmentComplaintHistoryBinding

import com.joinhub.complaintprotaluser.interfaces.ComplaintHistoryInterface
import com.joinhub.complaintprotaluser.models.ComplaintModel
import com.joinhub.complaintprotaluser.presentator.ComplaintHistoryPresentator
import org.ksoap2.serialization.SoapObject


val type = arrayOf("All","Active", "Solved", "Rejected", "Working","Cancelled")

class ComplaintHistory(): Fragment(), ComplaintHistoryInterface {

    lateinit var adapter:ComplaintHistoryAdapter
    lateinit var methodType:String
    private var _binding:FragmentComplaintHistoryBinding? = null
    private val binding get() = _binding!!
    lateinit var  preference: Preference
    lateinit var  presenatator:ComplaintHistoryPresentator
    companion object{
    lateinit var complaintList: MutableList<ComplaintModel>
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentComplaintHistoryBinding.inflate(inflater, container, false)
        initDropDown()
        complaintList= mutableListOf()
        initComplaintHistoryRec()
        if(complaintList.isEmpty()){
            presenatator.loadComplaint(preference.getIntpreference("userID"))
        }else{
            setUpRec(complaintList)
        }


        binding.filledExposedDropdown.setOnItemClickListener { parent, view, position, id ->
            val custList:MutableList<ComplaintModel> = mutableListOf()
            when {
                binding.filledExposedDropdown.text.toString()=="All" -> {
                    setUpRec(complaintList)
                }
                binding.filledExposedDropdown.text.toString()=="Active" -> {
                    custList.clear()
                    for(model in complaintList){
                        if(model.complaintStatus=="Active"){
                            custList.add(model)
                        }
                    }
                    setUpRec(custList)
                }
                binding.filledExposedDropdown.text.toString()=="Solved" -> {
                    custList.clear()
                    for(model in complaintList){
                        if(model.complaintStatus=="Solved"){
                            custList.add(model)
                        }
                    }
                    setUpRec(custList)
                }
                binding.filledExposedDropdown.text.toString()=="Rejected" -> {
                    custList.clear()
                    for(model in complaintList){
                        if(model.complaintStatus=="Rejected"){
                            custList.add(model)
                        }
                    }
                    setUpRec(custList)
                }
                binding.filledExposedDropdown.text.toString()=="Working" -> {
                    custList.clear()
                    for(model in complaintList){
                        if(model.complaintStatus=="Working"){
                            custList.add(model)
                        }
                    }
                    setUpRec(custList)
                }
                binding.filledExposedDropdown.text.toString()=="Cancelled" -> {
                    custList.clear()
                    for(model in complaintList){
                        if(model.complaintStatus=="Cancelled"){
                            custList.add(model)
                        }
                    }
                    setUpRec(custList)
                }
            }

        }


        binding.swipeLay.setOnRefreshListener {
            complaintList.clear()
            presenatator.loadComplaint(preference.getIntpreference("userID"))
            binding.swipeLay.isRefreshing = false
        }
        return binding.root
    }

    private fun initComplaintHistoryRec() {
        preference= Preference(requireContext())
        presenatator= ComplaintHistoryPresentator(this@ComplaintHistory,requireContext(), requireActivity())
    }

    fun initDropDown(){

        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,type)
        binding.filledExposedDropdown.setAdapter(adapter)
    }

    override fun onStarts() {
        binding.progressBar.visibility=View.VISIBLE
        binding.recComplaintHistory.visibility= View.GONE
        binding.txtNoHistory.visibility=View.GONE
    }
    override fun onError(e: String) {
        binding.recComplaintHistory.visibility=View.GONE
        binding.progressBar.visibility= View.GONE
        binding.txtNoHistory.visibility=View.VISIBLE
        binding.swipeLay.isRefreshing= false
    }

    override fun onSuccess(list: List<ComplaintModel>) {
        binding.recComplaintHistory.visibility=View.VISIBLE
        binding.progressBar.visibility= View.GONE
        binding.txtNoHistory.visibility=View.GONE
        complaintList.addAll(list)
        binding.swipeLay.isRefreshing= false
        setUpRec(complaintList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpRec(complaintList: MutableList<ComplaintModel>) {
        adapter= ComplaintHistoryAdapter(requireContext(),complaintList)
        binding.recComplaintHistory.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,
        false)
        binding.recComplaintHistory.adapter= adapter
    }


}