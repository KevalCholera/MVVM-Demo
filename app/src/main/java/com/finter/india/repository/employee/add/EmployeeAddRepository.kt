package com.finter.india.repository.employee.add

import androidx.lifecycle.MutableLiveData
import com.finter.india.model.employee.add.EmployeeAddResponse
import com.finter.india.network.PokedexClient
import com.finter.india.repository.Repository
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class EmployeeAddRepository @Inject constructor(
    private val client: PokedexClient
) : Repository {
    suspend fun employeeAdd(
        name: String,
        sales_person_no: String,
        address_1: String,
        address_2: String,
        city: String,
        state: String,
        pin_code: String,
        country: String,
        contact_no: String,
        email: String,
        id_no: String,
        onSuccess: (EmployeeAddResponse) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<EmployeeAddResponse>()

        client.employeeAdd(
            name,
            sales_person_no,
            address_1,
            address_2,
            city,
            state,
            pin_code,
            country,
            contact_no,
            email,
            id_no
        ) {
            it.onSuccess {
                data.whatIfNotNull { response ->
                    onSuccess(response)
                    liveData.apply { postValue(response) }
                }
            }
                .onError {
                     if (statusCode == com.skydoves.sandwich.StatusCode.Forbidden) {
                        onError(statusCode.name)
                    } else {
                        onError(message())
                    }
                }
                .onException {
                    onError(message())
                }
        }
        liveData.apply {
            postValue(EmployeeAddResponse(false, "", null))
        }
    }

    suspend fun employeeAdd(
        name: String,
        sales_person_no: String,
        address_1: String,
        address_2: String,
        city: String,
        state: String,
        pin_code: String,
        country: String,
        contact_no: String,
        email: String,
        id_no: String,
        attachment: Array<MultipartBody.Part?>,
        onSuccess: (EmployeeAddResponse) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<EmployeeAddResponse>()

        client.employeeAdd(
            name,
            sales_person_no,
            address_1,
            address_2,
            city,
            state,
            pin_code,
            country,
            contact_no,
            email,
            id_no,
            attachment
        ) {
            it.onSuccess {
                data.whatIfNotNull { response ->
                    onSuccess(response)
                    liveData.apply { postValue(response) }
                }
            }
                .onError {
                     if (statusCode == com.skydoves.sandwich.StatusCode.Forbidden) {
                        onError(statusCode.name)
                    } else {
                        onError(message())
                    }
                }
                .onException {
                    onError(message())
                }
        }
        liveData.apply {
            postValue(EmployeeAddResponse(false, "", null))
        }
    }
}