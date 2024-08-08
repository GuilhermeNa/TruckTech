package br.com.apps.repository.repository.employee

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

    override suspend fun delete(id: String) = write.delete(id)

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchById(id: String, flow: Boolean) = read.fetchById(id, flow)

    override suspend fun fetchEmployeesByMasterUidAndRole(
        masterUid: String,
        role: WorkRole,
        flow: Boolean
    ) = read.fetchEmployeesByMasterUidAndRole(masterUid, role, flow)

}