package com.joinhub.complaintprotaluser.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.complaintprotaluser.databinding.ListComplaintHistoryItemsBinding
import com.joinhub.complaintprotaluser.models.ComplaintHistoryModel

class ComplaintHistoryAdapter(val context:Context, val list:List<ComplaintHistoryModel>): RecyclerView.Adapter<ComplaintHistoryAdapter.ViewHolder>() {


    class ViewHolder( val binding:ListComplaintHistoryItemsBinding): RecyclerView.ViewHolder( binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListComplaintHistoryItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){

        }
    }

    override fun getItemCount(): Int {

        return list.size
    }
}