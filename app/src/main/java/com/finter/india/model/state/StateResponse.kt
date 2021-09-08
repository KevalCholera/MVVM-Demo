package com.finter.india.model.state

import com.finter.india.design.util.state.StateData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StateResponse(
    @field:Json(name = "status") val status: Boolean,
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "data") val data: List<StateData>?
)