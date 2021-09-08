package com.finter.india.model.employee.list

import com.finter.india.model.BaseViewState
import com.finter.india.model.Status

class EmployeeViewState(
    val status: Status,
    val error: String? = null,
    val response: EmployeeResponse? = null
) : BaseViewState(status, error)