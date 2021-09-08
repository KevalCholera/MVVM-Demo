package com.finter.india.model.employee.createemployee

import com.finter.india.model.BaseViewState
import com.finter.india.model.Status

class EmployeeCreateViewState(
    val status: Status,
    val error: String? = null,
    val response: EmployeeCreateResponse? = null
) : BaseViewState(status, error)