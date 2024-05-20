package br.com.apps.repository.repository.employee

import androidx.lifecycle.LiveData
import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.model.model.employee.Employee
import br.com.apps.model.model.employee.EmployeeType
import br.com.apps.repository.Resource
import br.com.apps.repository.util.Response

class EmployeeRepository(
    private val write: EmployeeWrite,
    private val read: EmployeeRead
): EmployeeRepositoryI {

    override suspend fun save(dto: EmployeeDto) = write.save(dto)

    override suspend fun delete(id: String, type: EmployeeType) = write.delete(id, type)

    override suspend fun deleteBankAcc(employeeId: String, bankId: String, type: EmployeeType) {
        TODO("Not yet implemented")
    }

    override suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: EmployeeType) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMainAccount(
        employeeId: String,
        oldMainAccId: String?,
        newMainAccId: String,
        type: EmployeeType
    ) {
        TODO("Not yet implemented")
    }

    override fun getAll(masterUid: String): LiveData<Resource<List<Employee>>> {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, type: EmployeeType): LiveData<Resource<Employee>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEmployeeBankAccounts(
        id: String,
        type: EmployeeType
    ): LiveData<Response<List<BankAccount>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: EmployeeType,
        flow: Boolean
    ): LiveData<Response<BankAccount>> {
        TODO("Not yet implemented")
    }


}