package br.com.apps.trucktech.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.user.CommonUser
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.fleet.FleetRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.UserUseCase
import kotlinx.coroutines.flow.first
import java.math.BigDecimal

class MainActivityViewModel(
    private val userUseCase: UserUseCase,
    private val truckRepository: FleetRepository
) : ViewModel() {

    lateinit var loggedUser: LoggedUser

    /**
     * Components
     */
    private var _components: MutableLiveData<VisualComponents> = MutableLiveData()
    val components get() = _components

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    /**
     * Define components view
     */
    fun setComponents(components: VisualComponents) {
        _components.value = components
    }

    /**
     * Load user data
     */
    fun initUserData(userId: String) =
        liveData(viewModelScope.coroutineContext) {
            try {
                val user = fetchUser(userId)
                val truck = fetchTruck(user.employeeId)
                val loggedUser = processData(user, truck)

                emit(Response.Success(loggedUser))
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }

    private suspend fun fetchUser(userId: String): CommonUser {
        val response = userUseCase.getById(userId, EmployeeType.DRIVER).asFlow().first()
        return response as CommonUser
    }

    private suspend fun fetchTruck(driverId: String): Truck {
        val response = truckRepository.getTruckByDriverId(driverId).asFlow().first()

        return when (response) {
            is Response.Error -> throw response.exception
            is Response.Success -> response.data ?: throw NullPointerException("Truck data is null")
        }
    }

    private fun processData(user: CommonUser, truck: Truck): LoggedUser {
        val parts = user.name.split(" ")
        val name = parts[0]
        val surname = if (parts.size > 1) " ${parts[1]}" else ""

        loggedUser =
            LoggedUser(
                masterUid = truck.masterUid,
                driverId = truck.driverId,
                truckId = truck.id!!,
                uid = user.uid,
                orderCode = user.orderCode,
                orderNumber = user.orderNumber,
                name = ("$name$surname"),
                plate = truck.plate,
                email = user.email,
                urlImage = user.urlImage,
                permissionLevelType = user.permission,
                commissionPercentual = truck.commissionPercentual
            )

        return loggedUser
    }

}

class VisualComponents(val hasBottomNavigation: Boolean = false)

data class LoggedUser(
    val masterUid: String,
    val uid: String,
    val driverId: String,
    val truckId: String,

    val orderCode: Int,
    val orderNumber: Int,
    val name: String,
    val plate: String,
    val email: String,
    val urlImage: String? = null,
    val permissionLevelType: PermissionLevelType,
    val commissionPercentual: BigDecimal
)