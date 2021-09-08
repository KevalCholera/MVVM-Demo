package com.finter.india.model.employee.activeinactive

import com.finter.india.design.employee.details.data.EmployeeDetailsSubData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeActiveInActiveResponse(
    @field:Json(name = "status") val status: Boolean,
    @field:Json(name = "message") val message: String?
)