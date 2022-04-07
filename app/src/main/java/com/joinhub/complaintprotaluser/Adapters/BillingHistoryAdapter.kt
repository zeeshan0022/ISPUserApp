package com.joinhub.complaintprotaluser.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.complaintprotaluser.databinding.ItemBillingHistoryBinding
import com.joinhub.complaintprotaluser.models.BillingModel

class BillingHistoryAdapter(val context: Context, val list:List<BillingModel>):
    RecyclerView.Adapter<BillingHistoryAdapter.BillingViewHolder>() {

    class BillingViewHolder(val binding:ItemBillingHistoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {


        return BillingViewHolder(ItemBillingHistoryBinding.inflate(LayoutInflater.from(context),
        parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
      with(holder){
          with(list[position]){
              binding.txtPackName.text= pkgName
              binding.txtMethod.text= "Payment Method: $billingMethod"
              binding.txtRate.text = "Rs.$charges"
              binding.txtSpeed.text= "Status: $status"
              binding.txtdate.text= "Date$billingDate"


              binding.card.setOnClickListener {

              }
          }


      }
    }

    override fun getItemCount(): Int {
      return list.size
    }
}