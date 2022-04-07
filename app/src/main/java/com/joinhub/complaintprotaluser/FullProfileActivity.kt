package com.joinhub.complaintprotaluser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.databinding.ActivityFullProfileBinding

class FullProfileActivity : AppCompatActivity() {
    lateinit var preference: Preference
    lateinit var binding:ActivityFullProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFullProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference= Preference(this)

        binding.back.setOnClickListener { finish() }
        binding.txtName.text= preference.getStringpreference("userFullName",null)
        binding.txtPhone.text=preference.getStringpreference("userPhone",null)

    }
}