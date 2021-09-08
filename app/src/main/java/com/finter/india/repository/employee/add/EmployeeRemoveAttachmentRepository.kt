package com.finter.india.repository.employee.add

import androidx.lifecycle.MutableLiveData
import com.finter.india.model.employee.details.EmployeeDetailsResponse
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

class EmployeeRemoveAttachmentRepository @Inject constructor(
    private val client: PokedexClient
) : Repository {
    suspend fun deleteEmployeeAttachment(
        id: String,
        employee_id: String,
        onSuccess: (EmployeeDetailsResponse) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<EmployeeDetailsResponse>()
        client.deleteEmployeeAttachment(
            id,
            employee_id
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
            postValue(EmployeeDetailsResponse(false, "", null))
        }
    }
}