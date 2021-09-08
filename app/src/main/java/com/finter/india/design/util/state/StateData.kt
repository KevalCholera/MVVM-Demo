package com.finter.india.design.util.state

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StateData(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "country_id") val country_id: String?,
    @field:Json(name = "state_code") val state_code: String?,
    @field:Json(name = "tin_code") val tin_code: String?
)