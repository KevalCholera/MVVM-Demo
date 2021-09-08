package com.finter.india.model.employee.add

import com.finter.india.design.employee.add.data.EmployeeAddData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeAddResponse(
    @field:Json(name = "status") val status: Boolean,
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "data") val data: EmployeeAddData?
)