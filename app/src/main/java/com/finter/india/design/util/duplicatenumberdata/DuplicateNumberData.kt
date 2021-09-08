package com.finter.india.design.util.duplicatenumberdata

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DuplicateNumberData(
    @field:Json(name = "status") val status: Boolean,
    @field:Json(name = "data") val data: DuplicateNumberSubData?,
    @field:Json(name = "message") val message: String?
)