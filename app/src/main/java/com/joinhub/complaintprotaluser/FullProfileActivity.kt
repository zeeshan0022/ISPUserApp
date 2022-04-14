package com.joinhub.complaintprotaluser

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.databinding.ActivityFullProfileBinding
import com.joinhub.complaintprotaluser.utilties.Constants
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException


class FullProfileActivity : AppCompatActivity() {
    lateinit var preference: Preference
    lateinit var binding:ActivityFullProfileBinding
    lateinit var selectedImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFullProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference= Preference(this)

        binding.back.setOnClickListener { finish() }
        binding.txtName.text= preference.getStringpreference("userFullName",null)
        binding.txtPhone.text=preference.getStringpreference("userPhone",null)
        binding.txtUserName.text= preference.getStringpreference("userName",null)
        binding.txtEmail.text= preference.getStringpreference("userEmail",null)
        binding.txtSRNAMe.text= preference.getStringpreference("serviceName",null)
        binding.txtpkgName.text= preference.getStringpreference("pkgName",null)
        binding.txtAddress.text= preference.getStringpreference("userAddress",null)

        binding.editImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            singleImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }
        if(preference.getStringpreference("userImage",null).isNotBlank()){
            binding.userImage.setImageBitmap(Constants.decodeBase64(preference.getStringpreference("userImage")))
        }else{
            binding.userImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.male_avatar))
        }
    }

    private val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                 selectedImageUri = data?.data!!

                binding.userImage.setImageURI(selectedImageUri)
                saveImage()
            }
        }

    fun saveImage(){

        preference.setStringpreference("userImage", encodeTobase64(uriToBitmap(selectedImageUri)))

    }
    private fun uriToBitmap(selectedFileUri: Uri):Bitmap {
        var image: Bitmap? =null
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
            return image!!
        }
    }
    private fun getPathFromURI(uri: Uri?): String {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)

        return imageEncoded
    }
}