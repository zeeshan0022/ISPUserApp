package com.joinhub.complaintprotaluser.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.databinding.ItemBillingHistoryBinding
import com.joinhub.complaintprotaluser.models.BillingModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
                  showDialog(context, list[position])
              }
          }


      }
    }
    @SuppressLint("SetTextI18n")
    fun showDialog(activity: Context?, model: BillingModel) {



        val dialog = Dialog(activity!!)
        val preference=Preference(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_billing_invoice)
        val billingID= dialog.findViewById<TextView>(R.id.invoice_id)
        val date= dialog.findViewById<TextView>(R.id.invoice_date)
        val packageName= dialog.findViewById<TextView>(R.id.txtPackName)
        val customerName= dialog.findViewById<TextView>(R.id.txtCustomer)
        val method= dialog.findViewById<TextView>(R.id.txtMethod)
        val txtStatus= dialog.findViewById<TextView>(R.id.txtStatus)
        val rate= dialog.findViewById<TextView>(R.id.txtRate)
        val total= dialog.findViewById<TextView>(R.id.txtTotal)
        billingID.text= "ID: bill001"+model.billingID
        date.text="Date: "+model.billingDate
        packageName.text= "Package Name: " + model.pkgName
        customerName.text= "Customer Name: "+preference.getStringpreference("userFullName", null)
        method.text= "Payment Method: "+ model.billingMethod
        txtStatus.text="Status: "+model.status
        rate.text= "Rs."+model.charges
        total.text= "Rs."+model.charges
        val llPdf = dialog.findViewById<LinearLayout>(R.id.llPdf)
        val llShare = dialog.findViewById<LinearLayout>(R.id.llShare)
        llShare.setOnClickListener {
            shareImageUri(saveImage(getBitmapFromView(dialog.findViewById(R.id.layout))!!)!!)
        }

        llPdf.setOnClickListener {

            try {
                val filename= File(Environment.getExternalStorageDirectory(),model.pkgName+"_${model.month}"+".png")

                FileOutputStream(filename).use { out ->
                    getBitmapFromView(dialog.findViewById(R.id.layout))!!.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
                    Toast.makeText(context, "Saved to Gallery ", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
            }
        }
        dialog.show()


    }


    private fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }
    override fun getItemCount(): Int {
      return list.size
    }

    private fun saveImage(image: Bitmap): Uri? {
        //TODO - Should be processed in another thread
        val imagesFolder = File(context.cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(context, "com.joinhub.complaintprotaluser.fileprovider", file)
        } catch (e: IOException) {
            Log.d(TAG, "IOException while trying to write file for sharing: " + e.localizedMessage)
        }
        return uri
    }
    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        context.startActivity(intent)
    }
    }