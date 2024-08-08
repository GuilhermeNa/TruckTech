package br.com.apps.repository.repository.loan

class LoanRepository(private val read: LoanReadImpl) : LoanRepositoryInterface {

    override suspend fun fetchLoanListByEmployeeId(id: String, flow: Boolean) =
        read.fetchLoanListByEmployeeId(id, flow)

    override suspend fun fetchLoanListByEmployeeIds(ids: List<String>, flow: Boolean) =
        read.fetchLoanListByEmployeeIds(ids, flow)

    override suspend fun fetchLoanById(id: String, flow: Boolean) =
        read.fetchLoanById(id, flow)

    override suspend fun fetchLoanByIds(ids: List<String>, flow: Boolean) =
        read.fetchLoanByIds(ids, flow)

}