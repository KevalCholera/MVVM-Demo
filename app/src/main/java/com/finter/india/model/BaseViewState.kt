package com.finter.india.model

open class BaseViewState(val baseStatus: Status, val baseError: String?) {
    fun isLoading() = baseStatus == Status.LOADING
    fun isEmpty() = baseStatus == Status.FAIL
    fun isSuccess() = baseStatus == Status.SUCCESS
    fun getErrorMessage() = baseError
    fun shouldShowErrorMessage() = baseError != null
}