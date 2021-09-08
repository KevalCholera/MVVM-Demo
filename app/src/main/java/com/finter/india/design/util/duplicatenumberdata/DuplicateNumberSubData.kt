package com.finter.india.design.util.duplicatenumberdata

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DuplicateNumberSubData(
    @field:Json(name = "new_inv_no") val new_inv_no: String?,
    @field:Json(name = "vou_number") val vou_number: String?,
    @field:Json(name = "new_payment_no") val new_payment_no: String?,
    @field:Json(name = "new_debit_note_no") val new_debit_note_no: String?,
    @field:Json(name = "new_challan_no") val new_challan_no: String?,
    @field:Json(name = "new_estimate_no") val new_estimate_no: String?,
    @field:Json(name = "new_jv_no") val new_jv_no: String?,
    @field:Json(name = "new_pi_no") val new_pi_no: String?
)