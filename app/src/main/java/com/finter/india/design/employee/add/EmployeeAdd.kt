package com.finter.india.design.employee.add

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finter.india.R
import com.finter.india.base.DataBindingActivity
import com.finter.india.core.observeWith
import com.finter.india.databinding.ActivityEmployeeAddBinding
import com.finter.india.design.adapter.basicinformation.statelist.BasicInformationAdapterDataNew
import com.finter.india.design.adapter.basicinformation.statelist.BasicInformationAdapterNew
import com.finter.india.design.employee.add.adapter.EmployeeAddDocumentAdapter
import com.finter.india.design.employee.interf.EmployeeAdapterInterface
import com.finter.india.design.employee.list.data.EmployeeData
import com.finter.india.design.party.add.adapter.PartyAddDocumentData
import com.finter.india.model.Status
import com.finter.india.utils.*
import com.finter.india.utils.cameragallerypopup.CameraGalleryInterface
import com.finter.india.utils.cameragallerypopup.Compressor
import com.finter.india.utils.interf.NoInternetInterface
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EmployeeAdd : DataBindingActivity(), NoInternetInterface, EmployeeAdapterInterface,
    CameraGalleryInterface {
    private val binding: ActivityEmployeeAddBinding by binding(R.layout.activity_employee_add)

    @VisibleForTesting
    val _employeeAddViewModel by viewModels<EmployeeAddViewModel>()

    private var arrayStateName: ArrayList<String> = ArrayList()
    private var arrayStateCode: ArrayList<String> = ArrayList()
    private var arrayStateTinCode: ArrayList<String> = ArrayList()
    private var arrayStatesID: ArrayList<String> = ArrayList()

    private var arrayStateNameFilter: ArrayList<String> = ArrayList()
    private var arrayStateCodeFilter: ArrayList<String> = ArrayList()
    private var arrayStateTinCodeFilter: ArrayList<String> = ArrayList()
    private var arrayStatesIDFilter: ArrayList<String> = ArrayList()

    var mList: ArrayList<Uri> = arrayListOf()
    var mListLocal: ArrayList<Uri> = arrayListOf()
    var mListUploaded: ArrayList<String> = arrayListOf()

    var textViewBoolean = false
    lateinit var customerAdapter: EmployeeAddDocumentAdapter
    private var apiCall = -1
    var onDeleteObject = JSONObject()
    private val FILE_PICKER_REQUEST_CODE = 1
    private val PERMISSIONS_REQUEST_CODE = 0
    private var mTempPhotoPath = ""

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@EmployeeAdd
            vieModel = _employeeAddViewModel.apply {
                getContext(intent.extras?.getString(Constants.All_ID).toString())
            }
        }
        if (sharedPreferenceUtil.getString(
                Constants.ACTIVITY_CUSTOMER,
                ""
            ) == Constants.Add_CUSTOMER
        ) {
            binding.tvSalesPersonAddHeading.text = resources.getString(R.string.add_employee)
            _employeeAddViewModel.setCountry(resources.getString(R.string.india))
            _employeeAddViewModel.setCountryCode("+91")
        } else {
            _employeeAddViewModel.fillSalesPerson()
            binding.tvSalesPersonAddHeading.text = resources.getString(R.string.update_employee)
        }

        customerAdapter =
            EmployeeAddDocumentAdapter(
                _employeeAddViewModel,
                mList,
                mListUploaded,
                mListLocal,
                this@EmployeeAdd,
                intent.extras!!.getString(Constants.All_ID).toString(),
                this
            )
        binding.activityResultRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.activityResultRecyclerView.adapter = customerAdapter

        _employeeAddViewModel.salesPersonAddResponse.observeWith(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    closeKeyboard()

                    Toast.makeText(this, it.response?.message.toString(), Toast.LENGTH_SHORT).show()
                    successTone()
                    progressBarTouchable(true)
                    val returnBackIntent = Intent()
                    returnBackIntent.putExtra("101", true)
                    setResult(Activity.RESULT_OK, returnBackIntent)
                    finish()

                }
                Status.LOADING -> {
                    progressBarTouchable(false)
                    closeKeyboard()
                }
                Status.ERROR -> {
                    progressBarTouchable(true)
                    closeKeyboard()
                    snackBar(binding.coordinatorLayout, it.error)
                    apiCall = 0
                    noInternetPopup(this, false)
                }
                Status.FAIL -> {
                    progressBarTouchable(true)
                    closeKeyboard()
                    snackBar(binding.coordinatorLayout, it.error)
                }
                Status.EMPTY -> {
                    progressBarTouchable(true)
                    closeKeyboard()
                    snackBar(binding.coordinatorLayout, it.error)
                }
                Status.UNAUTHORISED -> {
                    progressBarTouchable(true)

                }
            }
        }

        _employeeAddViewModel.salesPersonUpdateDetailsResponse.observeWith(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBarTouchable(true)

                    closeKeyboard()
                    _employeeAddViewModel.setData(it.response?.data!!)

                    if (it.response.data.attachment != null) {

                        for (element in it.response.data.attachment) {
                            setInView(element)
                        }
                    }
                }
                Status.LOADING -> {
                    progressBarTouchable(false)
                    closeKeyboard()
                }

                Status.ERROR -> {
                    progressBarTouchable(true)
                    closeKeyboard()
                    snackBar(binding.coordinatorLayout, it.error)
                    apiCall = 1
                    noInternetPopup(this, false)
                }
                Status.FAIL -> {
                    progressBarTouchable(true)
                    snackBar(binding.coordinatorLayout, it.error)
                    closeKeyboard()
                }
                Status.EMPTY -> {
                    progressBarTouchable(true)
                    snackBar(binding.coordinatorLayout, it.error)
                    closeKeyboard()
                }
                Status.UNAUTHORISED -> {
                    progressBarTouchable(true)
                }
            }
        }
        _employeeAddViewModel.stateResponse.observeWith(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    if (it.response?.data != null) {
                        val stateArray = JSONArray()

                        for (i in it.response.data.indices) {
                            val stateObject = JSONObject()
                            stateObject.put("name", it.response.data[i].name)
                            stateObject.put("state_code", it.response.data[i].state_code)
                            stateObject.put("tin_code", it.response.data[i].tin_code)
                            stateObject.put("id", it.response.data[i].id)
                            stateArray.put(stateObject)
                        }
                        sharedPreferenceUtil.putValue(Constants.STATE_LIST, stateArray.toString())

                        stateData(stateArray)
                    }

                    progressBarTouchable(true)
                    closeKeyboard()
                }
                Status.LOADING -> {
                    arrayStateName = ArrayList()
                    arrayStateCode = ArrayList()
                    arrayStateTinCode = ArrayList()
                    arrayStatesID = ArrayList()
                    progressBarTouchable(false)
                    closeKeyboard()
                }

                Status.ERROR -> {
                    arrayStateName = ArrayList()
                    arrayStateCode = ArrayList()
                    arrayStateTinCode = ArrayList()
                    arrayStatesID = ArrayList()
                    progressBarTouchable(true)
                    closeKeyboard()
                    apiCall = 2
                    noInternetPopup(this, false)
                    snackBar(binding.coordinatorLayout, it.error)
                }
                Status.FAIL -> {
                    arrayStateName = ArrayList()
                    arrayStateCode = ArrayList()
                    arrayStateTinCode = ArrayList()
                    arrayStatesID = ArrayList()
                    progressBarTouchable(true)
                    snackBar(binding.coordinatorLayout, it.error)
                    closeKeyboard()
                }
                Status.EMPTY -> {
                    arrayStateName = ArrayList()
                    arrayStateCode = ArrayList()
                    arrayStateTinCode = ArrayList()
                    arrayStatesID = ArrayList()
                    progressBarTouchable(true)
                    snackBar(binding.coordinatorLayout, it.error)
                    closeKeyboard()
                }
                Status.UNAUTHORISED -> {
                    progressBarTouchable(true)
                }
            }
        }

        _employeeAddViewModel.deleteAttachmentResponse.observe(
            this,
            androidx.lifecycle.Observer {
                when (it?.status) {
                    Status.SUCCESS -> {
                        closeKeyboard()

                        progressBarTouchable(true)

                        val mListLocalData: ArrayList<Uri> = ArrayList()

                        for (i in 0 until mListLocal.size)
                            mListLocalData.add(mListLocal[i])

                        customerAdapter.emptyDocList()

                        if (it.response?.data?.attachment != null && it.response.data.attachment.isNotEmpty())
                            for (element in it.response.data.attachment) {
                                setInView(element)
                            }

                        if (mListLocalData.size > 0)
                            for (i in 0 until mListLocalData.size)
                                setInView(mListLocalData[i])
                    }
                    Status.LOADING -> {
                        closeKeyboard()
                        progressBarTouchable(false)
                    }
                    Status.ERROR -> {
                        closeKeyboard()
                        progressBarTouchable(true)
                        snackBar(binding.coordinatorLayout, it.error)
                        apiCall = 3
                        noInternetPopup(this, true)
                    }
                    Status.FAIL -> {
                        closeKeyboard()
                        progressBarTouchable(true)
                        snackBar(binding.coordinatorLayout, it.error)
                    }
                    Status.EMPTY -> {
                        closeKeyboard()
                        progressBarTouchable(true)
                        snackBar(binding.coordinatorLayout, it.error)
                    }
                    Status.UNAUTHORISED -> {
                        progressBarTouchable(true)
                    }
                    null -> TODO()
                }
            })
    }

    private fun openOccasionsPopupOptionsForState(textView: TextView) {
        try {
            val alertDialogs: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this)
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialog: View = inflater.inflate(R.layout.element_popup, null)

            val rcElementPopUp = dialog.findViewById(R.id.rcElementPopUp) as RecyclerView
            val etElementPopUp = dialog.findViewById(R.id.etElementPopUp) as EditText
            var alert: android.app.AlertDialog? = null

            etElementPopUp.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    arrayStateNameFilter = ArrayList()
                    arrayStateCodeFilter = ArrayList()
                    arrayStateTinCodeFilter = ArrayList()
                    arrayStatesIDFilter = ArrayList()

                    if (s != "" && s.isNotEmpty()) {
                        for (i in 0 until arrayStateName.size)
                            if (arrayStateName[i].toUpperCase(java.util.Locale.ROOT)
                                    .contains(s.toString().toUpperCase(java.util.Locale.ROOT)) ||
                                arrayStateCode[i].toUpperCase(java.util.Locale.ROOT).contains(s.toString().toUpperCase(java.util.Locale.ROOT))
                            ) {
                                arrayStateNameFilter.add(arrayStateName[i])
                                arrayStateCodeFilter.add(arrayStateCode[i])
                                arrayStateTinCodeFilter.add(arrayStateTinCode.get(i))
                                arrayStatesIDFilter.add(arrayStatesID[i])
                            }

                        generateData(
                            textView,
                            alert!!,
                            arrayStateNameFilter,
                            arrayStateCodeFilter,
                            arrayStateTinCodeFilter,
                            arrayStatesIDFilter,
                            rcElementPopUp
                        )
                    } else
                        generateData(
                            textView,
                            alert!!,
                            arrayStateName,
                            arrayStateCode,
                            arrayStateTinCode,
                            arrayStatesID,
                            rcElementPopUp
                        )
                }

                override fun afterTextChanged(s: Editable) {}
            })

            alertDialogs.setView(dialog)
            alert = alertDialogs.create()
            alert.show()

            generateData(
                textView,
                alert!!,
                arrayStateName,
                arrayStateCode,
                arrayStateTinCode,
                arrayStatesID,
                rcElementPopUp
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateData(
        textView: TextView,
        alert: AlertDialog,
        arrayStateName: ArrayList<String>,
        arrayStateCode: ArrayList<String>,
        arrayStateTinCode: ArrayList<String>,
        arrayStateID: ArrayList<String>,
        rcElementPopUp: RecyclerView
    ) {
        val result = ArrayList<BasicInformationAdapterDataNew>()

        for (j in 0 until arrayStateName.size) {
            result.add(
                BasicInformationAdapterDataNew(
                    arrayStateName[j],
                    arrayStateCode[j],
                    arrayStateTinCode[j],
                    arrayStateID[j]
                )
            )
        }

        val adapter =
            BasicInformationAdapterNew(
                _employeeAddViewModel.stateTag,
                alert,
                textView,
                result,
                _employeeAddViewModel.stateCode,
                true
            )
        val layoutManager = LinearLayoutManager(this)

        rcElementPopUp.layoutManager = layoutManager
        rcElementPopUp.itemAnimator = DefaultItemAnimator()
        rcElementPopUp.adapter = adapter
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    fun stateListClick(view: View) {
        closeKeyboard()
        if (sharedPreferenceUtil.getString(Constants.STATE_LIST, "") == "")
            _employeeAddViewModel.getStateList()
        else {
            stateData(JSONArray(sharedPreferenceUtil.getString(Constants.STATE_LIST, "")))
        }
    }

    private fun stateData(data: JSONArray) {
        arrayStateName = ArrayList()
        arrayStateCode = ArrayList()
        arrayStateTinCode = ArrayList()
        arrayStatesID = ArrayList()

        for (i in 0 until data.length()) {
            val stateObject = data.optJSONObject(i)
            arrayStateName.add(stateObject.optString("name"))
            arrayStateCode.add(stateObject.optString("state_code"))
            arrayStateTinCode.add(stateObject.optString("tin_code"))
            arrayStatesID.add(stateObject.optString("id"))
        }
        openOccasionsPopupOptionsForState(binding.tvSalesPersonAddState)
    }

    override fun onBackPressed() {
        progressBarTouchable(true)
        super.onBackPressed()
        closeKeyboard()
        sharedPreferenceUtil.putValue(Constants.ACTIVITY_CUSTOMER, "")
        finish()
    }

    fun docPicker(view: View) {

    }

    private fun setInView(uri: Uri) {
        Log.i("uri", uri.toString())

        val file = File(uri.path.toString())

        val cuDa: MutableList<PartyAddDocumentData> = mutableListOf()
        val cd = PartyAddDocumentData(
            "#10",
            file.name.toString(),
            file.name.toString(),
            file.extension.replace(".", ""),
            true
        )
        cuDa.add(cd)
        mList.add(uri)
        mListLocal.add(uri)
        customerAdapter.updateDocList(cuDa)
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
        mList.add(uri!!.toUri())
        mListUploaded.add(uri)
        customerAdapter.updateDocList(cuDa)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            if (requestCode == Constants.OPEN_FILE_REQUEST_CODE) {

                val documentUri = data?.data!!

                contentResolver.takePersistableUriPermission(
                    documentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val fileSize = getSize(this, documentUri)

                val fileSizeInKB = fileSize.toFloat() / 1024
                val fileSizeInMB = fileSizeInKB / 1024

                if (fileSizeInMB > 2) {
                    Toast.makeText(
                        this,
                        "File Should be less then 2 MB",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    setInView(Uri.fromFile(fileFromContentUri(this,documentUri)))
                }
            } else if (requestCode == Constants.CHOOSE_FILE) {
                if (data != null) {
                    var inputStream: InputStream? = null
                    try {

                        inputStream = contentResolver.openInputStream(data.data!!)
                        val exifInterface = ExifInterface(inputStream!!)

                        mTempPhotoPath = fileFromContentUri(this, data.data!!).absolutePath

                        val imgFile = File(Uri.parse(mTempPhotoPath).path!!)
                        val compressFile: File =
                            Compressor.getDefault(this)!!.compressToFile(imgFile)

                        Log.i("Data", mTempPhotoPath + "-->" + compressFile.length())

                        setInView(Uri.fromFile(compressFile))

                    } catch (e: IOException) {
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close()
                            } catch (ignored: IOException) {
                            }

                        }
                    }
                } else {
                    Log.i("Data Empty", data?.data.toString())
                }
            } else if (requestCode == Constants.CAMERA) {

                val imgFile = File(Uri.parse(mTempPhotoPath).path.toString())
                val compressFile: File =
                    Compressor.getDefault(this)!!.compressToFile(imgFile)

                Log.i("Data", mTempPhotoPath + "-->" + compressFile.length())

                setInView(Uri.fromFile(compressFile))

            }
    }

    fun onClickEmployee(view: View) {

        if (mListLocal.size > 0) {
            val numbers: MutableList<FileWithType> = mutableListOf()

            for (i in 0 until mListLocal.size) {
                val file = File(mListLocal[i].path!!)
                numbers.add(FileWithType(file, file.extension))
            }
            _employeeAddViewModel.onAddCreateSalesPersonClickWithAttachment(
                createRequestBody(
                    "attachment",
                    numbers
                )
            )
        } else
            _employeeAddViewModel.onAddCreateSalesPersonClick()
    }

    private fun createRequestBody(
        name: String,
        arrayfile: MutableList<FileWithType>?
    ): Array<MultipartBody.Part?> {
        val mutableList = mutableListOf<MultipartBody.Part>()
        arrayfile?.let {
            for (i in 0 until arrayfile.size) {
                val file1 = arrayfile[i]
                val name1 = String.format(Locale.ENGLISH, "%s[%d]", name, i)
                mutableList.add(createRequestBody(name1, file1.file, file1.type))
            }
        }

        return mutableList.toTypedArray()
    }

    private fun createRequestBody(name: String, file: File?, type: String?): MultipartBody.Part {
        var nameT: String? = null
        file?.name?.let {
            nameT = escapeUnicode(it)
        }
        val file1 = MultipartBody.Part.createFormData(
            name,
            nameT,
            file!!.asRequestBody()
        )
        return file1

    }

    private fun escapeUnicode(input: String): String {
        val b = StringBuilder(input.length)
        val f = Formatter(b)
        for (c in input.toCharArray()) {
            if (c.toInt() < 128) {
                b.append(c)
            } else {
                f.format("\\u%04x", c.toInt())
            }
        }
        return b.toString()
    }

    fun errorPopUp(
        activity: Activity,
        attachmentName: String,
        customerAddViewModel: EmployeeAddViewModel,
        attachmentId: String,
        customerId: String
    ) {

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle(attachmentName)

        builder.setMessage("Are you sure! you want to delete Attachment? By Clicking DELETE, attachment will delete! After that you don't have to save the form for delete...")
        builder.setCancelable(false)

        builder.setPositiveButton("Delete") { _, _ ->
            customerAddViewModel.deleteEmployeeAttachment(
                attachmentId,
                customerId
            )
        }
        builder.setNegativeButton("Cancel") { _, _ ->

        }

        builder.show()
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            //if you want to open PDF file
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
            //Adding Read URI permission
            flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivityForResult(intent, Constants.OPEN_FILE_REQUEST_CODE)
    }

    override fun noInternetRefreshClick() {
        when (apiCall) {
            0 -> {
                binding.ivEmployeeAddSave.performClick()
            }
            1 -> {
                _employeeAddViewModel.fillSalesPerson()
            }
            2 -> {
                binding.cvEmployeeAddState.performClick()
            }
            3 -> {
                onDeleteAttachment(
                    this,
                    onDeleteObject.optString("attachmentName"),
                    _employeeAddViewModel,
                    onDeleteObject.optString("attachmentId"),
                    onDeleteObject.optString("customerId")
                )
            }
        }
    }

    override fun noInternetBackClick() {

    }

    override fun onMethodCallback(
        sharedPreferenceUtil: SharedPreferenceUtil,
        context: Context,
        data: EmployeeData
    ) {

    }

    override fun onDeleteAttachment(
        activity: Activity,
        attachmentName: String,
        employeeAddViewModel: EmployeeAddViewModel,
        attachmentId: String,
        customerId: String
    ) {
        onDeleteObject = JSONObject()
        onDeleteObject.put("attachmentName", attachmentName)
        onDeleteObject.put("attachmentId", attachmentId)
        onDeleteObject.put("customerId", customerId)
        errorPopUp(
            activity,
            attachmentName,
            employeeAddViewModel,
            attachmentId,
            customerId
        )
    }

    override fun onCameraClickListener() {
        mTempPhotoPath = fetchCamera()
    }

    override fun onGalleryClickListener() {
        fetchMediaLocation()
    }
}