package com.finter.india.design.employee.list

import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.finter.india.base.LiveCoroutinesViewModel
import com.finter.india.model.Status
import com.finter.india.model.employee.list.EmployeeViewState
import com.finter.india.repository.employee.list.EmployeeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeViewModel @ViewModelInject constructor(
    private val employeeRepository: EmployeeRepository
) : LiveCoroutinesViewModel() {

    private var _salesPersonResponse: MutableLiveData<EmployeeViewState> = MutableLiveData()

    val toastLiveData: MutableLiveData<String> = MutableLiveData()
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var newDataLoading: ObservableBoolean = ObservableBoolean(false)
    var cvNoDataVisibility: ObservableBoolean = ObservableBoolean(false)
    var cvNoSearchVisibility: ObservableBoolean = ObservableBoolean(false)
    var rvVisibility: ObservableBoolean = ObservableBoolean(false)

    val salesPersonResponse: LiveData<EmployeeViewState>
        get() {
            return _salesPersonResponse
        }

    fun fetchSalesPersonData(page: String, search: String) {
        isLoading.set(true)
        rvVisibility.set(false)
        cvNoDataVisibility.set(false)
        cvNoSearchVisibility.set(false)

        _salesPersonResponse.value =
            EmployeeViewState(
                Status.LOADING,
                null,
                null
            )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeRepository.employeeList(page, search,
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            isLoading.set(false)

                            rvVisibility.set(true)
                            cvNoDataVisibility.set(false)
                            cvNoSearchVisibility.set(false)

                            _salesPersonResponse.postValue(
                                EmployeeViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )

                        } else {
                            isLoading.set(false)
                            rvVisibility.set(false)
                            cvNoDataVisibility.set(true)
                            cvNoSearchVisibility.set(false)

                            _salesPersonResponse.postValue(
                                EmployeeViewState(
                                    Status.EMPTY,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        isLoading.set(false)
                        rvVisibility.set(false)
                        cvNoDataVisibility.set(true)
                        cvNoSearchVisibility.set(false)

                        _salesPersonResponse.postValue(
                            EmployeeViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    rvVisibility.set(false)
                    cvNoDataVisibility.set(true)
                    cvNoSearchVisibility.set(false)

                    _salesPersonResponse.postValue(
                        EmployeeViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun updateSalesPersonList(page: String, search: String) {
        newDataLoading.set(true)
        rvVisibility.set(true)
        cvNoDataVisibility.set(false)
        cvNoSearchVisibility.set(false)

        _salesPersonResponse.value =
            EmployeeViewState(
                Status.LOADING,
                null,
                null
            )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeRepository.employeeList(page, search,
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            newDataLoading.set(false)
                            rvVisibility.set(true)
                            cvNoDataVisibility.set(false)
                            cvNoSearchVisibility.set(false)

                            _salesPersonResponse.postValue(
                                EmployeeViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )

                        } else {
                            newDataLoading.set(false)
                            rvVisibility.set(true)
                            cvNoDataVisibility.set(false)
                            cvNoSearchVisibility.set(false)

                            _salesPersonResponse.postValue(
                                EmployeeViewState(
                                    Status.EMPTY,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        newDataLoading.set(false)
                        rvVisibility.set(true)
                        cvNoDataVisibility.set(false)
                        cvNoSearchVisibility.set(false)

                        _salesPersonResponse.postValue(
                            EmployeeViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    newDataLoading.set(false)
                    rvVisibility.set(true)
                    cvNoDataVisibility.set(false)
                    cvNoSearchVisibility.set(false)

                    _salesPersonResponse.postValue(
                        EmployeeViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }
}