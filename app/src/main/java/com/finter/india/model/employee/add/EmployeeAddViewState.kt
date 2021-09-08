package com.finter.india.model.employee.add

import com.finter.india.model.BaseViewState
import com.finter.india.model.Status

class EmployeeAddViewState(
    val status: Status,
    val error: String? = null,
    val response: EmployeeAddResponse? = null
) : BaseViewState(status, error)