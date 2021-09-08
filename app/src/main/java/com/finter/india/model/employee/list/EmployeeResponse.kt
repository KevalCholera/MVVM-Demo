package com.finter.india.model.employee.list

import com.finter.india.design.employee.list.data.EmployeeListSubData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeResponse(
    @field:Json(name = "status") val status: Boolean,
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "data") val data: EmployeeListSubData?
)