package br.com.apps.repository.repository.payable

import br.com.apps.model.enums.EmployeePayableTicket

class EmployeePayableRepository(private val read: EmployeePayableReadImpl) : EmployeePayableInterface {

    override suspend fun fetchPayablesByParentId(
        id: String,
        type: EmployeePayableTicket,
        flow: Boolean
    ) = read.fetchPayablesByParentId(id, type, flow)

    override suspend fun fetchPayablesByEmployeeIdAndStatus(
        id: String,
        isPaid: Boolean,
        flow: Boolean
    ) = read.fetchPayablesByEmployeeIdAndStatus(id, isPaid, flow)

}