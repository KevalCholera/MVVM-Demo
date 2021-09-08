package com.finter.india.design.employee.interf

import com.finter.india.design.party.add.adapter.PartyAddDocumentData

public interface PartyDetailsAdapterInterface {
    fun onClickAttachment(
        customerAddDocumentData: PartyAddDocumentData
    )

    fun onClickView(customerAddDocumentData: PartyAddDocumentData)
    fun onCheckStoragePermission(): Boolean
}