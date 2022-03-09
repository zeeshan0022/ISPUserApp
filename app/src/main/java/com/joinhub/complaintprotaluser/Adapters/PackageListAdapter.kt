package com.joinhub.complaintprotaluser.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.activities.PackageDetailsActivity
import com.joinhub.complaintprotaluser.databinding.ItemPackageListBinding
import com.joinhub.complaintprotaluser.databinding.ListComplaintHistoryItemsBinding
import com.joinhub.complaintprotaluser.models.ComplaintModel
import com.joinhub.complaintprotaluser.models.PackageDetails

class PackageListAdapter(val context: Context, private val list:List<PackageDetails>, val unLimit:Boolean): RecyclerView.Adapter<PackageListAdapter.ViewHolder>() {
    var  preference: Preference= Preference(context)
    class ViewHolder( val binding: ItemPackageListBinding): RecyclerView.ViewHolder( binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPackageListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       with(holder){
           binding.txtTitle.text= list[position].pkgName
           binding.txtPrice.text= "Rs. "+list[position].pkgRate+"/month"
           binding.txtSpeed.text= list[position].pkgSpeed
           binding.txtVolume.text= list[position].pkgVolume
           if(preference.getIntpreference("pkgID")==list[position].pkgID){
               binding.btnUpgrade.text= "Active Package"
           }

           binding.btnUpgrade.setOnClickListener {
               if(preference.getIntpreference("pkgID")==list[position].pkgID) {

                   val i= Intent(context, PackageDetailsActivity::class.java)
                   i.putExtra("value", false)
                   context.startActivity(i)
               }else{
                   val i= Intent(context, PackageDetailsActivity::class.java)
                   i.putExtra("value", true)
                   i.putExtra("pos", position)
                   i.putExtra("un", unLimit)
                   context.startActivity(i)
               }

           }
       }
    }

    override fun getItemCount(): Int {
       return  list.size
    }
}