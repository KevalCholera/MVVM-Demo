package com.finter.india.model.employee.activeinactive

import com.finter.india.model.BaseViewState
import com.finter.india.model.Status

class EmployeeActiveInActiveViewState(
    val status: Status,
    val error: String? = null,
    val response: EmployeeActiveInActiveResponse? = null
) : BaseViewState(status, error)