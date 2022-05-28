package com.joinhub.complaintprotaluser.bottomsheets

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.hms.iap.entity.OrderStatusCode
import com.huawei.hms.iap.entity.OwnedPurchasesResult
import com.huawei.hms.iap.entity.ProductInfo
import com.joinhub.alphavpn.utility.Preference
import com.joinhub.complaintprotaluser.activities.DashBoardActivity
import com.joinhub.complaintprotaluser.activities.PackageDetailsActivity
import com.joinhub.complaintprotaluser.activities.PaymentActivity
import com.joinhub.complaintprotaluser.databinding.FragmentPackageUpgradeBottomSheetBinding
import com.joinhub.complaintprotaluser.fragments.AllPackagesFragment
import com.joinhub.complaintprotaluser.fragments.UnlimitedPackagesFragment
import com.joinhub.complaintprotaluser.huaweiIAPLab.SubscriptionContract
import com.joinhub.complaintprotaluser.huaweiIAPLab.SubscriptionPresenter
import com.joinhub.complaintprotaluser.huaweiIAPLab.SubscriptionUtils
import com.joinhub.complaintprotaluser.interfaces.PackageUpgradeInterface
import com.joinhub.complaintprotaluser.models.PackageDetails
import com.joinhub.complaintprotaluser.presentator.PackageUpgradePresentatorval
import com.joinhub.complaintprotaluser.utilties.Constants


class PackageUpgradeBottomSheet(var model:PackageDetails=PackageDetails()) : BottomSheetDialogFragment() , PackageUpgradeInterface,
    SubscriptionContract.View{
    lateinit var binding: FragmentPackageUpgradeBottomSheetBinding
    companion object {
        const val TAG = "ActionBottomDialog"
    }
    lateinit var pkgId:String
    lateinit var price:String
    var presenter: SubscriptionPresenter? = null
    lateinit var preference: Preference
    var type: String? = null
    var methodType:String="EasyPaisa"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPackageUpgradeBottomSheetBinding.inflate(layoutInflater,container,false)
        init()
        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnEasyPaisa.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                methodType= "EasyPaisa"
            }
        }
        binding.btnJazzcash.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                methodType= "JazzCash"
            }
        }
        binding.btn2ChechOut.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                methodType= "2CheckOut"
            }
        }
        binding.btnHuaweiIAP.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                methodType= "huaweiIAP"

            }
        }
        binding.btnProceed.setOnClickListener {
            if(methodType == "huaweiIAP"){
                presenter!!.buy("pkg$pkgId", resultLauncher);
            }else{
                val intent= Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("type", methodType)
                intent.putExtra("charges",price)
                resultLauncher.launch(intent)
            }
        }
        return  binding.root
    }

    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (result.resultCode == RESULT_OK) {
                // Get String data from Intent
                val ResponseCode: String = data!!.getStringExtra("pp_ResponseCode").toString()
                println("DateFn: ResponseCode:$ResponseCode")
                if (ResponseCode == "000") {
                    Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT)
                        .show()
                    updatePackage()
                } else {
                    Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }else{
            Toast.makeText(requireContext(),"Payment Cancelled", Toast.LENGTH_LONG).show()
        }
    }

    private fun updatePackage() {
        val preference= Preference(requireContext())

        if(!PackageDetailsActivity.unLimit){
            preference.setIntpreference("pkgID", AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgID)
            preference.setStringpreference("pkgName",AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgName)
            preference.setStringpreference("pkgDesc",AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgDesc)
            preference.setStringpreference("pkgSpeed",AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgSpeed)
            preference.setStringpreference("pkgVolume",AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgVolume)
            preference.setStringpreference("pkgRate",AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate.toString())
            preference.setStringpreference("pkgBouns_Speed",AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgBouns_Speed)
            preference.setStringpreference("pkgBanner",AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgBanner.toString())
            val presentator= PackageUpgradePresentatorval(this@PackageUpgradeBottomSheet, requireActivity())
            presentator.upgradePackage(preference.getIntpreference("userID"),
                AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgID,
                type.toString(),AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate.toString(),true,
                AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgName)
        }else{
            preference.setIntpreference("pkgID", UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgID)
            preference.setStringpreference("pkgName",UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgName)
            preference.setStringpreference("pkgDesc",UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgDesc)
            preference.setStringpreference("pkgSpeed",UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgSpeed)
            preference.setStringpreference("pkgVolume",UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgVolume)
            preference.setStringpreference("pkgRate",UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate.toString())
            preference.setStringpreference("pkgBouns_Speed",UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgBouns_Speed)
            preference.setStringpreference("pkgBanner",UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgBanner.toString())

            val presentator= PackageUpgradePresentatorval(this@PackageUpgradeBottomSheet, requireActivity())
            presentator.upgradePackage(preference.getIntpreference("userID"),
                UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgID,
                type.toString(),UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate.toString(),true,
                UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgName)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        presenter= SubscriptionPresenter(this);
        preference= Preference(requireContext())
        binding.txtCName.text= preference.getStringpreference("userFullName", null)
        binding.txtPhone.text= preference.getStringpreference("userPhone", null)
        if(!PackageDetailsActivity.unLimit){
            price= AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate.toString()
            pkgId=AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgID.toString()
            binding.txtName.text= AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgName
            binding.txtspeedVolume.text="Speed: "+AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgSpeed+
                    "  Volume: "+ AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgVolume
            binding.txtTotal.text="Rs."+ AllPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate

        }else{
            price= UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate.toString()
            pkgId=UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgID.toString()
            binding.txtName.text= UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgName
            binding.txtspeedVolume.text="Speed: "+UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgSpeed+
                    "  Volume: "+ UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgVolume
            binding.txtTotal.text="Rs."+ UnlimitedPackagesFragment.listPack[PackageDetailsActivity.pos].pkgRate
        }
    }


    fun type(type: String?) {
        this@PackageUpgradeBottomSheet.type = type
    }

    fun newInstance(): PackageUpgradeBottomSheet {
        return PackageUpgradeBottomSheet()
    }

    override fun onStarts() {

    }

    override fun onSuccess(message: String) {
       Toast.makeText(requireContext(), message,Toast.LENGTH_LONG).show()
       startActivity(Intent(requireContext(),DashBoardActivity::class.java))
        requireActivity().finish()

    }

    override fun onError(e: String) {
        Toast.makeText(requireContext(),e,Toast.LENGTH_LONG).show()
    }

    override fun showProducts(productInfoList: MutableList<ProductInfo>?) {

    }

    override fun updateProductStatus(ownedPurchasesResult: OwnedPurchasesResult?) {

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == com.joinhub.complaintprotaluser.huaweiIAPLab.Constants.REQ_CODE_BUY) {
            if (resultCode == RESULT_OK) {
                val purchaseResult = SubscriptionUtils.getPurchaseResult(requireActivity(), data)
                if (OrderStatusCode.ORDER_STATE_SUCCESS == purchaseResult) {
                    Log.d("MainActivity", "onProductPurchased: ")
                    Log.d("UpdateFirebase", "true")
                    Log.e("Subscription", "Subscription Done Test")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    }
                    updatePackage()
                    presenter!!.refreshSubscription()
                    return
                }
                if (OrderStatusCode.ORDER_STATE_CANCEL == purchaseResult) {
                    Toast.makeText(requireContext(), "cancelled", Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            } else {
                //  Log.i(TAG, "cancel subscribe");
                Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
            }
        }
    }
}