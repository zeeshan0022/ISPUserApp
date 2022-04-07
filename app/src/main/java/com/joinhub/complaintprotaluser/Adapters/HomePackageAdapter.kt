package com.joinhub.complaintprotaluser.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.complaintprotaluser.databinding.ItemPackagelatestBinding
import com.joinhub.complaintprotaluser.models.PackageDetails

class HomePackageAdapter(val list: MutableList<PackageDetails>, val activity: Activity):
    RecyclerView.Adapter<HomePackageAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPackagelatestBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPackagelatestBinding.inflate(LayoutInflater.from(activity),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            with(list[position]){
                txtTitle.text=pkgName
                txtSpeed.text=pkgSpeed
                txtVolume.text=pkgVolume
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}