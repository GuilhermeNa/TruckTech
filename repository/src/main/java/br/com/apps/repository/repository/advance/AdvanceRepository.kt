package br.com.apps.repository.repository.advance

class AdvanceRepository(private val read: AdvanceRead): AdvanceRepositoryI {

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun getAdvanceListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ) = read.getAdvanceListByEmployeeIdAndPaymentStatus(employeeId, isPaid, flow)

    override suspend fun getAdvanceListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ) = read.getAdvanceListByEmployeeIdsAndPaymentStatus(employeeIdList, isPaid, flow)

}

