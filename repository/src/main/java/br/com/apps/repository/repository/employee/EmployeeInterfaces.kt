package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.Resource
import br.com.apps.repository.util.Response

interface EmployeeRepositoryI : EmployeeWriteI, EmployeeReadI

interface EmployeeWriteI {

    suspend fun save(dto: EmployeeDto): String

    suspend fun delete(id: String, type: EmployeeType)

    suspend fun deleteBankAcc(employeeId: String, bankId: String, type: EmployeeType)

    suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: EmployeeType)

    suspend fun updateMainAccount(
        employeeId: String,
        oldMainAccId: String?,
        newMainAccId: String,
        type: EmployeeType
    )

}

interface EmployeeReadI {

    fun getAll(masterUid: String): LiveData<Resource<List<Employee>>>

    fun getById(id: String, type: EmployeeType): LiveData<Resource<Employee>>

    suspend fun getEmployeeBankAccounts(id: String, type: EmployeeType)
            : LiveData<Response<List<BankAccount>>>

    suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: EmployeeType,
        flow: Boolean = false
    ): LiveData<Response<BankAccount>>

}