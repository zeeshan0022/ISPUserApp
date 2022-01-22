package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.ViewPager.BillingViewPagerAdapter
import com.joinhub.complaintprotaluser.ViewPager.PackageTabViewPagerAdapter
import com.joinhub.complaintprotaluser.databinding.FragmentBillingsBinding
private var tabBillingArray= arrayOf(
"Billing",
    "History"
)
class BillingsFragment : Fragment() {
    lateinit var binding:FragmentBillingsBinding
    lateinit var adapter:BillingViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBillingsBinding.inflate(layoutInflater,container,false)
        settingUpViewPager()

        return binding.root
    }
    private  fun  settingUpViewPager(){
        adapter = BillingViewPagerAdapter(
            activity?.supportFragmentManager!!,
            activity?.lifecycle!!

        )

        binding.viewPagerBilling.adapter= adapter
        TabLayoutMediator(binding.tabBilling,binding.viewPagerBilling) { tab, position ->
            tab.text = tabBillingArray[position]
        }.attach()
    }

}