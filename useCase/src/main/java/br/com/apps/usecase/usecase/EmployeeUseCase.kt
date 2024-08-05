package br.com.apps.usecase.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.enums.WorkRole
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.util.Resource
import br.com.apps.repository.util.Response
import br.com.apps.repository.util.WriteRequest
import br.com.apps.repository.util.validateAndProcess
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
        type: WorkRole
    ): LiveData<Response<List<BankAccount>>> {
        return repository.getEmployeeBankAccounts(id, type)
    }

    suspend fun updateMainAccount(employeeId: String, oldMainAccId: String?, newMainAccId: String, type: WorkRole) {
        return repository.updateMainAccount(employeeId, oldMainAccId, newMainAccId, type)
    }

    suspend fun saveBankAccount(writeReq: WriteRequest<BankAccountDto>, driver: WorkRole) {
        val dto = writeReq.data

        validateAndProcess (
            validateData = { dto.validateDataForDbInsertion() }
        ).let {
            repository.saveBankAccount(dto, WorkRole.TRUCK_DRIVER)
        }
    }

}