package com.joinhub.complaintprotaluser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.ViewPager.ComplaintTabViewPagerAdapter
import com.joinhub.complaintprotaluser.databinding.FragmentComplaintBinding
import com.joinhub.khataapp.Utilites.ThemeDataStore.PreferencesKeys.name

val tabArray = arrayOf(
    "Complaint",
    "History",
    "Live Chat",
    "Feedback"
)

class ComplaintFragment : Fragment() {

    val tabIconArray= arrayOf(
        R.drawable.ic_complaint,
        R.drawable.ic_complaint_history,
        R.drawable.ic_customer_service,
        R.drawable.ic_positive_feedback
    )
    private var _binding: FragmentComplaintBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterComplaint: ComplaintTabViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplaintBinding.inflate(inflater, container, false)
        settingUpViewPager()
        return binding.root
    }

    private fun settingUpViewPager() {
        adapterComplaint = ComplaintTabViewPagerAdapter(
            activity?.supportFragmentManager!!,
            activity?.lifecycle!!
        )
        binding.complaintViewPager.adapter= adapterComplaint
        TabLayoutMediator(binding.tabComplaint,binding.complaintViewPager) { tab, position ->
            tab.text = tabArray[position]
            tab.icon= ContextCompat.getDrawable(requireActivity(), tabIconArray[position]);
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}