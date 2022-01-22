package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.joinhub.complaintprotaluser.ViewPager.ComplaintTabViewPagerAdapter
import com.joinhub.complaintprotaluser.ViewPager.PackageTabViewPagerAdapter
import com.joinhub.complaintprotaluser.databinding.FragmentPackagesBinding


private val tabPackagesArray = arrayOf(
    "All",
    "Unlimited Volume"
)

class PackagesFragment : Fragment() {

    private var _binding: FragmentPackagesBinding? = null
    private lateinit var adapterPackaTabViewPagerAdapter: PackageTabViewPagerAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPackagesBinding.inflate(inflater, container, false)
        settingUpViewPager()

        return binding.root
    }

    private fun settingUpViewPager(){
        adapterPackaTabViewPagerAdapter = PackageTabViewPagerAdapter(
            activity?.supportFragmentManager!!,
            activity?.lifecycle!!

        )

        binding.viewPagerPackages.adapter= adapterPackaTabViewPagerAdapter
        TabLayoutMediator(binding.tabPackages,binding.viewPagerPackages) { tab, position ->
            tab.text = tabPackagesArray[position]
        }.attach()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}