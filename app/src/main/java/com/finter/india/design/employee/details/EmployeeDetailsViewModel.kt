package com.finter.india.design.employee.details

import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.finter.india.base.LiveCoroutinesViewModel
import com.finter.india.model.Status
import com.finter.india.model.employee.activeinactive.EmployeeActiveInActiveViewState
import com.finter.india.model.employee.createemployee.EmployeeCreateViewState
import com.finter.india.model.employee.details.EmployeeDetailsViewState
import com.finter.india.repository.employee.activateinactive.EmployeeActiveInActiveRepository
import com.finter.india.repository.employee.createemployee.EmployeeCreateRepository
import com.finter.india.repository.employee.delete.EmployeeDeleteRepository
import com.finter.india.repository.employee.details.EmployeeDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeDetailsViewModel @ViewModelInject constructor(
    private val employeeDetailsRepository: EmployeeDetailsRepository,
    private val employeeActiveInActiveRepository: EmployeeActiveInActiveRepository,
    private val employeeDeleteRepository: EmployeeDeleteRepository,
    private val employeeCreateRepository: EmployeeCreateRepository
) : LiveCoroutinesViewModel() {

    private var _employeeDetailsResponse: MutableLiveData<EmployeeDetailsViewState> =
        MutableLiveData()
    private var _employeeDeleteResponse: MutableLiveData<EmployeeDetailsViewState> =
        MutableLiveData()
    private var _employeeActiveInActiveResponse: MutableLiveData<EmployeeActiveInActiveViewState> =
        MutableLiveData()
    private var _employeeCreateResponse: MutableLiveData<EmployeeCreateViewState> =
        MutableLiveData()
    private var gettingEmployeeId: MutableLiveData<String> = MutableLiveData()

    val toastLiveData: MutableLiveData<String> = MutableLiveData()
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var isVisibility: ObservableBoolean = ObservableBoolean(false)

    val employeeDetailsResponse: LiveData<EmployeeDetailsViewState>
        get() {
            return _employeeDetailsResponse
        }

    val employeeDeleteResponse: LiveData<EmployeeDetailsViewState>
        get() {
            return _employeeDeleteResponse
        }

    val employeeActiveInActiveResponse: LiveData<EmployeeActiveInActiveViewState>
        get() {
            return _employeeActiveInActiveResponse
        }

    val employeeCreateResponse: LiveData<EmployeeCreateViewState>
        get() {
            return _employeeCreateResponse
        }

    fun salesPersonDetails() {
        isLoading.set(true)
        isVisibility.set(false)
        _employeeDetailsResponse.value = EmployeeDetailsViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeDetailsRepository.employeeDetails(gettingEmployeeId.value.toString(),
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            isLoading.set(false)
                            isVisibility.set(true)
                            _employeeDetailsResponse.postValue(
                                EmployeeDetailsViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )
                        } else {
                            _employeeDetailsResponse.postValue(
                                EmployeeDetailsViewState(
                                    Status.FAIL,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        isLoading.set(false)
                        isVisibility.set(false)
                        _employeeDetailsResponse.postValue(
                            EmployeeDetailsViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    isVisibility.set(false)

                    _employeeDetailsResponse.postValue(
                        EmployeeDetailsViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun salesPersonDelete() {
        isLoading.set(true)
        isVisibility.set(true)
        _employeeDeleteResponse.value = EmployeeDetailsViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeDeleteRepository.deleteEmployee(gettingEmployeeId.value.toString(),
                onSuccess = {
                    if (it.status) {
                        _employeeDeleteResponse.postValue(
                            EmployeeDetailsViewState(
                                Status.SUCCESS,
                                it.message,
                                it
                            )
                        )
                    } else {
                        isLoading.set(false)
                        isVisibility.set(true)
                        _employeeDeleteResponse.postValue(
                            EmployeeDetailsViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    isVisibility.set(true)

                    _employeeDeleteResponse.postValue(
                        EmployeeDetailsViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun salesPersonActiveInActive(emp_id: String, isActive: String) {
        isLoading.set(true)
        isVisibility.set(true)
        _employeeActiveInActiveResponse.value = EmployeeActiveInActiveViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeActiveInActiveRepository.activeInActiveEmployee(emp_id,
                isActive,
                onSuccess = {
                    if (it.status) {
                        _employeeActiveInActiveResponse.postValue(
                            EmployeeActiveInActiveViewState(
                                Status.SUCCESS,
                                it.message,
                                it
                            )
                        )
                    } else {
                        isLoading.set(false)
                        isVisibility.set(true)
                        _employeeActiveInActiveResponse.postValue(
                            EmployeeActiveInActiveViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    isVisibility.set(true)

                    _employeeActiveInActiveResponse.postValue(
                        EmployeeActiveInActiveViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun salesPersonCreate(password: String) {
        isLoading.set(true)
        isVisibility.set(true)
        _employeeCreateResponse.value = EmployeeCreateViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeCreateRepository.employeeCreate(gettingEmployeeId.value.toString(),
                password,
                onSuccess = {
                    if (it.status) {
                        _employeeCreateResponse.postValue(
                            EmployeeCreateViewState(
                                Status.SUCCESS,
                                it.message,
                                it
                            )
                        )
                    } else {
                        isLoading.set(false)
                        isVisibility.set(true)
                        _employeeCreateResponse.postValue(
                            EmployeeCreateViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    isVisibility.set(true)

                    _employeeCreateResponse.postValue(
                        EmployeeCreateViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun getId(id: String) {
        gettingEmployeeId.value = id
    }
}