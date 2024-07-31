package br.com.apps.repository.repository.advance

class AdvanceRepository(private val read: AdvanceReadImpl): AdvanceRepositoryInterface {

    //---------------------------------------------------------------------------------------------//
    // READ
    //---------------------------------------------------------------------------------------------//

    override suspend fun fetchAdvanceListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchAdvanceListByEmployeeIdAndPaymentStatus(employeeId, isPaid, flow)

    override suspend fun fetchAdvanceListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchAdvanceListByEmployeeIdsAndPaymentStatus(employeeIdList, isPaid, flow)

}

