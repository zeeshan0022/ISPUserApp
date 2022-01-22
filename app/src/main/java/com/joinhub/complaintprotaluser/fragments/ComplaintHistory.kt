package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joinhub.complaintprotaluser.databinding.FragmentComplaintHistoryBinding

import android.widget.ArrayAdapter
import com.joinhub.complaintprotaluser.interfaces.ComplaintHistoryInterface


val type = arrayOf("Active", "Solved", "Rejected", "Working")

class ComplaintHistory(): Fragment(), ComplaintHistoryInterface {

    private var _binding: FragmentComplaintHistoryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentComplaintHistoryBinding.inflate(inflater, container, false)
        initDropDown()
        initComplaintHistoryRec()
        return binding.root
    }

    private fun initComplaintHistoryRec() {

    }

    fun initDropDown(){

        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,type)
        binding.filledExposedDropdown.setAdapter(adapter)
    }

    override fun onFetch() {
        TODO("Not yet implemented")
    }

    override fun onError() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

}