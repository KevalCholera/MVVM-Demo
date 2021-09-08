package com.finter.india.design.employee.details.popup

import android.app.Activity
import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.finter.india.R
import com.finter.india.design.employee.interf.EmployeeCreateAccountInterface
import com.finter.india.utils.isValidPassword

class EmployeeCreateAccountPopUp {

    fun showPopupWindow(
        activity: Activity,
        employeeCreateAccountInterface: EmployeeCreateAccountInterface,
        id: String
    ) {
        val inflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.element_employee_create_account, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        val popupWindow = PopupWindow(popupView, width, height, true)
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val tvElementEmployeeCreateAccountErrorMessage =
            popupView.findViewById<TextView>(R.id.tvElementEmployeeCreateAccountErrorMessage)
        val tvEmployeeCreateAccountDescription =
            popupView.findViewById<com.abdulhakeem.seemoretextview.SeeMoreTextView>(R.id.tvEmployeeCreateAccountDescription)
        val etElementEmployeeCreateAccountPassword =
            popupView.findViewById<EditText>(R.id.etElementEmployeeCreateAccountPassword)
        val etElementEmployeeCreateAccountConfirmPassword =
            popupView.findViewById<EditText>(R.id.etElementEmployeeCreateAccountConfirmPassword)
        val btElementEmployeeCreateAccountSubmit =
            popupView.findViewById<Button>(R.id.btElementEmployeeCreateAccountSubmit)
        val btElementEmployeeCreateAccountCancel =
            popupView.findViewById<Button>(R.id.btElementEmployeeCreateAccountCancel)
        val ivElementEmployeeCreateAccountPasswordShowHide =
            popupView.findViewById<ImageView>(R.id.ivElementEmployeeCreateAccountPasswordShowHide)

        tvEmployeeCreateAccountDescription.setTextMaxLength(50)
        tvEmployeeCreateAccountDescription.toggle()
        tvEmployeeCreateAccountDescription.expandText(false)
        tvEmployeeCreateAccountDescription.setContent(activity.resources.getString(R.string.long_pass_desc))
        tvEmployeeCreateAccountDescription.setSeeMoreTextColor(R.color.green)

        tvElementEmployeeCreateAccountErrorMessage.visibility = View.GONE

        btElementEmployeeCreateAccountCancel.setOnClickListener { popupWindow.dismiss() }

        ivElementEmployeeCreateAccountPasswordShowHide.setOnClickListener {
            if (ivElementEmployeeCreateAccountPasswordShowHide.drawable.constantState == ContextCompat.getDrawable(
                    activity,
                    R.drawable.hide_password_red
                )?.constantState
            ) {
                etElementEmployeeCreateAccountPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                etElementEmployeeCreateAccountConfirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                ivElementEmployeeCreateAccountPasswordShowHide.setImageResource(R.drawable.show_password_custom_red)
            } else {
                etElementEmployeeCreateAccountPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                etElementEmployeeCreateAccountConfirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                ivElementEmployeeCreateAccountPasswordShowHide.setImageResource(R.drawable.hide_password_red)
            }
        }

        btElementEmployeeCreateAccountSubmit.setOnClickListener {
            if (etElementEmployeeCreateAccountPassword.text == null || etElementEmployeeCreateAccountPassword.text.toString() == "" || etElementEmployeeCreateAccountConfirmPassword.text == null || etElementEmployeeCreateAccountConfirmPassword.text.toString() == "") {
                tvElementEmployeeCreateAccountErrorMessage.visibility = View.VISIBLE
                tvElementEmployeeCreateAccountErrorMessage.text = "Enter All Fields"

                tvElementEmployeeCreateAccountErrorMessage.postDelayed(Runnable {
                    tvElementEmployeeCreateAccountErrorMessage.visibility = View.GONE
                }, 3000)

            } else if (!isValidPassword(etElementEmployeeCreateAccountPassword.text.toString())) {
                tvElementEmployeeCreateAccountErrorMessage.visibility = View.VISIBLE
                tvElementEmployeeCreateAccountErrorMessage.text = "Enter Valid Password"

                tvElementEmployeeCreateAccountErrorMessage.postDelayed(Runnable {
                    tvElementEmployeeCreateAccountErrorMessage.visibility = View.GONE
                }, 3000)
            } else if (etElementEmployeeCreateAccountPassword.text.toString() != etElementEmployeeCreateAccountConfirmPassword.text.toString()) {
                tvElementEmployeeCreateAccountErrorMessage.visibility = View.VISIBLE
                tvElementEmployeeCreateAccountErrorMessage.text =
                    "Password and Confirm Password doesn't match."

                tvElementEmployeeCreateAccountErrorMessage.postDelayed(Runnable {
                    tvElementEmployeeCreateAccountErrorMessage.visibility = View.GONE
                }, 3000)
            } else {
                popupWindow.dismiss()
                employeeCreateAccountInterface.onClickSubmit(
                    id,
                    etElementEmployeeCreateAccountPassword.text.toString()
                )
            }
        }
    }
}