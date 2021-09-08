package com.finter.india.model.employee.details

import com.finter.india.model.BaseViewState
import com.finter.india.model.Status

class EmployeeDetailsViewState(
    val status: Status,
    val error: String? = null,
    val response: EmployeeDetailsResponse? = null
) : BaseViewState(status, error)