package com.finter.india.design.employee.interf

import android.app.Activity
import android.content.Context
import com.finter.india.design.employee.add.EmployeeAddViewModel
import com.finter.india.design.employee.list.data.EmployeeData
import com.finter.india.utils.SharedPreferenceUtil

public interface EmployeeAdapterInterface {
    fun onMethodCallback(
        sharedPreferenceUtil: SharedPreferenceUtil,
        context: Context,
        data: EmployeeData
    )

    fun onDeleteAttachment(
        activity: Activity,
        attachmentName: String,
        employeeAddViewModel: EmployeeAddViewModel,
        attachmentId: String,
        customerId: String
    )
}