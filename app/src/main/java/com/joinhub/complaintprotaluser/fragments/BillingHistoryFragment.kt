package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.Adapters.BillingHistoryAdapter
import com.joinhub.complaintprotaluser.databinding.FragmentBillingHistoryBinding
import com.joinhub.complaintprotaluser.interfaces.BillingHistory
import com.joinhub.complaintprotaluser.models.BillingModel
import com.joinhub.complaintprotaluser.presentator.BillingPresentator

class BillingHistoryFragment : Fragment(), BillingHistory<BillingModel> {
    lateinit var adapter:BillingHistoryAdapter
    companion object {
        lateinit var list1: MutableList<BillingModel>
    }lateinit var binding:FragmentBillingHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentBillingHistoryBinding.inflate(layoutInflater,container,false)
        list1= mutableListOf()
        if(list1.isEmpty()){
            val presentator= BillingPresentator<Any>(this, requireActivity())
            presentator.loadHistory(Preference(requireContext()).getIntpreference("userID"))
        }else{
            setUpRec(list1)
        }
        return binding.root
    }

    override fun onStarts() {
    binding.progressBar.visibility=View.VISIBLE
    }

    override fun onSuccess(list: MutableList<BillingModel>) {
        binding.progressBar.visibility=View.GONE
        list1.addAll(list)
        setUpRec(list)
    }

    private fun setUpRec(list: MutableList<BillingModel>) {
        adapter= BillingHistoryAdapter(requireContext(),list)
        binding.recBillingHistory.layoutManager= LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)
        binding.recBillingHistory.adapter= adapter
    }

    override fun onError(e: String) {
        binding.progressBar.visibility=View.GONE
        Toast.makeText(requireContext(), e,Toast.LENGTH_LONG).show()

    }

}