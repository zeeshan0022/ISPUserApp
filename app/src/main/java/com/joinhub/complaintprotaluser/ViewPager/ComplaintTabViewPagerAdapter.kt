package com.joinhub.complaintprotaluser.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.joinhub.complaintprotaluser.fragments.AddComplatintFragment
import com.joinhub.complaintprotaluser.fragments.ComplaintFeedbackFragment
import com.joinhub.complaintprotaluser.fragments.ComplaintHistory
import com.joinhub.complaintprotaluser.fragments.LiveChatFragment

class ComplaintTabViewPagerAdapter(fragmentManager:FragmentManager, lifeCycle:Lifecycle) : FragmentStateAdapter(fragmentManager,lifeCycle) {
    private val NUM_PAGES=4
    override fun getItemCount(): Int {

        return NUM_PAGES;
    }

    override fun createFragment(position: Int): Fragment {

        when(position){
            0->  return AddComplatintFragment()
            1-> return ComplaintHistory()
            2->  return LiveChatFragment()
            3-> return ComplaintFeedbackFragment()

        }
        return AddComplatintFragment()
    }
}