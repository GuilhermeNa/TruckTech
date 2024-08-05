package br.com.apps.repository.repository.receivable

import br.com.apps.model.enums.EmployeeReceivableTicket

class EmployeeReceivableRepository(private val read: EmployeeReceivableReadImpl) : EmployeeReceivableInterface {

    override suspend fun fetchReceivablesByParentId(
        id: String,
        type: EmployeeReceivableTicket,
        flow: Boolean
    ) = read.fetchReceivablesByParentId(id, type, flow)

    override suspend fun fetchReceivableByEmployeeIdAndStatus(
        id: String,
        isReceived: Boolean,
        flow: Boolean
    ) = read.fetchReceivableByEmployeeIdAndStatus(id, isReceived, flow)

}