
package com.joinhub.complaintprotaluser.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joinhub.complaintprotaluser.databinding.ActivityContactUsBinding

class ContactUsActivity : AppCompatActivity() {
    lateinit var binding:ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }
    }
}