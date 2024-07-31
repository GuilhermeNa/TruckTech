package br.com.apps.repository.repository.loan

class LoanRepository(private val read: LoanReadImpl): LoanRepositoryInterface {

    override suspend fun fetchLoanListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchLoanListByEmployeeIdAndPaymentStatus(employeeId, isPaid, flow)

    override suspend fun fetchLoanListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchLoanListByEmployeeIdsAndPaymentStatus(employeeIdList, isPaid, flow)

}