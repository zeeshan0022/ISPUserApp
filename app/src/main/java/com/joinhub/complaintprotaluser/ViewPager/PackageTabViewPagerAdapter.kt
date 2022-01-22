package com.joinhub.complaintprotaluser.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.joinhub.complaintprotaluser.fragments.*

class PackageTabViewPagerAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifeCycle) {
    override fun getItemCount(): Int {
       return 2
    }


    override fun createFragment(position: Int): Fragment {
        when(position){
            0->  return AllPackagesFragment()
            1-> return UnlimitedPackagesFragment()
        }
        return AllPackagesFragment()
    }


}