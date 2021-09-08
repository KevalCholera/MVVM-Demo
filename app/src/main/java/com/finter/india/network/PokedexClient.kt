package com.finter.india.network

import com.finter.india.model.PokemonInfo
import com.finter.india.model.PokemonResponse
import com.finter.india.model.employee.activeinactive.EmployeeActiveInActiveResponse
import com.finter.india.model.employee.add.EmployeeAddResponse
import com.finter.india.model.employee.createemployee.EmployeeCreateResponse
import com.finter.india.model.employee.details.EmployeeDetailsResponse
import com.finter.india.model.employee.list.EmployeeResponse
import com.finter.india.model.state.StateResponse
import com.finter.india.utils.Constants
import com.finter.india.utils.SharedPreferenceUtil
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PokedexClient @Inject constructor(
    private val sharedPreferenceUtil: SharedPreferenceUtil,
    private val pokedexService: PokedexService
) {

    suspend fun fetchPokemonList(
        page: Int,
        onResult: (response: ApiResponse<PokemonResponse>) -> Unit
    ) {
        pokedexService.fetchPokemonList(
            limit = PAGING_SIZE,
            offset = page * PAGING_SIZE
        ).toResponseDataSource()
            .retry(RETRY_COUNT, RETRY_DELAY)
            .request(onResult)
    }

    suspend fun fetchPokemonInfo(
        name: String,
        onResult: (response: ApiResponse<PokemonInfo>) -> Unit
    ) {
        pokedexService.fetchPokemonInfo(
            name = name
        ).toResponseDataSource()
            .retry(RETRY_COUNT, RETRY_DELAY)
            .request(onResult)
    }

    suspend fun employeeList(
        page: String,
        search: String,
        onResult: (response: ApiResponse<EmployeeResponse>) -> Unit
    ) {
        pokedexService.employeeList(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            page, search
        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun employeeDetails(
        employee_id: String,
        onResult: (response: ApiResponse<EmployeeDetailsResponse>) -> Unit
    ) {
        pokedexService.viewEmployee(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            employee_id

        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun deleteEmployee(
        employee_id: String,
        onResult: (response: ApiResponse<EmployeeDetailsResponse>) -> Unit
    ) {
        pokedexService.deleteEmployee(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            employee_id
        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun activeInActiveEmployee(
        user_id: String,
        is_active: String,
        onResult: (response: ApiResponse<EmployeeActiveInActiveResponse>) -> Unit
    ) {
        pokedexService.activeInActiveEmployee(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            user_id,
            is_active
        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun employeeCreate(
        emp_id: String,
        password: String,
        onResult: (response: ApiResponse<EmployeeCreateResponse>) -> Unit
    ) {
        pokedexService.employeeCreate(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            emp_id,
            password
        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun employeeAdd(
        name: String,
        employee_no: String,
        address_1: String,
        address_2: String,
        city: String,
        state: String,
        pin_code: String,
        country: String,
        contact_no: String,
        email: String,
        id_no: String,
        onResult: (response: ApiResponse<EmployeeAddResponse>) -> Unit
    ) {

        pokedexService.addEmployee(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            name,
            employee_no,
            address_1,
            address_2,
            city,
            state,
            pin_code,
            country,
            contact_no,
            email,
            id_no
        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun employeeAdd(
        name: String,
        employee_no: String,
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
        onResult: (response: ApiResponse<EmployeeAddResponse>) -> Unit
    ) {
        pokedexService.addEmployee(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            name.toRequestBody(),
            employee_no.toRequestBody(),
            address_1.toRequestBody(),
            address_2.toRequestBody(),
            city.toRequestBody(),
            state.toRequestBody(),
            pin_code.toRequestBody(),
            country.toRequestBody(),
            contact_no.toRequestBody(),
            email.toRequestBody(),
            id_no.toRequestBody(),
            attachment
        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun employeeUpdate(
        employee_id: String,
        name: String,
        employee_no: String,
        address_1: String,
        address_2: String,
        city: String,
        state: String,
        pin_code: String,
        country: String,
        contact_no: String,
        email: String,
        id_no: String,
        onResult: (response: ApiResponse<EmployeeAddResponse>) -> Unit
    ) {
        pokedexService.updateEmployee(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            employee_id,
            name,
            employee_no,
            address_1,
            address_2,
            city,
            state,
            pin_code,
            country,
            contact_no,
            email,
            id_no

        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun employeeUpdate(
        employee_id: String,
        name: String,
        employee_no: String,
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
        onResult: (response: ApiResponse<EmployeeAddResponse>) -> Unit
    ) {
        pokedexService.updateEmployee(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            employee_id,
            name.toRequestBody(),
            employee_no.toRequestBody(),
            address_1.toRequestBody(),
            address_2.toRequestBody(),
            city.toRequestBody(),
            state.toRequestBody(),
            pin_code.toRequestBody(),
            country.toRequestBody(),
            contact_no.toRequestBody(),
            email.toRequestBody(),
            id_no.toRequestBody(),
            attachment

        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun deleteEmployeeAttachment(
        id: String,
        employee_id: String,
        onResult: (response: ApiResponse<EmployeeDetailsResponse>) -> Unit
    ) {
        pokedexService.deleteEmployeeAttachment(
            "Bearer " + sharedPreferenceUtil.getString(Constants.ACCOUNT_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.FIRM_TOKEN, "").toString(),
            sharedPreferenceUtil.getString(Constants.CURRENT_YEAR_SERVER, "").toString(),
            sharedPreferenceUtil.getString(Constants.DEVICE_ID, "").toString(),
            id,
            employee_id
        ).toResponseDataSource()
            .request(onResult)
    }

    suspend fun stateList(
        countryId: String,
        onResult: (response: ApiResponse<StateResponse>) -> Unit
    ) {
        pokedexService.stateList(
            countryId
        ).toResponseDataSource()
            .request(onResult)
    }


    companion object {
        private const val PAGING_SIZE = 20
        private const val RETRY_COUNT = 3
        private const val RETRY_DELAY = 7000L
    }
}