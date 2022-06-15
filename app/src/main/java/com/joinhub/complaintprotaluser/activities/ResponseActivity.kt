package com.joinhub.complaintprotaluser.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joinhub.complaintprotaluser.databinding.ActivityResponseBinding
import com.nouman.jazzcashlib.Constants
import com.nouman.jazzcashlib.JazzCashResponse


class ResponseActivity : AppCompatActivity() {
    lateinit var binding:ActivityResponseBinding
    var jazzCashResponse: JazzCashResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent != null) {
            jazzCashResponse =
                intent.getSerializableExtra(Constants.jazzCashResponse) as JazzCashResponse?
            binding.reponse.text= jazzCashResponse!!.ppResponseMessage
        }
    }
}