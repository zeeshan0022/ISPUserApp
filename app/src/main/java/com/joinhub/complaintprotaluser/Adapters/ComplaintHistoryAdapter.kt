package com.joinhub.complaintprotaluser.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.activities.ComplaintDetail
import com.joinhub.complaintprotaluser.databinding.ListComplaintHistoryItemsBinding
import com.joinhub.complaintprotaluser.models.ComplaintModel
import com.squareup.picasso.Picasso

class ComplaintHistoryAdapter(val context:Context, private val list:List<ComplaintModel>): RecyclerView.Adapter<ComplaintHistoryAdapter.ViewHolder>() {


    private var preference: Preference= Preference(context)
    class ViewHolder( val binding:ListComplaintHistoryItemsBinding): RecyclerView.ViewHolder( binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListComplaintHistoryItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
           binding.itemTicketNo.text="Ticket No: "+ list[position].complaintTicketNo
           binding.itemIssueType.text="Issue Type: "+ list[position].complaintIssue
           binding.itemSRName.text="Under Service: "+ preference.getStringpreference("serviceName",null)
           binding.itemAreaCode.text= "Area Code: "+ preference.getStringpreference("areaCode",null)

            when (list[position].complaintStatus) {
                "Active" -> {
                    Picasso.get().load(R.drawable.activeicon).into(binding.itemStatusIcon)
                }
                "Solved" -> {
                    Picasso.get().load(R.drawable.solved).into(binding.itemStatusIcon)
                }
                "Rejected" -> {
                    Picasso.get().load(R.drawable.reject).into(binding.itemStatusIcon)
                }
                "Working" -> {
                    Picasso.get().load(R.drawable.workings).into(binding.itemStatusIcon)
                }
                "Cancelled" -> {
                    Picasso.get().load(R.drawable.cancelled).into(binding.itemStatusIcon)
                }
            }
            binding.cardItem.setOnClickListener{
                val intent= Intent(context, ComplaintDetail::class.java)
                intent.putExtra("pos",position)
                context.startActivity(intent)

            }
        }

    }

    override fun getItemCount(): Int {

        return list.size
    }
}