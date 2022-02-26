package com.joinhub.complaintprotaluser.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.databinding.FragmentAddComplatintBinding
import com.joinhub.complaintprotaluser.interfaces.AddComplaintInterface
import com.joinhub.complaintprotaluser.models.ComplaintModel
import com.joinhub.complaintprotaluser.presentator.AddComplaintPresentator
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.checkGoogleAPI
import java.text.SimpleDateFormat
import java.util.*


class AddComplatintFragment : Fragment() , AddComplaintInterface {
    private lateinit var preference: Preference
    private val REQUEST_LOCATION = 1
    var pattern = "dd-MM-yyyy"
    @SuppressLint("SimpleDateFormat")
    var simpleDateFormat = SimpleDateFormat(pattern)
    var date = simpleDateFormat.format(Date())
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var binding: FragmentAddComplatintBinding
    private var currentLatitude = 0.0
    private lateinit var  locationManager: LocationManager
    private lateinit var fusedLocationProviderClientGoogle: com.google.android.gms.location.FusedLocationProviderClient
    private var currentLongitude = 0.0
    private lateinit var issue:String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback
    // Define a device setting client.
    private lateinit var settingsClient: SettingsClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddComplatintBinding.inflate(inflater, container, false)
        init()
        if(checkGoogleAPI(requireActivity())){
            initGoogleLocation()
        }else{
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            settingsClient = LocationServices.getSettingsClient(requireActivity())
            mLocationRequest = LocationRequest().apply {
                interval = 1000
                needAddress = true
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            iniHuaweiLocation()
        }

        binding.locationLayout.setEndIconOnClickListener{
            if(checkGoogleAPI(requireActivity())){
                locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS()
                } else {
                    getLocation()
                }

            }else{

                requestLocationUpdatesWithCallback()
            }
        }
        //radio Button Hanlder
        binding.radioFre.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                issue= binding.radioFre.text.toString()
             binding.radioFre.isChecked= true
             binding.radioGaming.isChecked=false
             binding.radioModem.isChecked=false
                binding.radioSlowBro.isChecked =false
            }
        }
        binding.radioGaming.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                issue= binding.radioGaming.text.toString()
                binding.radioFre.isChecked= false
                binding.radioGaming.isChecked=true
                binding.radioModem.isChecked=false
                binding.radioSlowBro.isChecked =false
            }
        }
        binding.radioModem.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                issue= binding.radioModem.text.toString()
                binding.radioFre.isChecked= false
                binding.radioGaming.isChecked=false
                binding.radioModem.isChecked=true
                binding.radioSlowBro.isChecked =false
            }
        }
        binding.radioSlowBro.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                issue= binding.radioSlowBro.text.toString()
                binding.radioFre.isChecked= false
                binding.radioGaming.isChecked=false
                binding.radioModem.isChecked=false
                binding.radioSlowBro.isChecked =true
            }
        }

        binding.btnSendComplaint.setOnClickListener {
            sendComplaint(
                binding.nameEditText.text.toString().trim(),
                binding.descEditText.text.toString().trim()
            )
        }
        return binding.root
    }

    private fun sendComplaint(name: String, desc: String) {
     if(name.isBlank() ||desc.isBlank() || binding.locationEditText.text?.isBlank()!!){
         if(name.isBlank()){
             binding.nameLayout.error="Please Enter Name"
         }else{
             binding.nameLayout.error=null
         }
         if(desc.isBlank()){
             binding.descLayout.error="Please Description"
         }else{
             binding.descLayout.error=null
         }

         if(binding.locationEditText.text.toString().isBlank()){
             binding.locationLayout.error="Please Click on Location Button "
         }else{
             binding.locationLayout.error=null
         }
     }else{
         binding.locationLayout.error=null
         binding.nameLayout.error=null
         binding.descLayout.error=null

         showDialog()

     }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Register Complaint")
            .setMessage("Are you sure you want to Register Complaint?")
            .setPositiveButton(
                "Yes"
            ) { dialog, _ ->
                var ticket: Int
                val presentator = AddComplaintPresentator(this@AddComplatintFragment,requireContext(),requireActivity())
                if(preference.getIntpreference("ticket")==0){
                ticket= 1000
                    preference.setIntpreference("ticket",ticket)
                }else{
                    ticket= preference.getIntpreference("ticket")
                    preference.setIntpreference("ticket",++ticket)
                }
                presentator.saveToDatabase(ComplaintModel(1, "tk$ticket",binding.nameEditText.text.toString().trim(),
                binding.phoneEditText.text.toString().trim(), preference.getStringpreference("userEmail",null).toString(),
                currentLongitude.toString(),currentLatitude.toString(),issue,binding.descEditText.text.toString().trim(), "Active",preference.getIntpreference("serviceID"),
                preference.getIntpreference("userID"),date))
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            val locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (locationGPS != null) {
                val lat = locationGPS.latitude
                val longi = locationGPS.longitude
                currentLatitude = lat
                currentLatitude = longi
                getAddress(lat,longi)
            } else {
                Toast.makeText(requireContext(), "Unable to find location.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun OnGPS() {
       val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes"
        ) { _, _ ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun initGoogleLocation() {
        fusedLocationProviderClientGoogle= com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    @SuppressLint("SetTextI18n")
    fun getAddress(lat: Double, lng: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        val city = addresses[0].locality
        val country = addresses[0].countryName
        val knownName = addresses[0].featureName
    binding.locationEditText.setText("$address $city $country $knownName")
    }
    private fun removeLocationUpdatesWithCallback() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        } catch (e: Exception) {
        }
    }
    private fun requestLocationUpdatesWithCallback() {
        try {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            // Check the device settings before requesting location updates.
            val locationSettingsResponseTask: Task<LocationSettingsResponse> =
                settingsClient.checkLocationSettings(locationSettingsRequest)

            locationSettingsResponseTask.addOnSuccessListener { locationSettingsResponse: LocationSettingsResponse? ->
                Log.i("Huawei", "check location settings success  {$locationSettingsResponse}")
                // Request location updates.
                fusedLocationProviderClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.getMainLooper()
                )
                    .addOnSuccessListener {
                        Log.i("Huawei", "requestLocationUpdatesWithCallback onSuccess")
                    }
                    .addOnFailureListener { e ->
                        Log.e(
                            "Huawei",
                            "requestLocationUpdatesWithCallback onFailure:${e.message}"
                        )
                    }
            }
                .addOnFailureListener { e: Exception ->
                    Log.e("Huawei", "checkLocationSetting onFailure:${e.message}")
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(requireActivity(), 0
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.e("Huawei", "PendingIntent unable to execute request.")
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e("Huawei", "requestLocationUpdatesWithCallback exception:${e.message}")
        }
    }
    private fun iniHuaweiLocation() {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult != null) {
                        val locations: List<Location> =
                            locationResult.locations
                        if (locations.isNotEmpty()) {
                            for (location in locations) {
                               currentLatitude= location.latitude
                                currentLongitude= location.longitude
                                getAddress(currentLatitude,currentLongitude)
                               removeLocationUpdatesWithCallback()
                            }
                        }
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                    locationAvailability?.let {
                        val flag: Boolean = locationAvailability.isLocationAvailable
                        Log.i("Huawei", "onLocationAvailability isLocationAvailable:$flag")
                    }
                }
            }

    }


    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(requireActivity(), strings, 1)
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(requireActivity(), strings, 2)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {


            } else {

            }
        }
        if (requestCode == 2) {
            if (grantResults.size > 2 && grantResults[2] == PERMISSION_GRANTED && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {

            } else {

            }
        }
    }



    @SuppressLint("SetTextI18n")
    private fun init() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        preference = Preference(requireContext())
        if (preference.getIntpreference("userID") > 0) {
            binding.phoneEditText.setText("0" + preference.getStringpreference("userPhone",null))
            binding.addressEditText.setText(preference.getStringpreference("userAddress", null))
        }

    }

    override fun onStarts() {
        binding.progressBar.visibility= View.VISIBLE
    }

    override fun onSuccess() {
        Toast.makeText(requireContext(), "Complaint Registered Successfully", Toast.LENGTH_LONG).show()
        binding.progressBar.visibility= View.GONE
    }

    override fun onError(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_LONG).show()
        binding.progressBar.visibility= View.GONE
    }


}
