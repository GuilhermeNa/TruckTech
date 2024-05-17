package br.com.apps.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.UserCredentials
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.Resource
import br.com.apps.repository.util.Response
import br.com.apps.repository.repository.EmployeeRepository
import java.security.InvalidParameterException

class EmployeeUseCase(
    private val authUseCase: AuthenticationUseCase,
    private val repository: EmployeeRepository
) {

    fun authenticateANewEmployee(credentials: UserCredentials, employeeId: String):
            LiveData<Resource<Boolean>> {
        return authUseCase.authenticateAnExistingUser(credentials, employeeId)
    }

    /**
     * Add a new Employee
     */
    fun save(employeeDto: EmployeeDto): LiveData<Resource<Boolean>> {
        val liveData = MutableLiveData<Resource<Boolean>>()

        try {
            repository.save(employeeDto)
            liveData.value = Resource(data = true)
        } catch (e: InvalidParameterException) {
            liveData.value = Resource(data = false, error = "Funcionário invalido")
        }

        return liveData
    }

    fun getById(id: String, type: EmployeeType): LiveData<Resource<Employee>> {
        return repository.getById(id, type)
    }

    fun getAll(masterUid: String): LiveData<Resource<List<Employee>>> {
        return repository.getAll(masterUid)
    }

    fun deleteEmployee(id: String, type: EmployeeType): LiveData<Resource<Boolean>> {
        return repository.deleteEmployee(id, type)
    }

    suspend fun getEmployeeBankAccountsList(
        id: String,
        type: EmployeeType
    ): LiveData<Response<List<BankAccount>>> {
        return repository.getEmployeeBankAccounts(id, type)
    }

    suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: EmployeeType
    ): LiveData<Response<BankAccount>> {
        return repository.getBankAccountById(employeeId, bankId, type)
    }

    suspend fun deleteBankAccount(employeeId: String, bankId: String, type: EmployeeType) {
        repository.deleteBankAccount(employeeId, bankId, type)
    }

     suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: EmployeeType) {
        repository.saveBankAccount(bankAccDto, type)
    }

    suspend fun updateMainAccount(employeeId: String, oldMainAccId: String?, newMainAccId: String, type: EmployeeType) {
        return repository.updateMainAccount(employeeId, oldMainAccId, newMainAccId, type)
    }

}