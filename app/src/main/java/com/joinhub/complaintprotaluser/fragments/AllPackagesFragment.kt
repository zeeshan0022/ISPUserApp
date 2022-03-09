package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.complaintprotaluser.Adapters.PackageListAdapter
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.databinding.FragmentAllPackagesBinding
import com.joinhub.complaintprotaluser.interfaces.PackageInterface
import com.joinhub.complaintprotaluser.models.ComplaintModel
import com.joinhub.complaintprotaluser.models.PackageDetails
import com.joinhub.complaintprotaluser.presentator.PackagePresenatator

class AllPackagesFragment: Fragment(), PackageInterface {
    lateinit var  binding:FragmentAllPackagesBinding
    lateinit var  presenatator:PackagePresenatator
    lateinit var adapter: PackageListAdapter
    companion object {
        lateinit var listPack: MutableList<PackageDetails>
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       listPack = mutableListOf()
        binding= FragmentAllPackagesBinding.inflate(layoutInflater)
        presenatator= PackagePresenatator(this@AllPackagesFragment, requireActivity())
        if(listPack.isEmpty()){
            presenatator.loadAllPack(false)
        }else{
            setUpRecyclerView(listPack)
        }
        binding.swipeLay.setOnRefreshListener {
            listPack.clear()
            presenatator.loadAllPack(false)
        }
        return binding.root
    }

    private fun setUpRecyclerView(listPackage: MutableList<PackageDetails>) {
        adapter= PackageListAdapter(requireContext(), listPackage,false)
        binding.allPackageRecycler.layoutManager= LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        binding.allPackageRecycler.adapter= adapter
    }

    override fun onStarts() {
       binding.progressBar.visibility= View.VISIBLE
    }

    override fun onSuccess(list: MutableList<PackageDetails>) {
        listPack.addAll(list)

        setUpRecyclerView(list)
        binding.progressBar.visibility= View.GONE
        binding.swipeLay.isRefreshing= false
    }

    override fun onError(e: String) {

        binding.progressBar.visibility= View.GONE
        binding.swipeLay.isRefreshing= false

    }


}