package com.joinhub.complaintprotaluser.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.activities.SettingsActivity
import com.joinhub.complaintprotaluser.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)


        binding.cardSettings.setOnClickListener{
            startActivity(Intent(context, SettingsActivity::class.java))
        }
        return binding.root
    }

}