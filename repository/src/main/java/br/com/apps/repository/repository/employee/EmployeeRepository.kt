package br.com.apps.repository.repository.employee

import br.com.apps.model.dto.employee_dto.BankAccountDto
import br.com.apps.model.dto.employee_dto.EmployeeDto
import br.com.apps.model.enums.WorkRole

class EmployeeRepository(
    private val write: EmployeeWriteImpl,
    private val read: EmployeeReadImpl
) : EmployeeRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // WRITE
    //---------------------------------------------------------------------------------------------//

    override suspend fun save(dto: EmployeeDto) = write.save(dto)

    override suspend fun delete(id: String, type: WorkRole) = write.delete(id, type)

    override suspend fun deleteBankAcc(employeeId: String, bankId: String, type: WorkRole) =
        write.deleteBankAcc(employeeId, bankId, type)

    override suspend fun saveBankAccount(bankAccDto: BankAccountDto, type: WorkRole) =
        write.saveBankAccount(bankAccDto, type)

    override suspend fun updateMainAccount(
        employeeId: String,
        oldMainAccId: String?,
        newMainAccId: String,
        type: WorkRole
    ) = write.updateMainAccount(employeeId, oldMainAccId, newMainAccId, type)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchEmployeeListByMasterUid(
        masterUid: String,
        flow: Boolean
    ) = read.fetchEmployeeListByMasterUid(masterUid, flow)

    override suspend fun fetchById(
        id: String,
        type: WorkRole,
        flow: Boolean
    ) = read.fetchById(id, type, flow)

    override suspend fun getEmployeeBankAccounts(id: String, type: WorkRole, flow: Boolean) =
        read.getEmployeeBankAccounts(id, type, flow)

    override suspend fun getBankAccountById(
        employeeId: String,
        bankId: String,
        type: WorkRole,
        flow: Boolean
    ) = read.getBankAccountById(employeeId, bankId, type, flow)

}