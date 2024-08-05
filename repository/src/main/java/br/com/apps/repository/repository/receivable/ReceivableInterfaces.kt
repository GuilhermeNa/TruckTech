package br.com.apps.repository.repository.receivable

import androidx.lifecycle.LiveData
import br.com.apps.model.enums.EmployeeReceivableTicket
import br.com.apps.model.model.finance.receivable.Receivable
import br.com.apps.repository.util.Response

interface EmployeeReceivableInterface : EmployeeReceivableReadInterface

interface EmployeeReceivableReadInterface {

    suspend fun fetchReceivablesByParentId(
        id: String,
        type: EmployeeReceivableTicket,
        flow: Boolean = false
    ): LiveData<Response<List<Receivable>>>

    suspend fun fetchReceivableByEmployeeIdAndStatus(
        id: String,
        isReceived: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Receivable>>>

}