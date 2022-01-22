package com.joinhub.complaintprotaluser.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.joinhub.complaintprotaluser.fragments.AllPackagesFragment
import com.joinhub.complaintprotaluser.fragments.BillingHistoryFragment
import com.joinhub.complaintprotaluser.fragments.BillingHomeFragment
import com.joinhub.complaintprotaluser.fragments.UnlimitedPackagesFragment

class BillingViewPagerAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifeCycle) {
    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return BillingHomeFragment()
            1 -> return BillingHistoryFragment()
        }
        return BillingHomeFragment()
    }

}