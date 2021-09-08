package com.finter.india.design.employee.list.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeListSubData(
    @field:Json(name = "data") val data: List<EmployeeData>?,
    @field:Json(name = "current_page") val current_page: String?,
    @field:Json(name = "first_page_url") val first_page_url: String?,
    @field:Json(name = "next_page_url") val next_page_url: String?,
    @field:Json(name = "last_page_url") val last_page_url: String?,
    @field:Json(name = "from") val from: String?,
    @field:Json(name = "last_page") val last_page: String?,
    @field:Json(name = "path") val path: String?,
    @field:Json(name = "per_page") val per_page: String?,
    @field:Json(name = "prev_page_url") val prev_page_url: String?,
    @field:Json(name = "to") val to: String?,
    @field:Json(name = "total") val total: String?
)