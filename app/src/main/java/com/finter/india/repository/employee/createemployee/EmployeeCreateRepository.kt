package com.finter.india.repository.employee.createemployee

import androidx.lifecycle.MutableLiveData
import com.finter.india.model.employee.createemployee.EmployeeCreateResponse
import com.finter.india.network.PokedexClient
import com.finter.india.repository.Repository
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeCreateRepository @Inject constructor(
    private val client: PokedexClient
) : Repository {

    suspend fun employeeCreate(
        emp_id: String,
        password: String,
        onSuccess: (EmployeeCreateResponse) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<EmployeeCreateResponse>()
        client.employeeCreate(emp_id, password) {
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
        liveData.apply { postValue(EmployeeCreateResponse(false, "")) }
    }
}