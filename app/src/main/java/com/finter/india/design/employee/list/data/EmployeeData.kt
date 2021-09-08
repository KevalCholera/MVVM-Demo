package com.finter.india.design.employee.list.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeData(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "ac_id") val ac_id: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "eid_no") val eid_no: String?,
    @field:Json(name = "address_1") val address_1: String?,
    @field:Json(name = "address_2") val address_2: String?,
    @field:Json(name = "road") val road: String?,
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "state_id") val state: String?,
    @field:Json(name = "pin_code") val pin_code: String?,
    @field:Json(name = "country_id") val country: String?,
    @field:Json(name = "country_name") val country_name: String?,
    @field:Json(name = "mobile") val contact_no: String?,
    @field:Json(name = "email") val email: String?,
    @field:Json(name = "id_no") val id_no: String?,
    @field:Json(name = "attachment") val attachment: String?
)