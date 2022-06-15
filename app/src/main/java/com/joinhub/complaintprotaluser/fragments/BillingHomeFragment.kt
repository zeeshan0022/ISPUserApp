package com.joinhub.complaintprotaluser.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.test.core.app.ApplicationProvider
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.iap.Iap
import com.huawei.hms.iap.IapApiException
import com.huawei.hms.iap.IapClient
import com.huawei.hms.iap.entity.*
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.R
import com.joinhub.complaintprotaluser.activities.PackageDetailsActivity
import com.joinhub.complaintprotaluser.activities.PaymentActivity
import com.joinhub.complaintprotaluser.databinding.FragmentBillingHomeBinding
import com.joinhub.complaintprotaluser.fragments.BillingHistoryFragment.Companion.list1
import com.joinhub.complaintprotaluser.fragments.BillingHomeFragment.Companion.binding
import com.joinhub.complaintprotaluser.huaweiIAPLab.*
import com.joinhub.complaintprotaluser.interfaces.BillingHistory
import com.joinhub.complaintprotaluser.interfaces.PackageUpgradeInterface
import com.joinhub.complaintprotaluser.models.BillingModel
import com.joinhub.complaintprotaluser.presentator.BillingPresentator
import com.joinhub.complaintprotaluser.presentator.PackageUpgradePresentatorval
import com.joinhub.complaintprotaluser.utilties.Constants
import com.joinhub.complaintprotaluser.utilties.Constants.Companion.getDateOnly
import org.json.JSONException


class BillingHomeFragment : Fragment() , SubscriptionContract.View, PackageUpgradeInterface ,
    BillingHistory<BillingModel> {
   companion object{ @SuppressLint("StaticFieldLeak")
   lateinit var binding:FragmentBillingHomeBinding
   lateinit var list1: MutableList<BillingModel>}

    lateinit var preference: Preference
    private val TAG = "ConsumptionActivity"
    private var mClient: IapClient? = null
    lateinit var methodType:String

    lateinit var presenterS: SubscriptionPresenter
    var isHuawei: Boolean= false
    @SuppressLint("SetTextI18n")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentBillingHomeBinding.inflate(layoutInflater,container,false)
        preference= Preference(requireContext())
        mClient = Iap.getIapClient(requireActivity())
        initt()
        list1= mutableListOf()
        if(list1.isEmpty()){
            val presentator= BillingPresentator<Any>(this, requireActivity())
            presentator.loadHistory(Preference(requireContext()).getIntpreference("userID"))
        }else{
            if(list1.size>0) {
                val model = list1[list1.lastIndex]
                if(model.month== Constants.getMonth()){
                    binding.btnPayBills.isEnabled = false
                    binding.txtDueDate.text = model.billingDate
                    binding.txtStatus.text = model.status
                }else{
                    if(Constants.getMonth()>model.month){
                        binding.txtStatus.text = "unPaid"
                        binding.txtDueDate.text =
                            "15/" + Constants.getMonth() + "/" + Constants.getYear()

                    }

                }
            }else{
                preference.setStringpreference("status", "UnPaid")
                binding.txtStatus.text = "UnPaid"
                binding.txtDueDate.text =
                    "15/" + Constants.getMonth() + "/" + Constants.getYear()
            }

        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initt() {
        presenterS= SubscriptionPresenter(this)
        binding.easyPasiaCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.HuaweiCheckBox.isChecked= false
                binding.JazzCheckBox.isChecked= false
                isHuawei= false
                methodType= "EasyPaisa"
            }
        }
        binding.JazzCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.HuaweiCheckBox.isChecked= false
                binding.easyPasiaCheckBox.isChecked= false
                isHuawei= false
                methodType= "JazzCash"
            }
        }
        binding.HuaweiCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                methodType="HuaweiIAP"
                isHuawei= true
                binding.easyPasiaCheckBox.isChecked= false
                binding.JazzCheckBox.isChecked= false
            }
        }
        binding.txtCharges.text="Rs:"+preference.getStringpreference("pkgRate",null)


        binding.btnPayBills.setOnClickListener {
            if(isHuawei){

               // buy("pp5")
               // Toast.makeText(requireContext(),preference.getIntpreference("pkgID").toString(),Toast.LENGTH_LONG).show()
                presenterS.buy("pp0", resultLauncher1)
            }else{

                val i= Intent(requireContext(), PaymentActivity::class.java)
                i.putExtra("type",methodType)
                i.putExtra("charges",preference.getStringpreference("pkgRate",null))
               resultLauncher.launch(i)
            }
        }
    }

    override fun showProducts(productInfoList: MutableList<ProductInfo>?) {

    }

    override fun updateProductStatus(ownedPurchasesResult: OwnedPurchasesResult?) {

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == com.joinhub.complaintprotaluser.huaweiIAPLab.Constants.REQ_CODE_BUY) {
            if (resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = data
                if (resultCode == Activity.RESULT_OK) {
                    val purchaseResult = SubscriptionUtils.getPurchaseResult(requireActivity(), data)
                    if (OrderStatusCode.ORDER_STATE_SUCCESS == purchaseResult) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        }
                        updatePackage()

                    }
                    if (OrderStatusCode.ORDER_STATE_CANCEL == purchaseResult) {
                        Toast.makeText(requireContext(), "cancelled", Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                } else {
                    //  Log.i(TAG, "cancel subscribe")
                    Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
               val ResponseCode: String = data!!.getStringExtra("pp_ResponseCode").toString()
                println("DateFn: ResponseCode:$ResponseCode")
                if (ResponseCode == "000") {

                    Toast.makeText(
                        ApplicationProvider.getApplicationContext(),
                        "Payment Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    updatePackage()
                }else{
                    Toast.makeText(requireContext(),"Failed",Toast.LENGTH_LONG).show()
                }
            }else{

            Toast.makeText(requireContext(),"Cancelled",Toast.LENGTH_LONG).show()
        }

    }

    private var resultLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (result.resultCode == Activity.RESULT_OK) {
                val purchaseResult = SubscriptionUtils.getPurchaseResult(requireActivity(), data)
                if (OrderStatusCode.ORDER_STATE_SUCCESS == purchaseResult) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    }
                    updatePackage()
                    Toast.makeText(context,"Yes",Toast.LENGTH_LONG).show()
                }else
                if (OrderStatusCode.ORDER_STATE_CANCEL == purchaseResult) {
                    Toast.makeText(requireContext(), "cancelled", Toast.LENGTH_SHORT).show()
                }else if(OrderStatusCode.ORDER_STATE_FAILED == purchaseResult) {


                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
                }
                else {
                //  Log.i(TAG, "cancel subscribe")
                Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun updatePackage() {
        val preference= Preference(requireContext())

            val presentator= PackageUpgradePresentatorval(this@BillingHomeFragment, requireActivity())
            presentator.upgradePackage(preference.getIntpreference("userID"),
               preference.getIntpreference("pkgID"),
               methodType,preference.getStringpreference("pkgRate",null),
                false, preference.getStringpreference("pkgName",null))

    }

    override fun onStarts() {

    }

    override fun onSuccess(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }

    override fun onError(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(list: MutableList<BillingModel>) {
        list1.addAll(list)
        if(list1.size>0) {
            val model = list1[list1.lastIndex]
            if(model.month== Constants.getMonth()){
                binding.btnPayBills.isEnabled = false
                binding.txtDueDate.text = model.billingDate
                binding.txtStatus.text = model.status
            }else{
                if(Constants.getMonth()>model.month){
                    binding.txtStatus.text = "unPaid"
                    binding.txtDueDate.text =
                        "15/" + Constants.getMonth() + "/" + Constants.getYear()

                }

            }
        }else{
            preference.setStringpreference("status", "UnPaid")
            binding.txtStatus.text = "UnPaid"
            binding.txtDueDate.text =
                "15/" + Constants.getMonth() + "/" + Constants.getYear()
        }
    }
}