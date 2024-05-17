package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.IdHolder
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.EmployeeRepository
import br.com.apps.trucktech.TAG_DEBUG
import kotlinx.coroutines.launch

class BankPreviewViewModel(
    private val idHolder: IdHolder,
    private val repository: EmployeeRepository
) : ViewModel() {

    private val employeeId = idHolder.driverId
        ?: throw NullPointerException("BankPreviewViewModel: employeeId is null")

    private val bankId = idHolder.bankAccountId
        ?: throw NullPointerException("BankPreviewViewModel: bankId is null")

    /**
     * LiveData with a dark layer state, used when dialog is requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    /**
     * LiveData holding the response data of type [Response] with a [BankAccount]
     * to be displayed on screen.
     */
    private val _bankData = MutableLiveData<Response<BankAccount>>()
    val bankData get() = _bankData

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getBankAccountById(employeeId, bankId, EmployeeType.DRIVER).asFlow().collect {
                Log.d(TAG_DEBUG, "loadData")
                _bankData.postValue(it)
            }
        }
    }

    suspend fun delete() =
        liveData<Response<Unit>>(viewModelScope.coroutineContext) {
            try {
                repository.deleteBankAccount(employeeId, bankId, EmployeeType.DRIVER)
                emit(Response.Success())
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    /**
     * Sets the visibility of the [_darkLayer] to true, indicating that it should be shown.
     */
    fun requestDarkLayer() {
        _darkLayer.value = true
    }

    /**
     * Sets the visibility of the [_darkLayer] to false, indicating that it should be dismissed.
     */
    fun dismissDarkLayer() {
        _darkLayer.value = false
    }

}