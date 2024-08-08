package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.util.Resource
import java.security.InvalidParameterException

class EmployeeUseCase(
    private val authUseCase: AuthenticationUseCase,
    private val repository: EmployeeRepository
) {

    /**
     * Add a new Employee
     */
    suspend fun save(employeeDto: EmployeeDto): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()

        try {
            repository.save(employeeDto)
            liveData.value = Resource(data = true)
        } catch (e: InvalidParameterException) {
            liveData.value = Resource(data = false, error = "Funcionário invalido")
        }

        return liveData
    }



}