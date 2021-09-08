package com.finter.india.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Point
import android.graphics.Typeface
import android.location.LocationManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.*
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.finter.india.BuildConfig
import com.finter.india.R
import com.finter.india.design.util.duplicatenumberdata.DuplicateNumberDataInterface
import com.finter.india.utils.interf.NoInternetInterface
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONObject
import java.io.*
import java.net.URL
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

//object CommonUtils {

fun Activity.progressBarTouchable(touchable: Boolean) {
    if (touchable)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    else
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
}

fun Activity.snackBar(
    coordinatorLayout: View,
    msg: String?
) {
//    val snack: Snackbar = Snackbar.make(coordinatorLayout, msg.toString(), Snackbar.LENGTH_LONG)
//    val view = snack.view
//    view.minimumHeight = 50
//    val params: CoordinatorLayout.LayoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
//    params.gravity = Gravity.TOP
//    view.layoutParams = params
//    snack.show()
    if (isInternetAvailable())
        Snackbar.make(coordinatorLayout, msg.toString(), Snackbar.LENGTH_LONG).show()
//    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.onFocusLayout(
    editText: View,
    linearLayout: View,
    view: View
) {
    val sdk: Int = Build.VERSION.SDK_INT

    linearLayout.setOnClickListener(View.OnClickListener {
        editText.requestFocus()
        showKeyboard(editText)
    })

    editText.setOnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                linearLayout.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.edittext_round_corner
                    )
                )
            } else {
                linearLayout.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.edittext_round_corner
                )
            }
            view.visibility = View.INVISIBLE
        } else {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                linearLayout.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.simple_edittext
                    )
                )
            } else {
                linearLayout.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.simple_edittext
                )
            }
            view.visibility = View.VISIBLE
        }
    }
}

fun imageResize(
    activity: Activity,
    imageView: View,
    width_send: Int,
    height_send: Int
) {
    val display = activity.windowManager.defaultDisplay
    val size = Point()
    try {
        display.getRealSize(size)
    } catch (err: NoSuchMethodError) {
        display.getSize(size)
    }
    val width: Int = size.x * width_send
    val height: Int = size.y / height_send

    val lp = LinearLayout.LayoutParams(width, height)
    lp.gravity = Gravity.CENTER
    imageView.layoutParams = lp
}

fun headerSizeResize(
    activity: Activity,
    imageView: View,
    width_send: Int,
    height_send: Int
) {
    val display = activity.windowManager.defaultDisplay
    val size = Point()
    try {
        display.getRealSize(size)
    } catch (err: NoSuchMethodError) {
        display.getSize(size)
    }
    val width: Int = size.x / width_send
    val height: Int = size.y / height_send

    val lp = LinearLayout.LayoutParams(width, height)
    lp.gravity = Gravity.CENTER_VERTICAL
    imageView.setLayoutParams(lp)
}

fun Activity.statusBarHide() {

    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun Context.isGPS(): Boolean {
    val manager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

@SuppressLint("MissingPermission")
fun Context.isInternetAvailable(): Boolean {
    val connectivity =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivity.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

fun isValidEmail(strEmail: CharSequence?): Boolean {
    return !TextUtils.isEmpty(strEmail) && Patterns.EMAIL_ADDRESS.matcher(strEmail)
        .matches()
}

fun isValidPassword(pass: String?): Boolean {
    val p = Pattern.compile(
        "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$",
        Pattern.DOTALL
    )
    val m = p.matcher(pass)
    return m.find()
}

fun isPanNumber(
    string: String
): Boolean {
    val r = Pattern.compile("(([A-Za-z]{5})([0-9]{4})([a-zA-Z]))")
    val m = r.matcher(string)
    return m.find() && m.group(0) != null
}

fun isValidGSTIN(
    string: String
): Boolean {
    val r = Pattern.compile("\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}")
    val m = r.matcher(string)
    return m.find() && m.group(0) != null
}

fun isVehicleNumber(
    string: String
): Boolean {
    val r = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}\$")
    val m = r.matcher(string)
    return m.find() && m.group(0) != null
}

fun convertToComaNumber(numberString: String): String {

    var convertedString = numberString

    if (convertedString == "null" || convertedString == "" || convertedString == "." || convertedString == ",")
        convertedString = "0"

    val replacedString = convertedString.replace(",", "").trim().toDouble() + 0
    val returnString = DecimalFormat("#,###,###.00").format(replacedString).replace(",", "").trim()

    return if (returnString[0] == ".".toCharArray()[0] && ("0" + returnString.replace(",", "")
            .trim()).toDouble() < 1
    )
        "0$returnString"
    else if (returnString.length > 1 && returnString[0] == "-".toCharArray()[0] && returnString[1] == ".".toCharArray()[0])
        "-0${returnString.replace("-", "")}"
    else
        DecimalFormat("#,###,###.00").format(convertToTwoDecimal(replacedString.toString()).toDouble())
}

fun convertToTwoDecimal(numberString: String): String {

    var convertedString = numberString

    if (convertedString == "null" || convertedString == "" || convertedString == "." || convertedString == ",")
        convertedString = "0"

    val replacedString = convertedString.replace(",", "").trim().toDouble() + 0
    val returnString = DecimalFormat("0.00").format(replacedString).replace(",", "").trim()

    return if (returnString[0] == ".".toCharArray()[0] && ("0" + returnString.replace(",", "")
            .trim()).toDouble() < 1
    )
        "0$returnString"
    else
        DecimalFormat("0.00").format(replacedString)
}

fun convertToThreeDecimal(numberString: String): String {

    var convertedString = numberString

    if (convertedString == "null" || convertedString == "" || convertedString == "." || convertedString == ",")
        convertedString = "0"

    val replacedString = convertedString.replace(",", "").trim().toDouble() + 0
    val returnString = DecimalFormat("0.000").format(replacedString).replace(",", "").trim()

    return if (returnString[0] == ".".toCharArray()[0] && ("0" + returnString.replace(",", "")
            .trim()).toDouble() < 1
    )
        "0$returnString"
    else
        DecimalFormat("0.000").format(replacedString)
}

fun Context.getAppPath(): File {
    return File(
        Environment.getExternalStorageDirectory()
            .toString() + "/" + resources.getString(R.string.app_name) + "/"
    )
    //        return new File(activity.getFilesDir(), "My Account");
}

fun Context.getPathFromURI(uri: Uri): String? {
    if (DocumentsContract.isDocumentUri(this, uri)) {
        if (isExternalStorageDocument(
                uri
            )
        ) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split =
                docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory()
                    .toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(
                uri
            )
        ) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )
            return getDataColumn(
                contentUri,
                null,
                null
            )
        } else if (isMediaDocument(
                uri
            )
        ) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split =
                docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if (Constants.FILE_TYPE_IMAGE_STR == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if (Constants.FILE_TYPE_VIDEO_STR == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if (Constants.FILE_TYPE_Audio_STR == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            return getDataColumn(
                contentUri,
                selection,
                selectionArgs
            )
        }
    }
    return null
}

private fun Context.getDataColumn(
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        cursor = contentResolver.query(
            uri!!, projection, selection, selectionArgs,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

fun appDateFormat(date: Long): String {
    return SimpleDateFormat("MMM dd, yyyy").format(date)
}

fun serverDateFormat(date: Long): String {
    return SimpleDateFormat("yyyy-MM-dd").format(date)
}

fun appTimeFormat(time: Long): String {
    return SimpleDateFormat("hh:mm aa").format(time)
}

fun getInvoiceAppDateToServerDate(DateWithTime: Long): String {
    return SimpleDateFormat("yyyy-MM-dd").format(DateWithTime)
}

fun getInvoiceDate(DateWithTime: Long): String {
    return SimpleDateFormat("dd").format(DateWithTime)
}

fun getInvoiceMonth(DateWithTime: Long): String {
    return SimpleDateFormat("MM").format(DateWithTime)
}

fun getInvoiceYear(DateWithTime: Long): String {
    return SimpleDateFormat("yyyy").format(DateWithTime)
}

fun convertFullDateToDate(dateString: String): String {
    try {
        val sdf = SimpleDateFormat("MMM dd,yyyy")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return getInvoiceDate(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun convertFullDateToMonth(dateString: String): String {
    try {
        val sdf = SimpleDateFormat("MMM dd,yyyy")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return getInvoiceMonth(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun convertFullDateToYear(dateString: String): String {
    try {
        val sdf = SimpleDateFormat("MMM dd,yyyy")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return getInvoiceYear(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun getDateMonth(DateWithTime: Long): String {
    return SimpleDateFormat("MMM dd").format(DateWithTime)
}

fun convertInvoiceFullDateToDateMonth(dateString: String): String {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return getDateMonth(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun addDays(dateString: String, addDateAmount: Int): String {

    val sdf = SimpleDateFormat("MMM dd, yyyy")
    val c = Calendar.getInstance()
    try {
        c.time = sdf.parse(dateString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    c.add(
        Calendar.DATE,
        addDateAmount
    )

    val sdf1 = SimpleDateFormat("MMM dd, yyyy")

    return sdf1.format(c.time)
}

fun convertInvoiceServerDateToAppDate(dateString: String): String {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return appDateFormat(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun convertInvoiceAppDateToServerDate(dateString: String): String {
    try {
        val sdf = SimpleDateFormat("MMM dd, yyyy")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return getInvoiceAppDateToServerDate(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun appCurrentFullDate(): String {
    return SimpleDateFormat(
        "MMM dd, yyyy",
        Locale.getDefault()
    ).format(Date())
}

fun appCurrentFullDateWithTime(): String {
    return SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    ).format(Date())
}

fun appCurrentDate(): String {
    return SimpleDateFormat(
        "dd",
        Locale.getDefault()
    ).format(Date())
}

fun appCurrentMonth(): String {
    return SimpleDateFormat(
        "MM",
        Locale.getDefault()
    ).format(Date())
}

fun appCurrentYear(): String {
    return SimpleDateFormat(
        "yyyy",
        Locale.getDefault()
    ).format(Date())
}

fun convertDateToFinancialYear(date: String): String {

    val firstFourChars = date.substring(0, 4)
    val substring = firstFourChars.substring((firstFourChars.length - 2).coerceAtLeast(0))
    val substringInt = substring.toInt() + 1

    return "$firstFourChars-$substringInt"
}

fun convertToFinancialYearFull(date: String): String {
    val firstTenChars = date.substring(0, 10)
    return convertDateToFinancialYear(firstTenChars) + "\n" + convertToAppDate(firstTenChars)
}

fun subStringWithLength(date: String, start: Int, last: Int): String {
    return date.substring(start, last)
}

fun convertToFinancialYearWithDateAndTime(date: String): String {

    val firstFour = subStringWithLength(date, 0, 4)
    val addOne = firstFour.toInt() + 1

    return "$firstFour-04-01 / $addOne-03-31"
}

fun convertToFinancialOnlyYear(date: String): String {

    val firstFour = subStringWithLength(date, 0, 4)
    val addOne = firstFour.toInt() + 1

    return "$firstFour-$addOne"
}

fun convertToAppDate(dateString: String?): String {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return appDateFormat(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun setListViewHeightBasedOnChildren(listView: ListView) {
    val listAdapter: ListAdapter = listView.getAdapter()
        ?: // pre-condition
        return
    var totalHeight = 0
    for (i in 0 until listAdapter.getCount()) {
        val listItem: View = listAdapter.getView(i, null, listView)
        listItem.measure(0, 0)
        totalHeight += listItem.measuredHeight
    }
    val params: ViewGroup.LayoutParams = listView.getLayoutParams()
    params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
    listView.setLayoutParams(params)
    listView.requestLayout()
}

fun convertToServerDate(dateString: String?): String {
    try {
        val sdf = SimpleDateFormat("MMM dd,yyyy")
        val date = sdf.parse(dateString)

        val startDate = date.time

        return serverDateFormat(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun convertStringToDate(date: String?): Date {
    try {
        return SimpleDateFormat("MMMM dd, yyyy").parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return Date()
}

fun convertStringToDateTime(date: String?): Date {
    try {
        return SimpleDateFormat("dd-MM-yyyy hh:mm aa").parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return Date()
}

private fun dateEqualCompare(
    fromCompare: String,
    toCompare: String
): Boolean {
    val selectDate = convertStringToDate(
        fromCompare
    ).time
    val systemDate = convertStringToDate(
        toCompare
    ).time
    return selectDate == systemDate
}

private fun timeCompare(fromCompare: String, toCompare: String): Boolean {
    var selectTime: Long = 0
    var systemTime: Long = 0
    try {
        selectTime = SimpleDateFormat("hh:mm aa").parse(fromCompare).time
        systemTime = SimpleDateFormat("hh:mm aa").parse(toCompare).time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return selectTime >= systemTime
}

fun convertPickerDateToAppDate(dateString: String): String {
    try {

        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy")
        val date = sdf.parse(dateString.trim())
        val startDate = date.time

        return appDateFormat(startDate)

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun dateGreaterCompare(greaterDate: String?, lessDate: String?): Boolean {
    val greaterDateLong = convertStringToDate(
        greaterDate
    ).time
    val lessDateLong = convertStringToDate(
        lessDate
    ).time
    return greaterDateLong > lessDateLong
}

fun dateLessCompare(greaterDate: String?, lessDate: String?): Boolean {
    val greaterDateLong = convertStringToDate(
        greaterDate
    ).time
    val lessDateLong = convertStringToDate(
        lessDate
    ).time
    return greaterDateLong < lessDateLong
}

fun Context.datePicker(textView: TextView) {
    val cal = Calendar.getInstance()

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "MMM dd, yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textView.text = sdf.format(cal.time)
        }

    DatePickerDialog(
        this, R.style.DialogTheme, dateSetListener,
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun Activity.closeKeyboard() {
    try {
        val inputManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText)
            inputManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun isValidIFSCode(str: String?): Boolean {
    // Regex to check valid IFSC Code.
    val regex = "^[A-Z]{4}0[A-Z0-9]{6}$"

    val p = Pattern.compile(regex)

    if (str == null) {
        return false
    }

    val m: Matcher = p.matcher(str)

    return m.matches()
}

private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun Activity.getRealPathFromURI(contentUri: Uri): String {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val loader = CursorLoader(this, contentUri, proj, null, null, null)
    val cursor = loader.loadInBackground()
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val result = cursor.getString(column_index)
    cursor.close()
    return result
}

fun Context.getRealPathFromURI_API11to18(
    contentUri: Uri?,
    bBoolean: Boolean
): String {
    val projectData =
        arrayOf(MediaStore.Images.Media.DATA)
    var result: String? = null
    val cursorLoader = CursorLoader(
        this,
        contentUri, projectData, null, null, null
    )
    val cursor: Cursor = cursorLoader.loadInBackground()
    val column_index: Int
    column_index =
        if (bBoolean) cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) else cursor.getColumnIndexOrThrow(
            MediaStore.Video.Media.DATA
        )
    cursor.moveToFirst()
    result = cursor.getString(column_index)
    return result
}

@SuppressLint("Recycle")
fun Context.getRealPathFromURI_BelowAPI11(
    contentUri: Uri?,
    bBoolean: Boolean
): String {
    val proj: Array<String>
    val column_index: Int
    val cursor: Cursor?
    if (bBoolean) {
        proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    } else {
        proj = arrayOf(MediaStore.Video.Media.DATA)
        cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    }
    cursor.moveToFirst()
    return cursor.getString(column_index)
}

fun encryptData(dataToEncrypt: String): String {
    val byte_arr = dataToEncrypt.toByteArray()
    return Base64.encodeToString(byte_arr, Base64.DEFAULT)
}

fun decodeData(dataToDecrypt: String?): String {
    val image_str1 = Base64.decode(dataToDecrypt, 0)
    return String(image_str1)
}

fun Context.showKeyboard(editText: View) {
    val imm =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (!editText.hasFocus()) {
        editText.requestFocus()
    }
    editText.post { imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED) }
}

fun isSpecialChar(pass: String): Boolean {
    val p =
        Pattern.compile("[&@!#+]", Pattern.DOTALL)
    val m = p.matcher(pass)
    return m.find()
}

fun checkAllFilled(sharedPreferenceUtil: SharedPreferenceUtil): Boolean {

    if (sharedPreferenceUtil.getString(Constants.BASIC_INFO_FILLED, "").toString() == "1" &&
        sharedPreferenceUtil.getString(Constants.BANK_INFO_FILLED, "").toString() == "1" &&
        sharedPreferenceUtil.getString(Constants.OTHER_INFO_FILLED, "").toString() == "1" &&
        sharedPreferenceUtil.getString(Constants.TAX_INFO_FILLED, "").toString() == "1"
    ) {
        sharedPreferenceUtil.putValue(Constants.ALL_INFO_FILLED, true)
    } else {
        sharedPreferenceUtil.putValue(Constants.ALL_INFO_FILLED, false)
    }
    return true
}

fun Activity.errorMessagePopUp(
    title: String,
    msg: String,
    noButtonHide: Boolean,
    cancelable: Boolean
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(msg)

    if (cancelable)
        builder.setCancelable(true)
    else
        builder.setCancelable(false)

    if (noButtonHide)
        builder.setPositiveButton("Ok") { _, _ ->

        }

    builder.show()
}

fun Activity.noInternetPopup(noInternetInterface: NoInternetInterface, cancelButton: Boolean) {
    if (!isInternetAvailable())
        try {
            val alertDialogs: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this)
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialog: View = inflater.inflate(R.layout.no_internet_popup, null)

            imageResize(this, dialog, 1, 2)

            val btNoInternetPopupGoBack =
                dialog.findViewById(R.id.btNoInternetPopupGoBack) as Button
            val btNoInternetPopupRefresh =
                dialog.findViewById(R.id.btNoInternetPopupRefresh) as Button

            alertDialogs.setView(dialog)
            val alert = alertDialogs.create()
            alert.setCancelable(false)
            alert.show()

            if (cancelButton)
                btNoInternetPopupGoBack.visibility = View.VISIBLE
            else
                btNoInternetPopupGoBack.visibility = View.GONE

            btNoInternetPopupGoBack.setOnClickListener(View.OnClickListener {
                alert.dismiss()
                noInternetInterface.noInternetBackClick()
            })

            btNoInternetPopupRefresh.setOnClickListener(View.OnClickListener {
                alert.dismiss()
                noInternetInterface.noInternetRefreshClick()
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
}

fun Activity.getSizeFromUri(uri: Uri): Long {
    var dataSize = 0
    var f: File? = null
    val scheme = uri.scheme
    println("Scheme type $scheme")
    if (scheme == ContentResolver.SCHEME_CONTENT) {
        try {
            val fileInputStream = applicationContext.contentResolver.openInputStream(uri)
            dataSize = fileInputStream!!.available()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        println("File size in bytes$dataSize")
    } else if (scheme == ContentResolver.SCHEME_FILE) {
        val path = uri.path
        try {
            f = File(path)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (f != null) println("File size in bytes" + f.length())
    }
    return f?.length() ?: 0
}

fun downloadFile(
    activity: Activity,
    url: String,
    fileName: String?,
    folderName: String,
    businessName: String?,
    shareFile: Boolean
) {
    try {
        Toast.makeText(activity, "Download Started.", Toast.LENGTH_SHORT).show()
        var downloadIdConstant = 0L
        if (url.isNotEmpty()) {
            val uri = Uri.parse(url)
            if (0L == downloadIdConstant)
                activity.registerReceiver(
                    object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            val action = intent.action
                            if (0L == downloadIdConstant && DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                                val downloadId = intent.getLongExtra(
                                    DownloadManager.EXTRA_DOWNLOAD_ID, 0
                                )
                                if (0L == downloadIdConstant)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        context.openDownloadedAttachment(
                                            context,
                                            downloadId,
                                            shareFile
                                        )
                                    }, 500)

                                downloadIdConstant = downloadId
                            }
                        }
                    }, IntentFilter(
                        DownloadManager.ACTION_DOWNLOAD_COMPLETE
                    )
                )
            val request = DownloadManager.Request(uri)
            request.setMimeType(getMimeType(uri.toString()))
            request.setTitle(fileName)
            request.setDescription("Downloading attachment..")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                activity.resources.getString(R.string.app_name) + File.separator + businessName.toString()
                    .replace(" ", "_") + File.separator + folderName + File.separator + fileName
            )
            val dm =
                activity.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        }
    } catch (e: IllegalStateException) {
        Toast.makeText(
            activity,
            "Please insert an SD card to download file",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun getMimeType(url: String): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) {
        val mime = MimeTypeMap.getSingleton()
        type = mime.getMimeTypeFromExtension(extension)
    }
    return type
}

var attachmentDownloadCompleteReceive: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            val downloadId = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, 0
            )
            context.openDownloadedAttachment(context, downloadId, true)
        }
    }
}

@SuppressLint("LogNotTimber")
fun Context.openDownloadedAttachment(context: Context, downloadId: Long, shareFile: Boolean) {
    val downloadManager =
        context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query()
    query.setFilterById(downloadId)
    val cursor: Cursor = downloadManager.query(query)
    if (cursor.moveToFirst()) {
        val downloadStatus: Int =
            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        when (downloadStatus) {
            DownloadManager.STATUS_SUCCESSFUL -> {

                val downloadLocalUri: String =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))

                Log.i("Download File", downloadLocalUri)

                if (shareFile)
                    sendFile(downloadLocalUri.toString().replace("file://", ""))
//                else
//                    openFile(
//                        downloadLocalUri.toString().replace("file://", "")
//                    )
                return
            }
            else -> {

                Toast.makeText(
                    context,
                    "Something went wrong. Try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    cursor.close()
}

fun Context.openFile(myFilePath: String) {

    val photoURI = FileProvider.getUriForFile(
        this,
        this.applicationContext.packageName.toString() + ".provider",
        File(myFilePath)
    )

    val pdfOpenIntent = Intent(Intent.ACTION_VIEW)
    pdfOpenIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    if (myFilePath.contains(".pdf"))
        pdfOpenIntent.setDataAndType(photoURI, "application/pdf")
    else if (myFilePath.contains(".jpg") || myFilePath.contains(".jpeg"))
        pdfOpenIntent.setDataAndType(photoURI, "image/jpeg")

    try {
        val viewerIntent = Intent.createChooser(pdfOpenIntent, "Open Document")
        startActivity(viewerIntent)
    } catch (e: ActivityNotFoundException) {
    }
}

fun Context.sendFile(myFilePath: String) {
    val intentShareFile = Intent(Intent.ACTION_SEND)
    val fileWithinMyDir: File = File(myFilePath)

    if (fileWithinMyDir.exists()) {
        intentShareFile.type = "application/pdf"
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("$myFilePath"))
        intentShareFile.putExtra(
            Intent.EXTRA_SUBJECT,
            "Sharing File..."
        )
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
        startActivity(Intent.createChooser(intentShareFile, "Share File"))
    }
}

fun Activity.shareLink(url: String) {
    ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setChooserTitle("Share Link")
        .setText(url)
        .startChooser();
}

fun emptyLocalData(sharedPreferenceUtil: SharedPreferenceUtil) {
    sharedPreferenceUtil.remove(Constants.ACCOUNT_TOKEN)
    sharedPreferenceUtil.remove(Constants.FIRM_TOKEN)
    sharedPreferenceUtil.remove(Constants.FIRM_LIST)
    sharedPreferenceUtil.remove(Constants.BASIC_INFO_FILLED)
    sharedPreferenceUtil.remove(Constants.TAX_INFO_FILLED)
    sharedPreferenceUtil.remove(Constants.BANK_INFO_FILLED)
    sharedPreferenceUtil.remove(Constants.OTHER_INFO_FILLED)
    sharedPreferenceUtil.remove(Constants.USER_ID)
    sharedPreferenceUtil.remove(Constants.ROLE)
    sharedPreferenceUtil.remove(Constants.NO_FIRST_TIME_LOGIN)
    sharedPreferenceUtil.remove(Constants.BUSINESS_NAME)
    sharedPreferenceUtil.remove(Constants.BUSINESS_LOGO)
    sharedPreferenceUtil.remove(Constants.MOBILE)
    sharedPreferenceUtil.remove(Constants.YEAR_LIST)
    sharedPreferenceUtil.remove(Constants.CURRENT_YEAR_SERVER)
    sharedPreferenceUtil.remove(Constants.CURRENT_YEAR_APP)
    sharedPreferenceUtil.remove(Constants.STATE_LIST)
    sharedPreferenceUtil.remove(Constants.COUNTRY_LIST)
    sharedPreferenceUtil.remove(Constants.UNIT_LIST)
    sharedPreferenceUtil.clear()
}

fun filterEditTextLengthLimit(yourEditText: EditText, digitsBeforeZero: Int, digitsAfterZero: Int) {
    yourEditText.filters = arrayOf<InputFilter>(
        DecimalDigitsInputFilter(
            digitsBeforeZero,
            digitsAfterZero
        )
    )
}

fun filterEditTextLengthLimit(yourEditText: TextView, digitsBeforeZero: Int, digitsAfterZero: Int) {
    yourEditText.filters = arrayOf<InputFilter>(
        DecimalDigitsInputFilter(
            digitsBeforeZero,
            digitsAfterZero
        )
    )
}

fun convertDigitAmountToMinimizeAmount(amount: String, getDigit: Boolean): String {
    var getAmount = 0.00
    var convertedAmount = "0.00"
    getAmount =
        if (amount.replace(",", "") == "null" || amount.replace(",", "") == "" || amount.replace(
                ",",
                ""
            ) == "." || amount.replace(",", "") == "," || amount.replace(",", "") == "-"
        )
            0.00
        else
            amount.replace(",", "").toDouble()

    when {
        getAmount > 99999999999999999 -> convertedAmount = String.format(
            "%.2f",
            getAmount / 100000000000000000.00
        ) + " Snk"
        getAmount > 999999999999999 -> convertedAmount = String.format(
            "%.2f",
            getAmount / 1000000000000000.00
        ) + " Pdm"
        getAmount > 9999999999999 -> convertedAmount = String.format(
            "%.2f",
            getAmount / 10000000000000.00
        ) + " Nil"
        getAmount > 99999999999 -> convertedAmount = String.format(
            "%.2f",
            getAmount / 100000000000.00
        ) + " Kh"
        getAmount > 999999999 -> convertedAmount =
            String.format("%.2f", getAmount / 1000000000.00) + " Ar"
        getAmount > 9999999 -> convertedAmount =
            String.format("%.2f", getAmount / 10000000.00) + " Cr"
        getAmount > 99999 -> convertedAmount =
            String.format("%.2f", getAmount / 100000.00) + " Lakhs"
        else -> convertedAmount = getAmount.toString()
    }
    if (getAmount < 100000.00) {
        if (getDigit)
            return convertToComaNumber(convertedAmount)
        else
            return ""
    } else {
        val splited: ArrayList<String> =
            convertedAmount.split("\\s+".toRegex()) as ArrayList<String>
        if (getDigit)
            return splited[0]
        else
            return splited[1]
    }
}

fun roundOff(amount: String, roundOffValue: String, roundOffVisibility: Boolean): String {
    var convertedAmount = amount.toBigDecimal().toString()
    if (amount.replace(",", "") == "null" || amount.replace(",", "") == "" || amount.replace(
            ",",
            ""
        ) == "." || amount.replace(",", "") == "," || amount.replace(",", "") == "-"
    )
        convertedAmount = "0.00"
    var roundedAmount = 0.00

    if (convertedAmount.contains("."))
        roundedAmount = String.format(
            "%.2f",
            (("0." + convertedAmount.substring(convertedAmount.lastIndexOf(".") + 1)).toDouble())
        ).toDouble()

    if (roundOffVisibility) {
        when (roundOffValue) {
            "0" -> {
                return 0.00.toString()
            }
            "1" -> {
                return if (roundedAmount < 0.5 && roundedAmount != 0.00) {
                    "-$roundedAmount"
                } else if (roundedAmount != 0.00)
                    String.format("%.2f", (1.00 - roundedAmount))
                else
                    0.00.toString()
            }
            "2" -> {
                return if (roundedAmount != 0.00)
                    String.format("%.2f", (1.00 - roundedAmount))
                else
                    0.00.toString()
            }
            "3" -> {
                return if (roundedAmount != 0.00)
                    "-$roundedAmount"
                else
                    0.00.toString()
            }
            else -> return 0.00.toString()
        }
    } else
        return 0.00.toString()

}

fun extractNumberFromAnyAlphaNumeric(alphaNumeric1: String): Int {
    var alphaNumeric = alphaNumeric1
    alphaNumeric = if (alphaNumeric.isNotEmpty()) alphaNumeric.replace("\\D+".toRegex(), "") else ""
    return if (alphaNumeric.isNotEmpty()) alphaNumeric.toInt() else 0
}

fun shareData(activity: Activity, invoiceNo: String, totalPayable: String, businessName: String?) {
    val waIntent = Intent(Intent.ACTION_SEND)
    waIntent.type = "text/plain"

    val appLink = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"

    val dataPassing =
        "Dear Sir/Madam,\n\nGreetings from $businessName...!\n\nGentle reminder to make payment of ${
            activity.resources.getString(R.string.rupee_symbol)
        } $totalPayable due against Invoice No. $invoiceNo.\n\nLet us know, in case any queries or more information required. " +
                "\n \nStay healthy Stay Safe. \nTo join our family FINTER, download the app now.\n$appLink"

    waIntent.putExtra(
        Intent.EXTRA_TEXT,
        dataPassing
    )
    activity.startActivity(Intent.createChooser(waIntent, "Share with"))
}

fun compareDate(invoiceDate: String, registrationDate: String): Boolean {
    return if (invoiceDate == "null" || invoiceDate == "" || registrationDate == "null" || registrationDate == "")
        false
    else {
        val sdf = SimpleDateFormat("MMM dd,yyyy")

        val invoiceDateParsed = sdf.parse(invoiceDate)
        val registrationDateParsed = sdf.parse(registrationDate)
        invoiceDateParsed.time >= registrationDateParsed.time
    }
}

fun Context.successTone() {
    val notification =
        Uri.parse("android.resource://" + packageName.toString() + "/" + R.raw.snapchat_notification_sound)
    val r: Ringtone = RingtoneManager.getRingtone(applicationContext, notification)
    r.play()
}

fun isValidWebsite(potentialUrl: String): Boolean {

    try {
        return if (Patterns.WEB_URL.matcher(potentialUrl).matches())
            true
        else {
            URL(potentialUrl).toURI()
            true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}


fun Activity.duplicateNoException(
    msg: String,
    new_number: String,
    duplicateNumberDataInterface: DuplicateNumberDataInterface
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle("Duplicate Number.")
    builder.setMessage("$msg\nDue you want to continue with new Number-->$new_number")
    builder.setCancelable(false)

    builder.setPositiveButton("Submit") { _, _ ->
        duplicateNumberDataInterface.submitClicked(new_number)
    }
    builder.setNegativeButton("Cancel") { _, _ ->

    }

    builder.show()
}

//Check if you already have read storage permission
fun checkPermissionForReadWrite(context: Context): Boolean {
    val result: Int =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

    return result == PackageManager.PERMISSION_GRANTED
}

//Request Permission For Read Storage
fun requestPermissionForReadWrite(context: Context) {
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ), Constants.REQUEST_CODE_WRITE_STORAGE_PERMISION
    )
}

fun isPermissionGrantedForMediaLocationAccess(context: Context): Boolean {
    Log.i("Tag", "checkPermissionForAML")
    val result: Int =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_MEDIA_LOCATION
        )
    return result == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.Q)
fun requestPermissionForAccessMediaLocation(context: Context) {
    Log.i("Tag", "requestPermissionForAML")

    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION),
        Constants.MEDIA_LOCATION_PERMISSION_REQUEST_CODE
    )
}

@Throws(IOException::class)
fun createTempImageFile(context: Context): File? {
    val timeStamp = SimpleDateFormat(
        "yyyyMMdd_HHmmss",
        Locale.getDefault()
    ).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.externalCacheDir
    return File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",  /* suffix */
        storageDir /* directory */
    )
}


fun Activity.fetchCamera(): String {
    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        camera()
    } else {
        if (isPermissionGrantedForMediaLocationAccess(this)) {
            camera()
        } else {
            requestPermissionForAccessMediaLocation(this)
            ""
        }
    }
}

fun Activity.fetchMediaLocation() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {

        openChooser()

    } else {

        if (isPermissionGrantedForMediaLocationAccess(this)) {

            openChooser()
        } else {
            Log.i("Tag", "else chooseFile")

            requestPermissionForAccessMediaLocation(this)
        }

    }
}

private fun Activity.camera(): String {
    var mTempPhotoPath = ""
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(packageManager) != null) {
        // Create the temporary File where the photo should go
        var photoFile: File? = null
        try {
            photoFile = createTempImageFile(this)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (photoFile != null) {

            mTempPhotoPath = photoFile.absolutePath
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                Constants.FILE_PROVIDER_AUTHORITY,
                photoFile
            )

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, Constants.CAMERA)
        }
    }
    return mTempPhotoPath
}

private fun Activity.openChooser() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
    intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(
        Intent.createChooser(intent, "Select Picture"),
        Constants.CHOOSE_FILE
    )
}

fun fileFromContentUri(context: Context, contentUri: Uri): File {
    // Preparing Temp file name
    val file = File(contentUri.path.toString())
    val fileExtension = getFileExtension(context, contentUri)
    val fileName = file.name + if (fileExtension != null) ".$fileExtension" else ""

    // Creating Temp file
    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()

    try {
        val oStream = FileOutputStream(tempFile)
        val inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let {
            copy(inputStream, oStream)
        }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}

fun getSize(context: Context, uri: Uri?): String {
    var fileSize: String = ""
    val cursor: Cursor? = context.contentResolver
        .query(uri!!, null, null, null, null, null)
    cursor.use { cursor ->
        if (cursor != null && cursor.moveToFirst()) {

            val sizeIndex: Int = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (!cursor.isNull(sizeIndex)) {
                fileSize = cursor.getString(sizeIndex)
            }
        }
    }
    return fileSize
}

fun getFirstCharacterFromWords(s: CharSequence): String {

    var initials = ""
    for (i in s.toString().trim().split(" ")) {
        if (s != "" && i.isNotEmpty())
            initials += i[0]
        else
            initials = ""
    }
    return initials.toUpperCase(java.util.Locale.ROOT)
}

fun setInfoOfCalculationToSharedPreferenceUtil(
    sharedPreferenceUtil: SharedPreferenceUtil,
    productSubTotal: String,
    productTotal: String,
    totalWithOtherExpenses: String,
    gstFromList: String,
    invType: String
) {
    val listData = JSONObject()
    listData.put("subTotalAmount", convertToTwoDecimal(productSubTotal))
    listData.put("totalAmount", convertToTwoDecimal(productTotal))
    listData.put("grandTotal", convertToTwoDecimal(totalWithOtherExpenses))
    listData.put("gstFromList", convertToTwoDecimal(gstFromList))
    listData.put("invType", invType)

    sharedPreferenceUtil.putValue(Constants.CALCULATION_DATA, listData.toString())
}

fun convertExpenseServerDataToJsonObject(filename: String): JSONObject {
    val parts = filename.split("\\(".toRegex()).toTypedArray()
    val beforeFirstDot = parts[0]
    val str = filename.replace(beforeFirstDot, "").replace("(", "").replace(")", "")
    val array = ArrayList<String>()

    val between = str.split(",|=".toRegex()).toTypedArray()
    for (i in between.indices) {

        if (between[i] != "null")
            array.add(between[i])
        else
            array.add("0")
    }
    var newString = ""
    for (i in 0 until array.size) {

        newString += if ((i % 2) == 0) {
            "\"" + array[i].trim() + "\"" + "="
        } else {
            "\"" + array[i].trim() + "\"" + ","
        }
    }

    val objectData = JSONObject("{$newString}".replace(",}", "}"))
    return objectData
}