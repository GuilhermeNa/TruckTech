package br.com.apps.repository.repository.payable

import androidx.lifecycle.LiveData
import br.com.apps.model.enums.EmployeePayableTicket
import br.com.apps.model.model.finance.payable.Payable
import br.com.apps.repository.util.Response

interface EmployeePayableInterface : EmployeePayableReadInterface

interface EmployeePayableReadInterface {

    suspend fun fetchPayablesByParentId(id: String, type: EmployeePayableTicket, flow: Boolean = false)
            : LiveData<Response<List<Payable>>>

    suspend fun fetchPayablesByEmployeeIdAndStatus(id: String, isPaid: Boolean, flow: Boolean = false)
            : LiveData<Response<List<Payable>>>

}