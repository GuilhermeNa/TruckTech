package br.com.apps.repository.repository.advance

import androidx.lifecycle.LiveData
import br.com.apps.model.model.payroll.Advance
import br.com.apps.repository.util.Response

interface AdvanceRepositoryI : AdvanceReadI

interface AdvanceReadI {

    suspend fun getAdvanceListByEmployeeIdAndPaymentStatus(
        employeeId: String,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Advance>>>

    suspend fun getAdvanceListByEmployeeIdsAndPaymentStatus(
        employeeIdList: List<String>,
        isPaid: Boolean,
        flow: Boolean = false
    ): LiveData<Response<List<Advance>>>

}
