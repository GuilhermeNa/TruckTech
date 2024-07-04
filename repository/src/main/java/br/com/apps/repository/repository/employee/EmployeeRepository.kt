package br.com.apps.repository.repository.employee

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.model.employee.EmployeeType

class EmployeeRepository(
    private val write: EmployeeWriteImpl,
    private val read: EmployeeReadImpl
) : EmployeeRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: EmployeeDto) = write.save(dto)

    override suspend fun delete(id: String, type: EmployeeType) = write.delete(id, type)

    override suspend fun deleteBankAcc(employeeId: String, bankId: String, type: EmployeeType) =
        write.deleteBankAcc(employeeId, bankId, type)

    override suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: EmployeeType) =
        write.saveBankAccount(bankAccDto, type)

    override suspend fun updateMainAccount(
        employeeId: String,
        oldMainAccId: String?,
        newMainAccId: String,
        type: EmployeeType
    ) = write.updateMainAccount(employeeId, oldMainAccId, newMainAccId, type)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getEmployeeListByMasterUid(
        masterUid: String,
        flow: Boolean
    ) = read.getEmployeeListByMasterUid(masterUid, flow)

    override suspend fun getById(
        id: String,
        type: EmployeeType,
        flow: Boolean
    ) = read.getById(id, type, flow)

    override suspend fun getEmployeeBankAccounts(id: String, type: EmployeeType, flow: Boolean) =
        read.getEmployeeBankAccounts(id, type, flow)

    override suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: EmployeeType,
        flow: Boolean
    ) = read.getBankAccountById(employeeId, bankId, type, flow)

}