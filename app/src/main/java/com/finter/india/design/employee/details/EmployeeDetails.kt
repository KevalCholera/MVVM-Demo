package com.finter.india.design.employee.details

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finter.india.R
import com.finter.india.base.DataBindingActivity
import com.finter.india.databinding.ActivityEmployeeDetailsBinding
import com.finter.india.design.employee.add.EmployeeAdd
import com.finter.india.design.employee.details.adapter.EmployeeDetailsDocumentAdapter
import com.finter.india.design.employee.details.popup.EmployeeCreateAccountPopUp
import com.finter.india.design.employee.interf.EmployeeCreateAccountInterface
import com.finter.india.design.party.add.adapter.PartyAddDocumentData
import com.finter.india.design.employee.interf.PartyDetailsAdapterInterface
import com.finter.india.model.Status
import com.finter.india.utils.*
import com.finter.india.utils.interf.NoInternetInterface
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EmployeeDetails : DataBindingActivity(), NoInternetInterface,
    PartyDetailsAdapterInterface, EmployeeCreateAccountInterface {
    private val binding: ActivityEmployeeDetailsBinding by binding(R.layout.activity_employee_details)

    @VisibleForTesting
    val _employeeDetailsViewModel by viewModels<EmployeeDetailsViewModel>()
    var updateCustomerList = false
    var isUsed = false
    lateinit var supplierAdapter: EmployeeDetailsDocumentAdapter
    private var apiCall = -1
    private var isActive = false
    private var isUserCreated = false
    private var emp_user_id = ""

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@EmployeeDetails
            employeeDetailsViewModel = _employeeDetailsViewModel.apply {
                getId(
                    intent.extras?.getString(Constants.All_ID).toString()
                )
            }
        }

        supplierAdapter = EmployeeDetailsDocumentAdapter(this)

        binding.rcEmployeeDetailsAttachment.layoutManager = LinearLayoutManager(this)
        binding.rcEmployeeDetailsAttachment.adapter = supplierAdapter

        _employeeDetailsViewModel.employeeDetailsResponse.observe(this, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    closeKeyboard()
                    progressBarTouchable(true)
                    isUsed = it.response?.data?.is_used.equals("1")

                    isActive = if (it.response?.data?.is_active != null)
                        it.response.data.is_active.toString().toInt() == 1
                    else
                        false
                    isUserCreated = it.response?.data?.emp_user_id.toString().toInt() != 0
                    emp_user_id = it.response?.data?.emp_user_id.toString()

                    supplierAdapter.emptyDocList()
                    if (it.response?.data?.attachment != null) {
                        for (element in it.response.data.attachment) {
                            setInView(element)
                        }
                        binding.tvEmployeeDetailsAttachmentList.visibility = View.GONE
                        binding.cvEmployeeDetailsDocument.visibility = View.VISIBLE
                    } else {
                        binding.tvEmployeeDetailsAttachmentList.visibility = View.VISIBLE
                        binding.cvEmployeeDetailsDocument.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    closeKeyboard()
                    progressBarTouchable(false)
                }
                Status.ERROR -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                    apiCall = 0
                    noInternetPopup(this, false)
                }
                Status.FAIL -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.EMPTY -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.UNAUTHORISED -> {
                    progressBarTouchable(true)
                    
                }
                null -> TODO()
                else -> TODO()
            }
        })
        _employeeDetailsViewModel.employeeDeleteResponse.observe(this, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    closeKeyboard()
                    progressBarTouchable(true)
                    successTone()
                    updateCustomerList = true
                    Toast.makeText(this, it.response?.message.toString(), Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                Status.LOADING -> {
                    closeKeyboard()
                    progressBarTouchable(false)
                }
                Status.ERROR -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                    apiCall = 1
                    noInternetPopup(this, true)
                }
                Status.FAIL -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.EMPTY -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.UNAUTHORISED -> {
                    progressBarTouchable(true)
                    
                }
                null -> TODO()
                else -> TODO()
            }
        })
        _employeeDetailsViewModel.employeeCreateResponse.observe(this, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    closeKeyboard()
                    progressBarTouchable(true)
                    successTone()
                    updateCustomerList = true
                    isUserCreated = true
                    isActive = true
                    Toast.makeText(this, it.response?.message.toString(), Toast.LENGTH_SHORT).show()
                    onBackPressed()

                }
                Status.LOADING -> {
                    closeKeyboard()
                    progressBarTouchable(false)
                }
                Status.ERROR -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                    apiCall = 2
                    noInternetPopup(this, true)
                }
                Status.FAIL -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.EMPTY -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.UNAUTHORISED -> {
                    progressBarTouchable(true)
                    
                }
                null -> TODO()
                else -> TODO()
            }
        })
        _employeeDetailsViewModel.employeeActiveInActiveResponse.observe(this, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    closeKeyboard()
                    progressBarTouchable(true)
                    successTone()

                    updateCustomerList = true
                    isUserCreated = false
                    isActive = !isActive

                    Toast.makeText(this, it.response?.message.toString(), Toast.LENGTH_SHORT).show()
                    onBackPressed()

                }
                Status.LOADING -> {
                    closeKeyboard()
                    progressBarTouchable(false)
                }
                Status.ERROR -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                    apiCall = 3
                    noInternetPopup(this, true)
                }
                Status.FAIL -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.EMPTY -> {
                    snackBar(binding.coordinatorLayout, it.error)
                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.UNAUTHORISED -> {
                    progressBarTouchable(true)
                    
                }
                null -> TODO()
                else -> TODO()
            }
        })

        _employeeDetailsViewModel.salesPersonDetails()
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    fun popUpMenu(view: View) {
        showPopup()
    }

    private fun setInView(customerAddDocumentData: PartyAddDocumentData) {

        val uri = customerAddDocumentData.attachment
        val extension = uri?.substring(uri.lastIndexOf("."))

        val cuDa: MutableList<PartyAddDocumentData> = mutableListOf()
        val cd = PartyAddDocumentData(
            customerAddDocumentData.id,
            customerAddDocumentData.attachment_name,
            customerAddDocumentData.attachment,
            extension?.replace(".", ""),
            false
        )
        cuDa.add(cd)
        supplierAdapter.updateDocList(cuDa)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                updateCustomerList = true
                _employeeDetailsViewModel.salesPersonDetails()
            }
        } catch (ex: Exception) {
            Toast.makeText(
                this, ex.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showPopup() {
        val popup: PopupMenu? = PopupMenu(this, binding.ivSalesPersonDetailsEdit)
        popup?.inflate(R.menu.menu_customer_details)

        val menu_customer_details_create_user =
            popup?.menu?.findItem(R.id.menu_customer_details_create_user)

        val menu_customer_details_active_inactive =
            popup?.menu?.findItem(R.id.menu_customer_details_active_inactive)

        if (isUserCreated) {
            menu_customer_details_create_user?.isVisible = false
            menu_customer_details_active_inactive?.isVisible = true

            if (isActive)
                menu_customer_details_active_inactive?.title = "In-Active"
            else
                menu_customer_details_active_inactive?.title = "Active"
        } else {
            menu_customer_details_create_user?.isVisible = true
            menu_customer_details_active_inactive?.isVisible = false
        }

        popup?.setOnMenuItemClickListener { item: MenuItem? ->

            when (item?.itemId) {
                R.id.menu_customer_details_update -> {
                    sharedPreferenceUtil.putValue(
                        Constants.ACTIVITY_CUSTOMER,
                        Constants.UPDATE_CUSTOMER
                    )

                    val intent = Intent(this, EmployeeAdd::class.java).putExtra(
                        Constants.All_ID,
                        intent.extras?.getString(Constants.All_ID).toString()
                    )
                    startActivityForResult(intent, 101)
                }
                R.id.menu_customer_details_delete -> {
                    if (!isUsed)
                        errorPopUp()
                    else
                        snackBar(
                            binding.coordinatorLayout,
                            "You won't delete this \n You have Data!"
                        )
                }
                R.id.menu_customer_details_create_user -> {
                    createEmployeePopUp()
                }
                R.id.menu_customer_details_active_inactive -> {
                    activeInActiveEmployeePopUp()
                }
            }
            true
        }
        popup?.show()
    }

    private fun errorPopUp() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Delete!")

        builder.setMessage("Are you sure you want to delete Employee?")
        builder.setCancelable(false)
        builder.setPositiveButton("Delete") { _, _ ->
            _employeeDetailsViewModel.salesPersonDelete()
        }
        builder.setNegativeButton("Cancel") { _, _ ->

        }

        builder.show()
    }

    private fun createEmployeePopUp() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Create Employee Account!")

        builder.setMessage("Are you sure you want to Create Employee Account?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            val popUpClass = EmployeeCreateAccountPopUp()
            popUpClass.showPopupWindow(
                this,
                this,
                intent.extras?.getString(Constants.All_ID).toString()
            )
        }
        builder.setNegativeButton("No") { _, _ ->

        }

        builder.show()
    }

    private fun activeInActiveEmployeePopUp() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        if (!isActive) {
            builder.setTitle("Active Employee Account!")
            builder.setMessage("Are you sure you want to Activate Employee Account?")
            builder.setCancelable(false)

            builder.setPositiveButton("Activate") { _, _ ->
                _employeeDetailsViewModel.salesPersonActiveInActive(emp_user_id, "1")
            }

        } else {
            builder.setTitle("In-Active Employee Account!")
            builder.setMessage("Are you sure you want to In-Activate Employee Account?")

            builder.setPositiveButton("In-Activate") { _, _ ->
                _employeeDetailsViewModel.salesPersonActiveInActive(emp_user_id, "0")
            }
        }

        builder.setNegativeButton("No") { _, _ ->

        }

        builder.show()
    }

    override fun onBackPressed() {
        progressBarTouchable(true)
        super.onBackPressed()
        closeKeyboard()

        if (!updateCustomerList) {
            sharedPreferenceUtil.putValue(Constants.UPDATE_CUSTOMER_BOOLEAN, false)

        } else {
            sharedPreferenceUtil.putValue(Constants.UPDATE_CUSTOMER_BOOLEAN, true)

        }
        finish()
    }

    override fun noInternetRefreshClick() {
        when (apiCall) {
            0 -> {
                _employeeDetailsViewModel.salesPersonDetails()
            }
            1 -> {
                errorPopUp()
            }
            2 -> {
                createEmployeePopUp()
            }
            3 -> {
                activeInActiveEmployeePopUp()
            }
        }
    }

    override fun noInternetBackClick() {

    }


    private fun isStoragePermissionGranted(customerAddDocumentData: PartyAddDocumentData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                downloadFile(
                    this,
                    customerAddDocumentData.attachment.toString(),
                    customerAddDocumentData.attachment_name.toString(),
                    "Employee",
                    sharedPreferenceUtil.getString(Constants.BUSINESS_NAME, ""),
                    false
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.REQUEST_CODE_WRITE_STORAGE_PERMISION
                )

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            downloadFile(
                this,
                customerAddDocumentData.attachment.toString(),
                customerAddDocumentData.attachment_name.toString(),
                "Employee",
                sharedPreferenceUtil.getString(Constants.BUSINESS_NAME, ""),
                false
            )

        }
    }

    private fun permissionForDownload(customerAddDocumentData: PartyAddDocumentData) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            if (checkPermissionForReadWrite(this)) {
                downloadFile(
                    this,
                    customerAddDocumentData.attachment.toString(),
                    customerAddDocumentData.attachment_name.toString(),
                    "Employee",
                    sharedPreferenceUtil.getString(Constants.BUSINESS_NAME, ""),
                    false
                )
            } else {
                requestPermissionForReadWrite(this)
            }

        } else {
            downloadFile(
                this,
                customerAddDocumentData.attachment.toString(),
                customerAddDocumentData.attachment_name.toString(),
                "Employee",
                sharedPreferenceUtil.getString(Constants.BUSINESS_NAME, ""),
                false
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_WRITE_STORAGE_PERMISION)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
            }
    }

    override fun onClickAttachment(customerAddDocumentData: PartyAddDocumentData) {
//        isStoragePermissionGranted(customerAddDocumentData)
        permissionForDownload(customerAddDocumentData)
    }

    override fun onClickView(customerAddDocumentData: PartyAddDocumentData) {

        val getName: Array<String> =
            Arrays.toString(getAppPath().list()).split(",".toRegex()).toTypedArray()

        for (i in getName.indices)
            if (customerAddDocumentData.attachment_name.toString() == getName[i]) {
                val file = File(getAppPath().toString() + customerAddDocumentData.attachment_name)
                val target = Intent(Intent.ACTION_VIEW)
                target.setDataAndType(Uri.fromFile(file), "application/pdf")
                target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

                val intent = Intent.createChooser(target, "Open File")
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {

                }
            }
    }

    override fun onCheckStoragePermission(): Boolean {
        return true
    }

    override fun onClickSubmit(id: String, password: String) {
        _employeeDetailsViewModel.salesPersonCreate(password)
    }
}