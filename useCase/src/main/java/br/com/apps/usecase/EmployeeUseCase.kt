package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.Resource
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.util.Response
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

    suspend fun getEmployeeBankAccountsList(
        id: String,
        type: EmployeeType
    ): LiveData<Response<List<BankAccount>>> {
        return repository.getEmployeeBankAccounts(id, type)
    }

    suspend fun updateMainAccount(employeeId: String, oldMainAccId: String?, newMainAccId: String, type: EmployeeType) {
        return repository.updateMainAccount(employeeId, oldMainAccId, newMainAccId, type)
    }

}