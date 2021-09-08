package com.finter.india.model.state

import com.finter.india.model.BaseViewState
import com.finter.india.model.Status

class StateViewState(
    val status: Status,
    val error: String? = null,
    val response: StateResponse? = null
) : BaseViewState(status, error)