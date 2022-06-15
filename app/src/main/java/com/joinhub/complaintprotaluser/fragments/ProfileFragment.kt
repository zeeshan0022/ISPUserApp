package com.joinhub.complaintprotaluser.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.FullProfileActivity
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.SpeedTestActivity
import com.joinhub.complaintprotaluser.activities.SettingsActivity
import com.joinhub.complaintprotaluser.activities.SigninActivity
import com.joinhub.complaintprotaluser.databinding.FragmentProfileBinding
import com.joinhub.complaintprotaluser.utilties.Constants


class ProfileFragment : Fragment() {
    lateinit var binding:FragmentProfileBinding
    lateinit var preference: Preference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)
        preference= Preference(requireContext())
        binding.cardSettings.setOnClickListener{
            startActivity(Intent(context, SettingsActivity::class.java))
        }
        binding.materialCardView10.setOnClickListener { startActivity(Intent(requireContext(),SpeedTestActivity::class.java)) }
        binding.txtEdit.setOnClickListener { startActivity(Intent(requireContext(),FullProfileActivity::class.java)) }
        binding.txtPName.text= preference.getStringpreference("userFullName")
        binding.txtPhone.text= preference.getStringpreference("userPhone")
        binding.btnLogout.setOnClickListener {
            preference.logout()
            startActivity(Intent(requireContext(),SigninActivity::class.java))
            requireActivity().finish()

        }

        binding.cardRate.setOnClickListener {
            showDialog(requireActivity())
        }
        return binding.root
    }

    fun showDialog(activity: Activity?) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.rate_us_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogButton: ImageView = dialog.findViewById(R.id.closerate)
        val rateUs: CardView = dialog.findViewById(R.id.rateuscard)
        val leaveSuggestion: CardView = dialog.findViewById(R.id.leaveSuggestion)
        leaveSuggestion.setOnClickListener {
            //startActivity(Intent(context, ::class.java))
            dialog.dismiss()
        }
        rateUs.setOnClickListener {
            val uri: Uri = Uri.parse("market://details?id=" + requireActivity().packageName)
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
                dialog.dismiss()
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show()
            }
        }
        dialogButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if(preference.getStringpreference("userImage",null).isNotBlank()){
            binding.profile.setImageBitmap(Constants.decodeBase64(preference.getStringpreference("userImage")))
        }else{
            binding.profile.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.male_avatar))
        }
    }
}