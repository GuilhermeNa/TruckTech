package br.com.apps.trucktech.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.apps.model.model.Truck
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.model.model.user.CommonUser
import br.com.apps.model.model.user.PermissionLevelType
import br.com.apps.repository.repository.fleet.FleetRepository
import br.com.apps.repository.util.Response
import br.com.apps.usecase.UserUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModel(
    private val userUseCase: UserUseCase,
    private val truckRepository: FleetRepository
) : ViewModel() {

    lateinit var loggedUser: LoggedUser

    /**
     * User
     */
    private var _userData = MutableLiveData<Response<LoggedUser>>()
    val userData get() = _userData

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
    fun loadUserData(userId: String) {
        viewModelScope.launch {
            val userDef = loadUser(userId)
            userDef.await()
            val user = userDef.getCompleted()

            val truckDef = loadTruck(user.employeeId)
            truckDef.await()
            val truck = truckDef.getCompleted()

            loggedUser = LoggedUser(
                masterUid = truck.masterUid,
                driverId = truck.driverId,
                truckId = truck.id!!,

                name = user.name,
                plate = truck.plate,
                email = user.email,
                urlImage = user.urlImage,
                permissionLevelType = user.permission
            )

            _userData.value = Response.Success(loggedUser)
        }
    }

    private suspend fun loadUser(userId: String): CompletableDeferred<CommonUser> {
        val userDef = CompletableDeferred<CommonUser>()
        userUseCase.getById(userId, EmployeeType.DRIVER).asFlow().first {
            val user = it as CommonUser
            user.employeeId
            userDef.complete(user)
            true
        }
        return userDef
    }

    private suspend fun loadTruck(driverId: String): CompletableDeferred<Truck> {
        val truckDef = CompletableDeferred<Truck>()
        truckRepository.getTruckByDriverId(driverId).asFlow().first { response ->
            when (response) {
                is Response.Error -> _userData.value = Response.Error(response.exception)
                is Response.Success -> response.data?.let { truckDef.complete(it) }
            }
            true
        }
        return truckDef
    }

}

class VisualComponents(val hasBottomNavigation: Boolean = false)

//TODO parei aqui refazendo o usuario logado para ser salvo em cache na act viewModel
data class DriverAndTruck(
    var user: CommonUser? = null,
    var truck: Truck? = null
)

data class LoggedUser(
    val masterUid: String,
    val driverId: String,
    val truckId: String,

    val name: String,
    val plate: String,
    val email: String,
    val urlImage: String? = null,
    val permissionLevelType: PermissionLevelType
)