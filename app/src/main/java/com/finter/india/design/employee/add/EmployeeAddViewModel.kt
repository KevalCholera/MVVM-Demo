package com.finter.india.design.employee.add

import androidx.databinding.*
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.finter.india.BR
import com.finter.india.base.LiveCoroutinesViewModel
import com.finter.india.design.employee.details.data.EmployeeDetailsSubData
import com.finter.india.model.Status
import com.finter.india.model.employee.add.EmployeeAddViewState
import com.finter.india.model.employee.details.EmployeeDetailsViewState
import com.finter.india.model.state.StateViewState
import com.finter.india.repository.employee.StateRepository
import com.finter.india.repository.employee.add.EmployeeAddRepository
import com.finter.india.repository.employee.add.EmployeeRemoveAttachmentRepository
import com.finter.india.repository.employee.details.EmployeeDetailsRepository
import com.finter.india.repository.employee.update.EmployeeUpdateRepository
import com.finter.india.utils.Constants
import com.finter.india.utils.SharedPreferenceUtil
import com.finter.india.utils.isValidEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EmployeeAddViewModel @ViewModelInject constructor(
    private val employeeAddRepository: EmployeeAddRepository,
    private val employeeDetailsRepository: EmployeeDetailsRepository,
    private val employeeUpdateRepository: EmployeeUpdateRepository,
    private val employeeRemoveAttachmentRepository: EmployeeRemoveAttachmentRepository,
    private val stateRepository: StateRepository,
    @Assisted
    private val savedStateHandle: SavedStateHandle,
    private val sharedPreferenceUtil: SharedPreferenceUtil
) : LiveCoroutinesViewModel(), Observable {

    private val propertyChangeRegistry = PropertyChangeRegistry()
    private var gettingSalesPersonId: MutableLiveData<String> = MutableLiveData()

    private var _salesPersonAddResponse: MutableLiveData<EmployeeAddViewState> =
        MutableLiveData()

    private var _deleteAttachmentResponse: MutableLiveData<EmployeeDetailsViewState> =
        MutableLiveData()

    private var _salesPersonUpdateDetailsResponse: MutableLiveData<EmployeeDetailsViewState> =
        MutableLiveData()

    private var _stateResponse: MutableLiveData<StateViewState> = MutableLiveData()

    val salesPersonAddResponse: LiveData<EmployeeAddViewState>
        get() {
            return _salesPersonAddResponse
        }

    val salesPersonUpdateDetailsResponse: LiveData<EmployeeDetailsViewState>
        get() {
            return _salesPersonUpdateDetailsResponse
        }

    val stateResponse: LiveData<StateViewState>
        get() {
            return _stateResponse
        }

    val deleteAttachmentResponse: LiveData<EmployeeDetailsViewState>
        get() {
            return _deleteAttachmentResponse
        }

    var name: ObservableField<String>? = ObservableField("")
    var sNo: ObservableField<String>? = ObservableField("")
    var Address1: ObservableField<String>? = ObservableField("")
    var Address2: ObservableField<String>? = ObservableField("")
    var CountryName: ObservableField<String>? = ObservableField("")
    var StateName: ObservableField<String>? = ObservableField("")
    var CityName: ObservableField<String>? = ObservableField("")
    var Pin: ObservableField<String>? = ObservableField("")
    var CountryCodeNo: ObservableField<String>? = ObservableField("")
    var Mobile: ObservableField<String>? = ObservableField("")
    var Email: ObservableField<String>? = ObservableField("")
    var govNo: ObservableField<String>? = ObservableField("")
    var stateTag: ObservableField<String> = ObservableField("")
    var stateCode: ObservableField<String> = ObservableField("")

    val toastLiveData: MutableLiveData<String> = MutableLiveData()
    var isLoading: ObservableBoolean = ObservableBoolean(false)

    init {
        setCountry("India")
        setCountryCode("+91")
    }

    @Bindable
    fun getSalesPersonName(): String? {
        return name?.get()
    }

    fun setSalesPersonName(BusinessName: String?) {
        this.name?.set(BusinessName)
        propertyChangeRegistry.notifyChange(this, BR.salesPersonName)
    }

    @Bindable
    fun getSalesPersonNo(): String? {
        return sNo?.get()
    }

    fun setSalesPersonNo(sNo: String?) {
        this.sNo?.set(sNo)
        propertyChangeRegistry.notifyChange(this, BR.salesPersonNo)
    }

    @Bindable
    fun getAddressLine1(): String? {
        return Address1?.get()
    }

    fun setAddressLine1(Address1: String?) {
        this.Address1?.set(Address1)
        propertyChangeRegistry.notifyChange(this, BR.addressLine1)
    }

    @Bindable
    fun getAddressLine2(): String? {
        return Address2?.get()
    }

    fun setAddressLine2(Address2: String?) {
        this.Address2?.set(Address2)
        propertyChangeRegistry.notifyChange(this, BR.addressLine2)
    }

    @Bindable
    fun getCountry(): String? {
        return CountryName?.get()
    }

    fun setCountry(Country: String?) {
        this.CountryName?.set(Country)
        propertyChangeRegistry.notifyChange(this, BR.country)
    }

    @Bindable
    fun getState(): String? {
        return StateName?.get()
    }

    fun setState(State: String?) {
        this.StateName?.set(State)
        propertyChangeRegistry.notifyChange(this, BR.state)
    }

    @Bindable
    fun getCity(): String? {
        return CityName?.get()
    }

    fun setCity(City: String?) {
        this.CityName?.set(City)
        propertyChangeRegistry.notifyChange(this, BR.city)
    }

    @Bindable
    fun getPinCode(): String? {
        return Pin?.get()
    }

    fun setPinCode(PinCode: String?) {
        this.Pin?.set(PinCode)
        propertyChangeRegistry.notifyChange(this, BR.pinCode)
    }

    @Bindable
    fun getCountryCode(): String? {
        return CountryCodeNo?.get()
    }

    fun setCountryCode(CountryCode: String?) {
        this.CountryCodeNo?.set(CountryCode)
        propertyChangeRegistry.notifyChange(this, BR.countryCode)
    }

    @Bindable
    fun getMobileNo(): String? {
        return Mobile?.get()
    }

    fun setMobileNo(MobileNo: String?) {
        this.Mobile?.set(MobileNo)
        propertyChangeRegistry.notifyChange(this, BR.mobileNo)
    }

    @Bindable
    fun getEmailId(): String? {
        return Email?.get()
    }

    fun setEmailId(EmailId: String?) {
        this.Email?.set(EmailId)
        propertyChangeRegistry.notifyChange(this, BR.emailId)
    }

    @Bindable
    fun getGovIdNo(): String? {
        return govNo?.get()
    }

    fun setGovIdNo(govNo: String?) {
        this.govNo?.set(govNo)
        propertyChangeRegistry.notifyChange(this, BR.govIdNo)
    }

    fun fillSalesPerson() {
        isLoading.set(true)
        _salesPersonUpdateDetailsResponse.value = EmployeeDetailsViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeDetailsRepository.employeeDetails(gettingSalesPersonId.value.toString(),
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            isLoading.set(false)
                            _salesPersonUpdateDetailsResponse.postValue(
                                EmployeeDetailsViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )
                        } else {
                            _salesPersonUpdateDetailsResponse.postValue(
                                EmployeeDetailsViewState(
                                    Status.FAIL,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        isLoading.set(false)
                        _salesPersonUpdateDetailsResponse.postValue(
                            EmployeeDetailsViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)

                    _salesPersonUpdateDetailsResponse.postValue(
                        EmployeeDetailsViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun onAddCreateSalesPersonClick() {
        when {
            getSalesPersonName() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Employee Name"
                    )
                )
                return
            }
            getMobileNo() == "" || getMobileNo()?.length!! < 10 -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Valid Mobile Number"
                    )
                )
                return
            }
            getEmailId().toString() != "" && !isValidEmail(
                getEmailId().toString()
            ) -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Valid Email ID"
                    )
                )
                return
            }
            getAddressLine1() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Residence Address"
                    )
                )
                return
            }
            getCity() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter City Name"
                    )
                )
                return
            }

            getState() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Select State"
                    )
                )
                return
            }
            getCountry() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Select Country"
                    )
                )
                return
            }
            getPinCode() == "" || getPinCode().toString().length < 6 -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Valid Pincode"
                    )
                )
                return
            }
            else -> {
                if (sharedPreferenceUtil.getString(
                        Constants.ACTIVITY_CUSTOMER,
                        ""
                    ) == Constants.Add_CUSTOMER
                )
                    addSalesPerson()
                else
                    updateSalesPerson()
            }
        }
    }

    private fun addSalesPerson() {
        isLoading.set(true)
        _salesPersonAddResponse.value = EmployeeAddViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeAddRepository.employeeAdd(
                getSalesPersonName().toString(),
                getSalesPersonNo().toString(),
                getAddressLine1().toString(),
                getAddressLine2().toString(),
                getCity().toString(),
                stateTag.get().toString(),
                getPinCode().toString(),
                "101",
                getMobileNo().toString(),
                getEmailId().toString(),
                getGovIdNo().toString(),
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            isLoading.set(false)
                            _salesPersonAddResponse.postValue(
                                EmployeeAddViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )
                        } else {
                            isLoading.set(false)
                            _salesPersonAddResponse.postValue(
                                EmployeeAddViewState(
                                    Status.EMPTY,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        isLoading.set(false)
                        _salesPersonAddResponse.postValue(
                            EmployeeAddViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    _salesPersonAddResponse.postValue(
                        EmployeeAddViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    private fun updateSalesPerson() {
        isLoading.set(true)
        _salesPersonAddResponse.value = EmployeeAddViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeUpdateRepository.employeeUpdate(
                gettingSalesPersonId.value.toString(),
                getSalesPersonName().toString(),
                getSalesPersonNo().toString(),
                getAddressLine1().toString(),
                getAddressLine2().toString(),
                getCity().toString(),
                stateTag.get().toString(),
                getPinCode().toString(),
                "101",
                getMobileNo().toString(),
                getEmailId().toString(),
                getGovIdNo().toString(),
                onSuccess = {
                    if (it.status) {
                        isLoading.set(false)
                        _salesPersonAddResponse.postValue(
                            EmployeeAddViewState(
                                Status.SUCCESS,
                                null,
                                it
                            )
                        )
                    } else {
                        isLoading.set(false)
                        _salesPersonAddResponse.postValue(
                            EmployeeAddViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    _salesPersonAddResponse.postValue(
                        EmployeeAddViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun onAddCreateSalesPersonClickWithAttachment(attachment: Array<MultipartBody.Part?>) {
        when {
            getSalesPersonName() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Employee Name"
                    )
                )
                return
            }
            getMobileNo() == "" || getMobileNo().toString().length < 10 -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Valid Mobile Number"
                    )
                )
                return
            }
            getEmailId() != "" && !isValidEmail(
                getEmailId()
            ) -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Valid Email ID"
                    )
                )
                return
            }
            getAddressLine1() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Residence Address"
                    )
                )
                return
            }
            getCity() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter City Name"
                    )
                )
                return
            }

            getState() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Select State"
                    )
                )
                return
            }
            getCountry() == "" -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Select Country"
                    )
                )
                return
            }
            getPinCode() == "" || getPinCode().toString().length < 6 -> {
                _salesPersonAddResponse.postValue(
                    EmployeeAddViewState(
                        Status.ERROR,
                        "Enter Valid Pincode"
                    )
                )
                return
            }
            else -> {
                if (sharedPreferenceUtil.getString(
                        Constants.ACTIVITY_CUSTOMER,
                        ""
                    ) == Constants.Add_CUSTOMER
                )
                    addSalesPersonWithAttachment(attachment)
                else
                    updateSalesPersonWithAttachment(attachment)
            }
        }
    }

    private fun addSalesPersonWithAttachment(attachment: Array<MultipartBody.Part?>) {
        isLoading.set(true)
        _salesPersonAddResponse.value = EmployeeAddViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeAddRepository.employeeAdd(
                getSalesPersonName().toString(),
                getSalesPersonNo().toString(),
                getAddressLine1().toString(),
                getAddressLine2().toString(),
                getCity().toString(),
                stateTag.get().toString(),
                getPinCode().toString(),
                "101",
                getMobileNo().toString(),
                getEmailId().toString(),
                getGovIdNo().toString(),
                attachment,
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            isLoading.set(false)
                            _salesPersonAddResponse.postValue(
                                EmployeeAddViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )
                        } else {
                            isLoading.set(false)
                            _salesPersonAddResponse.postValue(
                                EmployeeAddViewState(
                                    Status.EMPTY,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        isLoading.set(false)
                        _salesPersonAddResponse.postValue(
                            EmployeeAddViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    _salesPersonAddResponse.postValue(
                        EmployeeAddViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    private fun updateSalesPersonWithAttachment(attachment: Array<MultipartBody.Part?>) {
        isLoading.set(true)
        _salesPersonAddResponse.value = EmployeeAddViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeUpdateRepository.employeeUpdate(
                gettingSalesPersonId.value.toString(),
                getSalesPersonName().toString(),
                getSalesPersonNo().toString(),
                getAddressLine1().toString(),
                getAddressLine2().toString(),
                getCity().toString(),
                stateTag.get().toString(),
                getPinCode().toString(),
                "101",
                getMobileNo().toString(),
                getEmailId().toString(),
                getGovIdNo().toString(),
                attachment,
                onSuccess = {
                    if (it.status) {
                        isLoading.set(false)
                        _salesPersonAddResponse.postValue(
                            EmployeeAddViewState(
                                Status.SUCCESS,
                                null,
                                it
                            )
                        )
                    } else {
                        isLoading.set(false)
                        _salesPersonAddResponse.postValue(
                            EmployeeAddViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    _salesPersonAddResponse.postValue(
                        EmployeeAddViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun getStateList() {
        isLoading.set(true)
        _stateResponse.value = StateViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            stateRepository.stateList("101",
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            isLoading.set(false)
                            _stateResponse.postValue(
                                StateViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )
                        } else {
                            isLoading.set(false)
                            _stateResponse.postValue(
                                StateViewState(
                                    Status.EMPTY,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        isLoading.set(false)
                        _stateResponse.postValue(
                            StateViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    _stateResponse.postValue(
                        StateViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun deleteEmployeeAttachment(attachmentId: String, customerId: String) {
        isLoading.set(true)
        _deleteAttachmentResponse.value = EmployeeDetailsViewState(
            Status.LOADING,
            null,
            null
        )
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            employeeRemoveAttachmentRepository.deleteEmployeeAttachment(attachmentId, customerId,
                onSuccess = {
                    if (it.status) {
                        if (it.data != null) {
                            isLoading.set(false)
                            _deleteAttachmentResponse.postValue(
                                EmployeeDetailsViewState(
                                    Status.SUCCESS,
                                    null,
                                    it
                                )
                            )
                        } else {
                            isLoading.set(false)
                            _deleteAttachmentResponse.postValue(
                                EmployeeDetailsViewState(
                                    Status.EMPTY,
                                    "No Data Found"
                                )
                            )
                        }
                    } else {
                        isLoading.set(false)
                        _deleteAttachmentResponse.postValue(
                            EmployeeDetailsViewState(
                                Status.FAIL,
                                it.message
                            )
                        )
                    }
                },
                onError = {
                    isLoading.set(false)
                    _deleteAttachmentResponse.postValue(
                        EmployeeDetailsViewState(
                            Status.ERROR,
                            "Server Error"
                        )
                    )
                })
        }
    }

    fun setData(data: EmployeeDetailsSubData) {

        if (data.name != null)
            setSalesPersonName(data.name)
        else
            setSalesPersonName("")

        if (data.eid_no != null)
            setSalesPersonNo(data.eid_no)
        else
            setSalesPersonNo("")

        setCountryCode("+91")

        if (data.contact_no != null)
            setMobileNo(data.contact_no)
        else
            setMobileNo("")

        if (data.email != null)
            setEmailId(data.email)
        else
            setEmailId("")

        if (data.address_1 != null)
            setAddressLine1(data.address_1)
        else
            setAddressLine1("")

        if (data.address_2 != null)
            setAddressLine2(data.address_2)
        else
            setAddressLine2("")

        if (data.country_name != null)
            setCountry(data.country_name)
        else
            setCountry("")

        if (data.state_name != null)
            setState(data.state_name)
        else
            setState("")

        stateTag.set(data.state)

        if (data.city != null)
            setCity(data.city)
        else
            setCity("")

        if (data.id_no != null)
            setGovIdNo(data.id_no)
        else
            setGovIdNo("")

        if (data.pin_code != null)
            setPinCode(data.pin_code)
        else
            setPinCode("")
    }

    fun getContext(productId: String) {
        this.gettingSalesPersonId.value = productId
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.add(callback)
    }
}