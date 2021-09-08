package com.finter.india.repository.employee.list

import androidx.lifecycle.MutableLiveData
import com.finter.india.model.employee.list.EmployeeResponse
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

class EmployeeRepository @Inject constructor(
    private val client: PokedexClient
) : Repository {

    suspend fun employeeList(
        page: String,
        search: String,
        onSuccess: (EmployeeResponse) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<EmployeeResponse>()

        client.employeeList(page,search) {
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
        liveData.apply { postValue(EmployeeResponse(false, "", null)) }
    }
}