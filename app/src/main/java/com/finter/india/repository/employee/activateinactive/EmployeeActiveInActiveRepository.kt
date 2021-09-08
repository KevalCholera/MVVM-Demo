package com.finter.india.repository.employee.activateinactive

import androidx.lifecycle.MutableLiveData
import com.finter.india.model.employee.activeinactive.EmployeeActiveInActiveResponse
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

class EmployeeActiveInActiveRepository @Inject constructor(
    private val client: PokedexClient
) : Repository {

    suspend fun activeInActiveEmployee(
        user_id: String,
        is_active: String,
        onSuccess: (EmployeeActiveInActiveResponse) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<EmployeeActiveInActiveResponse>()

        client.activeInActiveEmployee(user_id, is_active) {
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
        liveData.apply { postValue(EmployeeActiveInActiveResponse(false, "")) }
    }
}