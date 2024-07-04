package br.com.apps.repository.repository.loan

class LoanRepository(private val read: LoanReadImpl): LoanRepositoryInterface {

    override suspend fun getLoanListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ) = read.getLoanListByEmployeeIdAndPaymentStatus(employeeId, isPaid, flow)

    override suspend fun getLoanListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ) = read.getLoanListByEmployeeIdsAndPaymentStatus(employeeIdList, isPaid, flow)

}