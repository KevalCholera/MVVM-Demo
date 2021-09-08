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
import com.skydoves.sandwich.DataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PokedexService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): DataSource<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): DataSource<PokemonInfo>

    @FormUrlEncoded
    @POST("store-employee")
    suspend fun addEmployee(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Field("name") name: String,
        @Field("eid_no") eid_no: String,
        @Field("address_1") address_1: String,
        @Field("address_2") address_2: String,
        @Field("city") city: String,
        @Field("state_id") state_id: String,
        @Field("pin_code") pin_code: String,
        @Field("country_id") country_id: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("id_no") id_no: String
    ): DataSource<EmployeeAddResponse>

    @Multipart
    @POST("store-employee")
    suspend fun addEmployee(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Part("name") name: RequestBody,
        @Part("eid_no") eid_no: RequestBody,
        @Part("address_1") address_1: RequestBody,
        @Part("address_2") address_2: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state_id") state_id: RequestBody,
        @Part("pin_code") pin_code: RequestBody,
        @Part("country_id") country_id: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody,
        @Part("id_no") id_no: RequestBody,
        @Part attachment: Array<MultipartBody.Part?>
    ): DataSource<EmployeeAddResponse>

    @GET("employees")
    suspend fun employeeList(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Query("page") page: String,
        @Query("search") search: String
    ): DataSource<EmployeeResponse>

    @GET("delete-employee/{employee_id}")
    suspend fun deleteEmployee(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Path("employee_id") product_id: String
    ): DataSource<EmployeeDetailsResponse>

    @FormUrlEncoded
    @POST("active-inactive-accountant")
    suspend fun activeInActiveEmployee(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Field("user_id") user_id: String,
        @Field("is_active") is_active: String
    ): DataSource<EmployeeActiveInActiveResponse>

    @FormUrlEncoded
    @POST("create-user")
    suspend fun employeeCreate(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Field("emp_id") emp_id: String,
        @Field("password") password: String
    ): DataSource<EmployeeCreateResponse>

    @GET("employee/{employee_id}")
    suspend fun viewEmployee(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Path("employee_id") product_id: String
    ): DataSource<EmployeeDetailsResponse>

    @FormUrlEncoded
    @POST("update-employee/{employee_id}")
    suspend fun updateEmployee(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Path("employee_id") product_id: String,
        @Field("name") name: String,
        @Field("eid_no") eid_no: String,
        @Field("address_1") address_1: String,
        @Field("address_2") address_2: String,
        @Field("city") city: String,
        @Field("state_id") state_id: String,
        @Field("pin_code") pin_code: String,
        @Field("country_id") country_id: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("id_no") id_no: String
    ): DataSource<EmployeeAddResponse>

    @Multipart
    @POST("update-employee/{employee_id}")
    suspend fun updateEmployee(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Path("employee_id") product_id: String,
        @Part("name") name: RequestBody,
        @Part("eid_no") eid_no: RequestBody,
        @Part("address_1") address_1: RequestBody,
        @Part("address_2") address_2: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state_id") state_id: RequestBody,
        @Part("pin_code") pin_code: RequestBody,
        @Part("country_id") country_id: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody,
        @Part("id_no") id_no: RequestBody,
        @Part attachment: Array<MultipartBody.Part?>
    ): DataSource<EmployeeAddResponse>

    @FormUrlEncoded
    @POST("remove-employee-attachment")
    suspend fun deleteEmployeeAttachment(
        @Header("Authorization") Authorization: String,
        @Header("token") token: String,
        @Header("financialYear") financialYear: String,
        @Header("deviceId") deviceId: String,
        @Field("id") id: String,
        @Field("emp_id") emp_id: String
    ): DataSource<EmployeeDetailsResponse>

    @GET("states/{country_id}")
    suspend fun stateList(@Path("country_id") country_id: String): DataSource<StateResponse>
}