package br.com.apps.trucktech.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.user.CommonUser
import br.com.apps.usecase.FleetUseCase
import br.com.apps.usecase.UserUseCase
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userUseCase: UserUseCase,
    private val truckUseCase: FleetUseCase
) : ViewModel() {

    /**
     * User
     */
    private var _userData: MutableLiveData<DriverAndTruck> = MutableLiveData()
    val userData get() = _userData

    /**
     * Components
     */
    private var _components: MutableLiveData<VisualComponents> = MutableLiveData()
    val components get() = _components

    /**
     * Define components view
     */
    fun setComponents(components: VisualComponents) {
        _components.value = components
    }

    /**
     * Search for user data
     */
    fun loadUserData(uid: String) {
        val driverAndTruck = DriverAndTruck()
        viewModelScope.launch {
            userUseCase.getById(uid, EmployeeType.DRIVER).asFlow().collect { user ->
                driverAndTruck.user = user as CommonUser
                user.employeeId?.let {
                    truckUseCase.getByDriverId(user.employeeId!!).asFlow().collect { truck ->
                        driverAndTruck.truck = truck
                        _userData.value = driverAndTruck
                    }
                }
            }
        }
    }

}

class VisualComponents(val hasBottomNavigation: Boolean = false)

data class DriverAndTruck(
    var user: CommonUser? = null,
    var truck: Truck? = null
)