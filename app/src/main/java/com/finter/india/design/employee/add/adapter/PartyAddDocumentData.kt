package com.finter.india.design.party.add.adapter

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PartyAddDocumentData(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "attachment_name") val attachment_name: String?,
    @field:Json(name = "attachment") val attachment: String?,
    @field:Json(name = "extension") val extension: String?,
    @field:Json(name = "newDoc") val newDoc: Boolean?
)