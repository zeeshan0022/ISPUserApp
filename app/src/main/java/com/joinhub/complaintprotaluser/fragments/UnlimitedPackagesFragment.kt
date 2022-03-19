package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.complaintprotaluser.Adapters.PackageListAdapter
import com.joinhub.complaintprotaluser.databinding.FragmentUnlimitedPackageBinding
import com.joinhub.complaintprotaluser.interfaces.PackageInterface
import com.joinhub.complaintprotaluser.models.PackageDetails
import com.joinhub.complaintprotaluser.presentator.PackagePresenatator

class UnlimitedPackagesFragment: Fragment(), PackageInterface {
    lateinit var  binding: FragmentUnlimitedPackageBinding
    lateinit var  presenatator: PackagePresenatator
    lateinit var adapter: PackageListAdapter
    companion object {
        lateinit var listPack: MutableList<PackageDetails>
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentUnlimitedPackageBinding.inflate(inflater,container,false)

        listPack = mutableListOf()
        presenatator= PackagePresenatator(this@UnlimitedPackagesFragment, requireActivity())
        if(listPack.isEmpty()){
            presenatator.loadAllPack(true)
        }else{
            setUpRecyclerView(listPack)
        }
        binding.swipeLay.setOnRefreshListener {
            listPack.clear()
            presenatator.loadAllPack(true)
        }
        return binding.root
    }

    private fun setUpRecyclerView(listPackage: MutableList<PackageDetails>) {
        adapter= PackageListAdapter(requireContext(), listPackage,true)
        binding.allPackageRecycler.layoutManager= LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)
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